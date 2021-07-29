package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.Merchant;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.exception.GException;
import com.godfunc.modules.merchant.dto.OrderDTO;
import com.godfunc.modules.merchant.enums.RoleNameEnum;
import com.godfunc.modules.merchant.mapper.OrderMapper;
import com.godfunc.modules.merchant.service.MerchantService;
import com.godfunc.modules.merchant.service.OrderDetailService;
import com.godfunc.modules.merchant.service.OrderService;
import com.godfunc.modules.security.util.SecurityUser;
import com.godfunc.modules.sys.enums.SuperManagerEnum;
import com.godfunc.result.ApiCodeMsg;
import com.godfunc.service.NotifyMerchantService;
import com.godfunc.util.Assert;
import com.godfunc.util.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final MerchantService merchantService;
    private final NotifyMerchantService notifyMerchantService;
    private final OrderDetailService orderDetailService;

    @Override
    public PageDTO<OrderDTO> getPage(Integer page, Integer limit, Integer status,
                                     String payType, String tradeNo, String orderNo,
                                     LocalDateTime createTime) {
        Long merchantId = null;

        Merchant merchant = merchantService.getByUserId(SecurityUser.getUserId());
        if (SecurityUser.getUser().getSuperManager() == SuperManagerEnum.SUPER_MANAGER.getValue() || SecurityUser.checkRole(RoleNameEnum.MANAGE.getValue())) {

        } else if (SecurityUser.checkRole(RoleNameEnum.AGENT.getValue())) {
            merchantId = 0L;
        } else if (SecurityUser.checkRole(RoleNameEnum.MERCHANT.getValue())) {
            merchantId = merchant.getId();
        } else {
            throw new GException(ApiCodeMsg.NOPERMISSION);
        }
        IPage<OrderDTO> resultPage = new Page<>(page, limit);
        List<Order> list = this.baseMapper.selectCustomPage(resultPage, merchantId, status, payType, tradeNo, orderNo, createTime);
        resultPage.setRecords(ConvertUtils.source2Target(list, OrderDTO.class));
        return new PageDTO<OrderDTO>(resultPage);
    }

    @Override
    public Boolean updatePaid(Long id) {
        Order order = getById(id);
        Assert.isNull(order, "订单不存在");
        Assert.isTrue(order.getStatus() != OrderStatusEnum.SCAN.getValue()
                && order.getStatus() != OrderStatusEnum.EXPIRED.getValue(), "只有已扫码和已过期的订单才能修改为已支付");
        return lambdaUpdate().set(Order::getStatus, OrderStatusEnum.PAID.getValue())
                .set(Order::getPayTime, LocalDateTime.now())
                .eq(Order::getId, order.getId())
                .eq(Order::getStatus, order.getStatus()).update();

    }

    @Override
    public Boolean notifyMerchant(Long id) {
        Order order = getById(id);
        Assert.isNull(order, "订单不存在");
        Assert.isTrue(order.getStatus() != OrderStatusEnum.PAID.getValue()
                && order.getStatus() != OrderStatusEnum.FINISH.getValue(), "当前订单状态不能进行通知");
        OrderDetail detail = orderDetailService.getByOrderId(order.getId());
        boolean flag = notifyMerchantService.notifyMerchant(order.getNotifyUrl(), order.getOutTradeNo(),
                order.getOrderNo(), order.getAmount(), order.getRealAmount(),
                order.getPayType(), order.getStatus(), detail.getPlatPrivateKey());
        Assert.isTrue(!flag, "通知商户失败");
        if (order.getStatus() == OrderStatusEnum.PAID.getValue()) {
            return updateStatus(order.getId(), order.getStatus(), OrderStatusEnum.FINISH.getValue());
        } else {
            return updateStatus(order.getId(), order.getStatus(), OrderStatusEnum.FINISH.getValue());
        }
    }

    public boolean updateStatus(Long id, Integer oldStatus, Integer newStatus) {
        return lambdaUpdate().set(oldStatus == OrderStatusEnum.PAID.getValue(), Order::getStatus, newStatus)
                .set(Order::getNotifyTime, LocalDateTime.now())
                .eq(Order::getId, id)
                .eq(Order::getStatus, oldStatus)
                .update();
    }
}
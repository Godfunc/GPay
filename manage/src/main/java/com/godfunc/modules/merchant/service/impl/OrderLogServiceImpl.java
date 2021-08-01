package com.godfunc.modules.merchant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.Merchant;
import com.godfunc.entity.OrderLog;
import com.godfunc.exception.GException;
import com.godfunc.modules.merchant.dto.OrderLogDTO;
import com.godfunc.modules.merchant.enums.RoleNameEnum;
import com.godfunc.modules.merchant.mapper.OrderLogMapper;
import com.godfunc.modules.merchant.service.MerchantService;
import com.godfunc.modules.merchant.service.OrderLogService;
import com.godfunc.modules.security.util.SecurityUser;
import com.godfunc.modules.sys.enums.SuperManagerEnum;
import com.godfunc.result.ApiCodeMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService {

    private final MerchantService merchantService;


    @Override
    public PageDTO<OrderLogDTO> getPage(Integer page, Integer limit, Long orderId) {
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
        IPage<OrderLogDTO> resultPage = new Page<>(page, limit);
        List<OrderLogDTO> list = this.baseMapper.selectCustomPage(resultPage, orderId, merchantId);
        resultPage.setRecords(list);
        return new PageDTO<OrderLogDTO>(resultPage);
    }
}

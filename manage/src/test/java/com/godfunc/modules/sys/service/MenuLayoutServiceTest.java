package com.godfunc.modules.sys.service;

import com.godfunc.modules.sys.entity.Menu;
import com.godfunc.modules.sys.enums.MenuStatusEnum;
import com.godfunc.modules.sys.enums.MenuTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MenuLayoutServiceTest {

    @Autowired
    private MenuService menuService;

    int sort = 0;

    @Test
    public void generateMenu() {
        createLayout();
    }


    private boolean createLayout() {
        createMerchant();
        createChannel();
        createOrder();
        createSystem();
        Menu swagger = new Menu(0L, "http://localhost:9568/swagger-ui/index.html", "Layout", MenuTypeEnum.MENU.getValue(),
                null, "接口文档", false, null, true,
                null, "接口文档", "link", ++sort, MenuStatusEnum.ENABLE.getValue());
        return false;
    }

    private boolean createSystem() {
        Menu systemLayout = new Menu(0L, "/system", "Layout", MenuTypeEnum.MENU.getValue(),
                null, "系统功能", true, null, true,
                null, "系统功能", "system", ++sort, MenuStatusEnum.ENABLE.getValue());
        menuService.save(systemLayout);
        createUser(systemLayout.getId());
        createRole(systemLayout.getId());
        createLog(systemLayout.getId());
        createMenu(systemLayout.getId());
        createConfig(systemLayout.getId());
        return true;
    }

    public boolean createUser(Long layoutId) {
        Menu payCategoryRoute = new Menu(layoutId, "user", "user/index", MenuTypeEnum.MENU.getValue(),
                null, "用户管理", false, "mg:role:list,mg:role:getByUser,merchant:merchant:page", true,
                null, "用户管理", "user", 0, MenuStatusEnum.ENABLE.getValue());
        menuService.save(payCategoryRoute);
        Menu selectBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "mg:user:page", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu addBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增", false, "mg:user:add", true,
                null, null, null, 1, MenuStatusEnum.ENABLE.getValue());
        Menu editBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "修改", false, "mg:user:edit", true,
                null, null, null, 2, MenuStatusEnum.ENABLE.getValue());
        Menu removeBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "删除", false, "mg:user:remove", true,
                null, null, null, 3, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, addBtn, editBtn, removeBtn));
    }

    public boolean createMenu(Long layoutId) {
        Menu payCategoryRoute = new Menu(layoutId, "menu", "menu/index", MenuTypeEnum.MENU.getValue(),
                null, "菜单管理", false, null, true,
                null, "菜单管理", "menu", 1, MenuStatusEnum.ENABLE.getValue());
        menuService.save(payCategoryRoute);
        Menu selectBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "mg:menu:getAll", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu rootAddBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增根菜单", false, "mg:menu:rootadd,mg:menu:add", true,
                null, null, null, 1, MenuStatusEnum.ENABLE.getValue());
        Menu addBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增", false, "mg:menu:add", true,
                null, null, null, 2, MenuStatusEnum.ENABLE.getValue());
        Menu editBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "修改", false, "mg:menu:edit", true,
                null, null, null, 3, MenuStatusEnum.ENABLE.getValue());
        Menu removeBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "删除", false, "mg:menu:remove", true,
                null, null, null, 4, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, rootAddBtn, addBtn, editBtn, removeBtn));
    }

    public boolean createRole(Long layoutId) {
        Menu roleRoute = new Menu(layoutId, "role", "role/index", MenuTypeEnum.MENU.getValue(),
                null, "角色管理", false, null, true,
                null, "角色管理", "role", 2, MenuStatusEnum.ENABLE.getValue());
        menuService.save(roleRoute);
        Menu selectBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "mg:role:page", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu addBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增", false, "mg:role:add,mg:role:getMenus,mg:menu:getTree", true,
                null, null, null, 1, MenuStatusEnum.ENABLE.getValue());
        Menu editBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "修改", false, "mg:role:edit,mg:role:getMenus,mg:menu:getTree", true,
                null, null, null, 2, MenuStatusEnum.ENABLE.getValue());
        Menu removeBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "删除", false, "mg:role:remove", true,
                null, null, null, 3, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, addBtn, editBtn, removeBtn));
    }

    public boolean createLog(Long layoutId) {
        Menu roleRoute = new Menu(layoutId, "log", "log/index", MenuTypeEnum.MENU.getValue(),
                null, "日志", false, null, true,
                null, "日志", "log", 3, MenuStatusEnum.ENABLE.getValue());
        menuService.save(roleRoute);
        Menu selectBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "mg:log:page", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn));
    }

    public boolean createConfig(Long layoutId) {
        Menu roleRoute = new Menu(layoutId, "config", "config/index", MenuTypeEnum.MENU.getValue(),
                null, "配置管理", false, null, true,
                null, "配置管理", "nested", 4, MenuStatusEnum.ENABLE.getValue());
        menuService.save(roleRoute);
        Menu selectBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "mg:config:page", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu addBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增", false, "mg:config:add", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu editBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "修改", false, "mg:config:edit", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu removeBtn = new Menu(roleRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "删除", false, "mg:config:remove", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, addBtn, editBtn, removeBtn));
    }


    private boolean createMerchant() {
        Menu merchantLayout = new Menu(0L, "/merchant", "Layout", MenuTypeEnum.MENU.getValue(),
                null, "商户管理", false, null, true,
                null, "商户管理", "log", ++sort, MenuStatusEnum.ENABLE.getValue());
        menuService.save(merchantLayout);
        Menu merchantRoute = new Menu(merchantLayout.getId(), "merchant", "merchant/index", MenuTypeEnum.MENU.getValue(),
                null, "商户信息", false, null, true,
                null, "商户信息", "user", 0, MenuStatusEnum.ENABLE.getValue());
        menuService.save(merchantRoute);
        Menu selectBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "merchant:merchant:page,mg:user:list,merchant:merchant:keys,merchant:merchant:list", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu addBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增", false, "merchant:merchant:add", true,
                null, null, null, 1, MenuStatusEnum.ENABLE.getValue());
        Menu editBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "修改", false, "merchant:merchant:edit", true,
                null, null, null, 2, MenuStatusEnum.ENABLE.getValue());
        Menu removeBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "删除", false, "merchant:merchant:remove", true,
                null, null, null, 3, MenuStatusEnum.ENABLE.getValue());
        Menu riskSelectBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控查询", false, "merchant:merchantRisk:list", true,
                null, null, null, 4, MenuStatusEnum.ENABLE.getValue());
        Menu riskAddBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控新增", false, "merchant:merchantRisk:add", true,
                null, null, null, 5, MenuStatusEnum.ENABLE.getValue());
        Menu riskEditBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控修改", false, "merchant:merchantRisk:edit", true,
                null, null, null, 6, MenuStatusEnum.ENABLE.getValue());
        Menu riskRemoveBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控删除", false, "merchant:merchantRisk:remove", true,
                null, null, null, 7, MenuStatusEnum.ENABLE.getValue());
        Menu refreshKeysBtn = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "刷新密钥", false, "merchant:merchantRisk:refreshKeys", true,
                null, null, null, 8, MenuStatusEnum.ENABLE.getValue());
        Menu rateList = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "费率查询", false, "merchant:merchantChannelRate:list", true,
                null, null, null, 9, MenuStatusEnum.ENABLE.getValue());
        Menu rateSave = new Menu(merchantRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "费率保存", false, "merchant:merchantChannelRate:save", true,
                null, null, null, 10, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, addBtn, editBtn, removeBtn,
                riskSelectBtn, riskAddBtn, riskEditBtn, riskRemoveBtn, refreshKeysBtn, rateList,
                rateSave));
    }

    private boolean createChannel() {
        Menu channelLayout = new Menu(0L, "/channel", "Layout", MenuTypeEnum.MENU.getValue(),
                null, "渠道管理", true, null, true,
                null, "渠道管理", "tree", ++sort, MenuStatusEnum.ENABLE.getValue());
        menuService.save(channelLayout);
        createCategory(channelLayout.getId());
        createChannel(channelLayout.getId());
        createAccount(channelLayout.getId());
        return true;
    }

    public boolean createCategory(Long layoutId) {
        Menu payCategoryRoute = new Menu(layoutId, "payCategory", "payCategory/index", MenuTypeEnum.MENU.getValue(),
                null, "渠道主类", false, null, true,
                null, "渠道主类", "example", 0, MenuStatusEnum.ENABLE.getValue());
        menuService.save(payCategoryRoute);
        Menu selectBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "merchant:payCategory:page", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu addBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增", false, "merchant:payCategory:add", true,
                null, null, null, 1, MenuStatusEnum.ENABLE.getValue());
        Menu editBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "修改", false, "merchant:payCategory:edit", true,
                null, null, null, 2, MenuStatusEnum.ENABLE.getValue());
        Menu removeBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "删除", false, "merchant:payCategory:remove", true,
                null, null, null, 3, MenuStatusEnum.ENABLE.getValue());
        Menu weightListBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "权重（列表）", false, "merchant:payCategoryChannel:list", true,
                null, null, null, 4, MenuStatusEnum.ENABLE.getValue());
        Menu weightSaveBtn = new Menu(payCategoryRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "权重（保存）", false, "merchant:payCategoryChannel:weight", true,
                null, null, null, 5, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, addBtn, editBtn, removeBtn, weightListBtn, weightSaveBtn));
    }

    public boolean createChannel(Long layoutId) {
        Menu payChannelRoute = new Menu(layoutId, "payChannel", "payChannel/index", MenuTypeEnum.MENU.getValue(),
                null, "渠道子类", false, null, true,
                null, "渠道子类", "form", 1, MenuStatusEnum.ENABLE.getValue());
        menuService.save(payChannelRoute);
        Menu selectBtn = new Menu(payChannelRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "merchant:payChannel:page,merchant:payCategory:list", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu addBtn = new Menu(payChannelRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增", false, "merchant:payChannel:add", true,
                null, null, null, 1, MenuStatusEnum.ENABLE.getValue());
        Menu editBtn = new Menu(payChannelRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "修改", false, "merchant:payChannel:edit,merchant:payCategoryChannel:getByChannel", true,
                null, null, null, 2, MenuStatusEnum.ENABLE.getValue());
        Menu removeBtn = new Menu(payChannelRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "删除", false, "merchant:payChannel:remove", true,
                null, null, null, 3, MenuStatusEnum.ENABLE.getValue());
        Menu riskListBtn = new Menu(payChannelRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控查询", false, "merchant:channelRisk:listByChannel", true,
                null, null, null, 4, MenuStatusEnum.ENABLE.getValue());
        Menu riskAddBtn = new Menu(payChannelRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控新增", false, "merchant:channelRisk:add", true,
                null, null, null, 5, MenuStatusEnum.ENABLE.getValue());
        Menu riskEditBtn = new Menu(payChannelRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控修改", false, "merchant:channelRisk:edit", true,
                null, null, null, 6, MenuStatusEnum.ENABLE.getValue());
        Menu riskRemoveBtn = new Menu(payChannelRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控删除", false, "merchant:channelRisk:remove", true,
                null, null, null, 7, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, addBtn, editBtn, removeBtn, riskListBtn,
                riskAddBtn, riskEditBtn, riskRemoveBtn));
    }

    public boolean createAccount(Long layoutId) {
        Menu payChannelAccountRoute = new Menu(layoutId, "payChannelAccount", "payChannelAccount/index", MenuTypeEnum.MENU.getValue(),
                null, "渠道账号", false, null, true,
                null, "渠道账号", "role", 2, MenuStatusEnum.ENABLE.getValue());
        menuService.save(payChannelAccountRoute);
        Menu selectBtn = new Menu(payChannelAccountRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "merchant:payChannelAccount:page,merchant:payChannel:list", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu addBtn = new Menu(payChannelAccountRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "新增", false, "merchant:payChannelAccount:add", true,
                null, null, null, 1, MenuStatusEnum.ENABLE.getValue());
        Menu editBtn = new Menu(payChannelAccountRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "修改", false, "merchant:payChannelAccount:edit", true,
                null, null, null, 2, MenuStatusEnum.ENABLE.getValue());
        Menu removeBtn = new Menu(payChannelAccountRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "删除", false, "merchant:payChannelAccount:remove", true,
                null, null, null, 3, MenuStatusEnum.ENABLE.getValue());
        Menu riskListBtn = new Menu(payChannelAccountRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控查询", false, "merchant:channelRisk:listByAccount", true,
                null, null, null, 4, MenuStatusEnum.ENABLE.getValue());
        Menu riskAddBtn = new Menu(payChannelAccountRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控新增", false, "merchant:channelRisk:add", true,
                null, null, null, 5, MenuStatusEnum.ENABLE.getValue());
        Menu riskEditBtn = new Menu(payChannelAccountRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控修改", false, "merchant:channelRisk:edit", true,
                null, null, null, 6, MenuStatusEnum.ENABLE.getValue());
        Menu riskRemoveBtn = new Menu(payChannelAccountRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "风控删除", false, "merchant:channelRisk:remove", true,
                null, null, null, 7, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, addBtn, editBtn, removeBtn, riskListBtn,
                riskAddBtn, riskEditBtn, riskRemoveBtn));
    }

    private boolean createOrder() {
        Menu orderLayout = new Menu(0L, "/order", "Layout", MenuTypeEnum.MENU.getValue(),
                null, "订单", false, null, true,
                null, "订单", "log", ++sort, MenuStatusEnum.ENABLE.getValue());
        menuService.save(orderLayout);
        Menu orderRoute = new Menu(orderLayout.getId(), "order", "order/index", MenuTypeEnum.MENU.getValue(),
                null, "订单管理", true, null, true,
                null, "订单管理", "log", 0, MenuStatusEnum.ENABLE.getValue());
        menuService.save(orderRoute);
        Menu selectBtn = new Menu(orderRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "查询", false, "merchant:order:page", true,
                null, null, null, 0, MenuStatusEnum.ENABLE.getValue());
        Menu orderNotify = new Menu(orderRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "通知", false, "merchant:order:notify", true,
                null, null, null, 1, MenuStatusEnum.ENABLE.getValue());
        Menu orderPaid = new Menu(orderRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "补单", false, "merchant:order:updatePaid", true,
                null, null, null, 2, MenuStatusEnum.ENABLE.getValue());
        Menu orderLog = new Menu(orderRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "日志", false, "merchant:orderLog:page", true,
                null, null, null, 3, MenuStatusEnum.ENABLE.getValue());
        Menu createOrder = new Menu(orderRoute.getId(), null, "Button", MenuTypeEnum.BUTTON.getValue(),
                null, "下单", false, "merchant:order:createOrder", true,
                null, null, null, 4, MenuStatusEnum.ENABLE.getValue());
        return menuService.saveBatch(Arrays.asList(selectBtn, orderNotify, orderPaid, orderLog, createOrder));
    }

}

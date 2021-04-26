package com.godfunc.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.dto.PageDTO;
import com.godfunc.modules.security.service.UserTokenService;
import com.godfunc.modules.security.util.SecurityUser;
import com.godfunc.modules.sys.dto.UserDTO;
import com.godfunc.modules.sys.dto.UserInfoDTO;
import com.godfunc.modules.sys.entity.User;
import com.godfunc.modules.sys.entity.UserRole;
import com.godfunc.modules.sys.enums.SuperManagerEnum;
import com.godfunc.modules.sys.enums.UserStatusEnum;
import com.godfunc.modules.sys.mapper.UserMapper;
import com.godfunc.modules.sys.model.RoleMenuModel;
import com.godfunc.modules.sys.model.UserDetail;
import com.godfunc.modules.sys.param.UserAddParam;
import com.godfunc.modules.sys.param.UserEditParam;
import com.godfunc.modules.sys.param.UserPasswordParam;
import com.godfunc.modules.sys.service.RoleMenuService;
import com.godfunc.modules.sys.service.UserRoleService;
import com.godfunc.modules.sys.service.UserService;
import com.godfunc.util.Assert;
import com.godfunc.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author Godfunc
 * @since 2019-12-01
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserRoleService userRoleService;
    private final RoleMenuService roleMenuService;
    private final UserTokenService userTokenService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User getByUsername(String username) {
        return getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUsername(username);
        return getByUser(user);
    }

    @Override
    @Cacheable(cacheNames = "user::detail", key = "#id")
    public UserDetails getByUserId(Long id) {
        User user = getById(id);
        return getByUser(user);
    }

    private UserDetails getByUser(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        UserDetail userDetail = new UserDetail();
        BeanUtils.copyProperties(user, userDetail);
        List<RoleMenuModel> roleMenus = null;
        if (SuperManagerEnum.SUPER_MANAGER.getValue() == user.getSuperManager()) {
            List<UserRole> userRoles = userRoleService.getUserRoles(null);
            userDetail.setRoles(userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet()));
            roleMenus = roleMenuService.getAllRoleMenu();

        } else {
            List<UserRole> userRoles = userRoleService.getUserRoles(user.getId());
            if (CollectionUtils.isNotEmpty(userRoles)) {
                userDetail.setRoles(userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet()));
                roleMenus = roleMenuService.getRoleMenu(userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet()));
            }
        }
        if (CollectionUtils.isNotEmpty(roleMenus)) {
            userDetail.setAuthorities(AuthorityUtils.createAuthorityList(roleMenus.stream().map(x -> x.getPermissions().split(",")).flatMap(Arrays::stream).toArray(String[]::new)));
        }
        return userDetail;
    }

    @Override
    public UserInfoDTO getUserInfo() {
        UserDetail user = SecurityUser.getUser();
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName(user.getUsername());
        userInfoDTO.setAvatar(user.getUsername().substring(0, 1).toUpperCase());
        userInfoDTO.setPermissions(user.getAuthorities() == null ? null : user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        return userInfoDTO;
    }

    @Override
    public PageDTO<UserDTO> getPage(Integer page, Integer limit, Integer status, String username) {
        IPage<UserDTO> resultPage = new Page<>(page, limit);
        UserDetail user = SecurityUser.getUser();
        List<UserDTO> list = null;
        if (SuperManagerEnum.SUPER_MANAGER.getValue() == user.getSuperManager()) {
            list = this.baseMapper.selectCustomPage(resultPage, username, status, null);
        } else {
            list = this.baseMapper.selectCustomPage(resultPage, username, status, SuperManagerEnum.NOT_SUPER_MANAGER.getValue());
        }
        resultPage.setRecords(list);
        return new PageDTO<UserDTO>(resultPage);
    }

    @Override
    public Long add(UserAddParam param) {
        ValidatorUtils.validate(param);
        User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, param.getUsername()));
        Assert.isNotNull(user, "用户名已存在");
        Assert.isTrue(!param.getPassword().equals(param.getConfirmPassword()), "两次输入密码不一样，请重新输入");
        param.setPassword(passwordEncoder.encode(param.getPassword()));
        user = new User();
        BeanUtils.copyProperties(param, user);
        save(user);
        userRoleService.saveRoles(user.getId(), param.getRoles());
        return user.getId();

    }

    @Override
    @CacheEvict(cacheNames = "user::detail", key = "#param.id")
    public Long edit(UserEditParam param) {
        ValidatorUtils.validate(param);
        User user = getById(param.getId());
        Assert.isNull(user, "用户不存在");
        boolean exitUser = count(Wrappers.<User>lambdaQuery().eq(User::getUsername, param.getUsername()).ne(User::getId, param.getId())) > 0;
        Assert.isTrue(exitUser, "用户名已存在");
        if (StringUtils.isNotBlank(param.getPassword()) || StringUtils.isNotBlank(param.getConfirmPassword())) {
            Assert.isTrue(!param.getPassword().equals(param.getConfirmPassword()), "两次输入密码不一样，请重新输入");
            user.setPassword(passwordEncoder.encode(param.getPassword()));
        }
        user.setUsername(param.getUsername());
        user.setGender(param.getGender());
        user.setMobile(param.getMobile());
        user.setSuperManager(param.getSuperManager());
        user.setStatus(param.getStatus());
        updateById(user);
        userRoleService.removeByUserId(user.getId());
        userRoleService.saveRoles(user.getId(), param.getRoles());
        // 清除用户的token
        if (UserStatusEnum.DISABLE.getValue() == param.getStatus()) {
            userTokenService.deleteByUserId(user.getId());
        }
        return user.getId();
    }

    @Override
    @CacheEvict(cacheNames = "user::detail", key = "#id")
    public boolean removeData(Long id) {
        boolean b = removeById(id);
        userRoleService.removeByUserId(id);
        userTokenService.deleteByUserId(id);
        return b;
    }

    @Override
    public boolean password(UserPasswordParam param) {
        ValidatorUtils.validate(param);
        Assert.isTrue(!param.getNewPassword().equals(param.getConfirmNewPassword()), "两次密码输入不一致，请重新输入");
        User user = getById(SecurityUser.getUserId());
        user.setPassword(passwordEncoder.encode(param.getNewPassword()));
        return updateById(user);
    }
}

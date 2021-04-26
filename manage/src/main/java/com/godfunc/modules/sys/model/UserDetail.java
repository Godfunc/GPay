package com.godfunc.modules.sys.model;

import com.godfunc.modules.sys.enums.UserStatusEnum;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Data
public class UserDetail implements Serializable, UserDetails {

    public UserDetail() {
    }

    public UserDetail(Long id, String username, String password, String mobile, Integer superManager, Integer status, Long createId, LocalDateTime createTime, Collection<? extends GrantedAuthority> authorities, Set<Long> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.superManager = superManager;
        this.status = status;
        this.createId = createId;
        this.createTime = createTime;
        this.authorities = authorities;
        this.roles = roles;
    }

    private Long id;
    /**
     * 用户名
     */
    private String username;

    private String password;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 超级管理员 0不是 1是
     */
    private Integer superManager;

    /**
     * 状态 0停用 1启用
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private Collection<? extends GrantedAuthority> authorities;

    private Set<Long> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UserStatusEnum.ENABLE.getValue() == this.status;
    }
}

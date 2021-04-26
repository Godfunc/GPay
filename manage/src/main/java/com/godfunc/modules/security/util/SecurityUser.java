package com.godfunc.modules.security.util;

import com.godfunc.modules.sys.model.UserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
public class SecurityUser {

    public static Authentication getSubject() {
        try {
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户信息
     */
    public static UserDetail getUser() {
        Authentication authentication = getSubject();
        if (authentication == null || authentication.getPrincipal() == null) {
            return new UserDetail();
        }

        UserDetail user = (UserDetail) authentication.getPrincipal();
        if (user == null) {
            return new UserDetail();
        }
        return user;
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return getUser().getId();
    }

}
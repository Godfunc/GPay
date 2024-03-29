package com.godfunc.modules.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godfunc.constant.CommonConstant;
import com.godfunc.modules.security.dto.LoginDTO;
import com.godfunc.modules.security.entity.UserToken;
import com.godfunc.modules.security.param.LoginParam;
import com.godfunc.modules.security.service.CaptchaService;
import com.godfunc.modules.security.service.UserTokenService;
import com.godfunc.modules.sys.model.UserDetail;
import com.godfunc.result.R;
import com.godfunc.util.ResponseUtils;
import com.godfunc.util.ValidatorUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class UsernamePasswordJsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserTokenService userTokenService;
    private final CaptchaService captchaService;

    public UsernamePasswordJsonAuthenticationFilter(AuthenticationManager authenticationManager, UserTokenService userTokenService, CaptchaService captchaService) {
        this.userTokenService = userTokenService;
        this.captchaService = captchaService;
        this.setPostOnly(true);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginParam param = null;
        try {
            param = new ObjectMapper().readValue(request.getInputStream(), LoginParam.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("用户名和密码不能为空");
        }
        ValidatorUtils.validate(param);
        if (!captchaService.validate(param.getUuid(), param.getCaptcha())) {
            throw new AuthenticationServiceException("验证码不正确，请重新输入");
        }
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetail userDetail = (UserDetail) authResult.getPrincipal();
        UserToken userToken = userTokenService.createToken(userDetail.getId());
        ResponseUtils.out(response, R.ok(new LoginDTO(userToken.getToken(), DateTimeFormatter.ofPattern(CommonConstant.DATETIME_FORMAT).format(userToken.getExpireTime()))));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        if (failed instanceof BadCredentialsException) {
            ResponseUtils.out(response, R.fail("用户名或密码错误"));
        } else if (failed instanceof InternalAuthenticationServiceException) {
            ResponseUtils.out(response, R.fail("登录异常"));
        } else if (failed instanceof AuthenticationServiceException) {
            ResponseUtils.out(response, R.fail(failed.getMessage()));
        } else if (failed instanceof DisabledException) {
            ResponseUtils.out(response, R.fail("账号被停用"));
        } else {
            ResponseUtils.out(response, R.fail(failed.getMessage()));
        }
    }
}

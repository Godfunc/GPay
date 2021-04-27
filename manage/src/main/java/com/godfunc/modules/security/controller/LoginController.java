package com.godfunc.modules.security.controller;

import com.godfunc.modules.log.annotation.LogRecord;
import com.godfunc.modules.security.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Godfunc
 */
@RestController
@Api(tags = "登录")
@RequiredArgsConstructor
public class LoginController {

    private final CaptchaService captchaService;

    @GetMapping("captcha")
    @LogRecord("验证码")
    @ApiOperation("验证码")
    @ApiImplicitParam(name = "uuid", value = "uuid", paramType = "query", dataType = "String", dataTypeClass = String.class)
    public void captcha(@ApiIgnore HttpServletResponse response, String uuid) throws IOException {
        BufferedImage image = captchaService.create(uuid);
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        out.close();
    }
}

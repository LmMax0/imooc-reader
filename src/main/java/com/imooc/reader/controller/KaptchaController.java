package com.imooc.reader.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class KaptchaController {
    @Resource
    private Producer kaptchaProducer;

    @GetMapping("/verify_code")
    public void createVerifyCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        // 响应立即过期
        response.setDateHeader("Expires",0);
        // 不存储 不缓存 必须重新进行校验 不缓存任何图片数据
        response.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
        response.setHeader("Cache-Control","post-check=0,pre-check=0");
        response.setHeader("Pragma","no-cache");
        response.setContentType("image/png");
        // 生成验证码字符文本
        String verifyCode = kaptchaProducer.createText();
        request.getSession().setAttribute("kaptchaVerifyCode",verifyCode);

        System.out.println(request.getSession().getAttribute("kaptchaVerifyCode"));
        // 图片文件 --》 二进制
        BufferedImage image = kaptchaProducer.createImage(verifyCode);
        // 二进制文件 getOutputStream 字符的话 用 getWriter
        ServletOutputStream out = response.getOutputStream();
        // javax 提供的图片输出功能
        ImageIO.write(image,"png",out);
        out.flush();
        out.close();
    }
}

package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.constant.MailConstant;
import com.itheima.reggie.constant.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录功能接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String sendMailer;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/getCode")
    public R<Integer> getCode(@RequestParam String email) {
        log.info("收到获取验证码请求，邮箱地址：{}", email);

        //生成六位数验证码
        int code = (int)((Math.random()*9+1)*100000);

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(),true);
            mimeMessageHelper.setFrom(sendMailer); // 邮件发信人
            mimeMessageHelper.setTo(email); // 邮件收信人
            mimeMessageHelper.setSubject(MailConstant.subject); // 邮件标题
            mimeMessageHelper.setText(MailConstant.text + code); // 邮件内容
            mimeMessageHelper.setSentDate(new Date()); // 邮件发送时间

            //发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("发送邮件成功："+sendMailer+"->"+ email);
            
            // 存储验证码以便后续校验
            redisTemplate.opsForValue().set(email, code, MailConstant.CODE_EXPIRATION_TIME, TimeUnit.SECONDS); // 设置验证码过期时间

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送邮件失败："+sendMailer+"->"+ email);
            return R.error("发送验证码失败");
        }

        return R.success(code);
    }

    @PostMapping("/login")
    public R<String> login(@RequestParam String email,
                           @RequestParam String code,
                           HttpServletRequest request) {
        log.info("收到邮箱登录请求，邮箱地址：{}，验证码：{}", email, code);

        // 校验验证码
        Object storedCodeObj = redisTemplate.opsForValue().get(email);
        String storedCode = storedCodeObj != null ? storedCodeObj.toString() : null;

        //登录失败
        if (storedCode == null || !storedCode.equals(code)) {
            return R.error(MessageConstant.EMAIL_ERROR);
        }

        //将登录成功用户id存入session
        request.getSession().setAttribute("user", email);

        return R.success("登录成功");
    }
}

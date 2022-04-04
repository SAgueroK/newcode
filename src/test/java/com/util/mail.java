package com.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class mail {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void test(){
        Context context = new Context();
        context.setVariable("name","sunday");
        String content =templateEngine.process("/mail/demo",context);
        mailClient.sendMail("1912607519@qq.com","Test",content);
    }
}

package com.diyetisyen.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Bean
    @ConditionalOnProperty(prefix = "app.email", name = "enabled", havingValue = "true")
    JavaMailSender javaMailSender(
            @Value("${app.email.host}") String host,
            @Value("${app.email.port:587}") int port,
            @Value("${app.email.username:}") String username,
            @Value("${app.email.password:}") String password) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        Properties properties = sender.getJavaMailProperties();
        properties.put("mail.smtp.auth", !username.isBlank());
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.connectiontimeout", "5000");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.writetimeout", "5000");
        return sender;
    }
}

package com.hireflow.service.impl;

import com.hireflow.config.AppProperties;
import com.hireflow.config.MailProperties;
import com.hireflow.service.EmailService;
import com.hireflow.util.EmailTemplateUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final AppProperties appProperties;

    @Override
    @Async
    public void sendWelcomeEmail(String toEmail, String firstName) {

        String subject = "Welcome to HireFlow!";
        String html = EmailTemplateUtil.welcomeEmail(firstName);

        send(toEmail, subject, html);
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String toEmail, String firstName, String resetToken) {

        String resetLink = appProperties.getFrontendBaseUrl() + "/reset-password?token=" + resetToken;

        String subject = "Reset your HireFlow password";
        String html = EmailTemplateUtil.passwordResetEmail(firstName, resetLink, 15);

        send(toEmail, subject, html);
    }

    @Override
    @Async
    public void sendInterviewScheduledEmail(
            String toEmail,
            String candidateName,
            String jobTitle,
            String companyName,
            LocalDate interviewDate,
            LocalTime interviewTime,
            String round,
            String meetingLink) {

        String subject = "Interview Scheduled - " + jobTitle;

        String html = EmailTemplateUtil.interviewScheduledEmail(
                candidateName, jobTitle, companyName, interviewDate, interviewTime, round, meetingLink);

        send(toEmail, subject, html);
    }

    @Override
    @Async
    public void sendInterviewUpdatedEmail(
            String toEmail,
            String candidateName,
            String jobTitle,
            String companyName,
            LocalDate interviewDate,
            LocalTime interviewTime,
            String round,
            String meetingLink) {

        String subject = "Interview Updated - " + jobTitle;

        String html = EmailTemplateUtil.interviewUpdatedEmail(
                candidateName, jobTitle, companyName, interviewDate, interviewTime, round, meetingLink);

        send(toEmail, subject, html);
    }

    @Override
    @Async
    public void sendInterviewCancelledEmail(
            String toEmail,
            String candidateName,
            String jobTitle,
            String companyName) {

        String subject = "Interview Cancelled - " + jobTitle;

        String html = EmailTemplateUtil.interviewCancelledEmail(candidateName, jobTitle, companyName);

        send(toEmail, subject, html);
    }

    @Override
    @Async
    public void sendApplicationStatusChangedEmail(
            String toEmail,
            String candidateName,
            String jobTitle,
            String companyName,
            String status) {

        String subject = "Application Update - " + jobTitle;

        String html = EmailTemplateUtil.applicationStatusChangedEmail(
                candidateName, jobTitle, companyName, humanize(status));

        send(toEmail, subject, html);
    }

    private void send(String to, String subject, String html) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailProperties.getFrom(), mailProperties.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException ex) {
            log.error("Failed to send email to {} with subject '{}': {}", to, subject, ex.getMessage(), ex);
        } catch (Exception ex) {
            // Catch-all so a mail-server outage never breaks the calling business transaction.
            log.error("Unexpected error sending email to {}: {}", to, ex.getMessage(), ex);
        }
    }

    private String humanize(String enumName) {

        if (enumName == null || enumName.isBlank()) {
            return "";
        }

        String[] words = enumName.split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }
}
package com.hireflow.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EmailService {

    void sendWelcomeEmail(String toEmail, String firstName);

    void sendPasswordResetEmail(String toEmail, String firstName, String resetToken);

    void sendInterviewScheduledEmail(
            String toEmail,
            String candidateName,
            String jobTitle,
            String companyName,
            LocalDate interviewDate,
            LocalTime interviewTime,
            String round,
            String meetingLink);

    void sendInterviewUpdatedEmail(
            String toEmail,
            String candidateName,
            String jobTitle,
            String companyName,
            LocalDate interviewDate,
            LocalTime interviewTime,
            String round,
            String meetingLink);

    void sendInterviewCancelledEmail(
            String toEmail,
            String candidateName,
            String jobTitle,
            String companyName);

    void sendApplicationStatusChangedEmail(
            String toEmail,
            String candidateName,
            String jobTitle,
            String companyName,
            String status);
}
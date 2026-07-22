package com.hireflow.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class EmailTemplateUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    private EmailTemplateUtil() {
    }

    public static String welcomeEmail(String firstName) {

        String body = """
                <h2>Welcome to HireFlow, %s!</h2>
                <p>Your account has been created successfully. You can now sign in and start exploring
                jobs, managing applications, or posting openings depending on your role.</p>
                <p>If you didn't create this account, please contact our support team immediately.</p>
                """.formatted(escape(firstName));

        return wrapInLayout("Welcome to HireFlow", body);
    }

    public static String passwordResetEmail(String firstName, String resetLink, int expiryMinutes) {

        String body = """
                <h2>Reset your password</h2>
                <p>Hi %s,</p>
                <p>We received a request to reset your HireFlow password. Click the button below to
                choose a new one. This link expires in %d minutes.</p>
                <p style="text-align:center; margin: 32px 0;">
                    <a href="%s" style="background-color:#2563eb; color:#ffffff; padding:12px 24px;
                    border-radius:6px; text-decoration:none; font-weight:bold;">Reset Password</a>
                </p>
                <p>If you didn't request this, you can safely ignore this email — your password will
                remain unchanged.</p>
                """.formatted(escape(firstName), expiryMinutes, resetLink);

        return wrapInLayout("Reset your HireFlow password", body);
    }

    public static String interviewScheduledEmail(
            String candidateName,
            String jobTitle,
            String companyName,
            LocalDate date,
            LocalTime time,
            String round,
            String meetingLink) {

        String body = """
                <h2>Your interview is scheduled</h2>
                <p>Hi %s,</p>
                <p>Your interview for <strong>%s</strong> at <strong>%s</strong> has been scheduled.</p>
                %s
                <p>Good luck!</p>
                """.formatted(
                escape(candidateName),
                escape(jobTitle),
                escape(companyName),
                interviewDetailsTable(date, time, round, meetingLink)
        );

        return wrapInLayout("Interview Scheduled - " + jobTitle, body);
    }

    public static String interviewUpdatedEmail(
            String candidateName,
            String jobTitle,
            String companyName,
            LocalDate date,
            LocalTime time,
            String round,
            String meetingLink) {

        String body = """
                <h2>Your interview has been updated</h2>
                <p>Hi %s,</p>
                <p>Your interview for <strong>%s</strong> at <strong>%s</strong> has been rescheduled.
                Please find the updated details below.</p>
                %s
                """.formatted(
                escape(candidateName),
                escape(jobTitle),
                escape(companyName),
                interviewDetailsTable(date, time, round, meetingLink)
        );

        return wrapInLayout("Interview Updated - " + jobTitle, body);
    }

    public static String interviewCancelledEmail(
            String candidateName,
            String jobTitle,
            String companyName) {

        String body = """
                <h2>Your interview has been cancelled</h2>
                <p>Hi %s,</p>
                <p>Your interview for <strong>%s</strong> at <strong>%s</strong> has been cancelled.
                We'll reach out if it's rescheduled.</p>
                """.formatted(escape(candidateName), escape(jobTitle), escape(companyName));

        return wrapInLayout("Interview Cancelled - " + jobTitle, body);
    }

    public static String applicationStatusChangedEmail(
            String candidateName,
            String jobTitle,
            String companyName,
            String status) {

        String body = """
                <h2>Your application status has changed</h2>
                <p>Hi %s,</p>
                <p>Your application for <strong>%s</strong> at <strong>%s</strong> has a new status:</p>
                <p style="text-align:center; margin: 24px 0;">
                    <span style="background-color:#eef2ff; color:#2563eb; padding:8px 20px;
                    border-radius:20px; font-weight:bold;">%s</span>
                </p>
                """.formatted(escape(candidateName), escape(jobTitle), escape(companyName), escape(status));

        return wrapInLayout("Application Update - " + jobTitle, body);
    }

    private static String interviewDetailsTable(
            LocalDate date,
            LocalTime time,
            String round,
            String meetingLink) {

        StringBuilder rows = new StringBuilder();

        rows.append(row("Date", date != null ? date.format(DATE_FORMATTER) : "TBD"));
        rows.append(row("Time", time != null ? time.format(TIME_FORMATTER) : "TBD"));

        if (round != null && !round.isBlank()) {
            rows.append(row("Round", escape(round)));
        }

        if (meetingLink != null && !meetingLink.isBlank()) {
            rows.append(row("Meeting Link", "<a href=\"" + meetingLink + "\">" + escape(meetingLink) + "</a>"));
        }

        return """
                <table style="width:100%%; border-collapse:collapse; margin:16px 0;">
                %s
                </table>
                """.formatted(rows);
    }

    private static String row(String label, String value) {
        return """
                <tr>
                    <td style="padding:8px 0; color:#6b7280; width:140px;">%s</td>
                    <td style="padding:8px 0; color:#111827; font-weight:600;">%s</td>
                </tr>
                """.formatted(label, value);
    }

    private static String wrapInLayout(String title, String bodyHtml) {

        return """
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"><title>%s</title></head>
                <body style="font-family: Arial, sans-serif; background-color:#f3f4f6; padding:24px; margin:0;">
                    <div style="max-width:600px; margin:0 auto; background-color:#ffffff; border-radius:8px; overflow:hidden;">
                        <div style="background-color:#111827; padding:20px 32px;">
                            <span style="color:#ffffff; font-size:20px; font-weight:bold;">HireFlow</span>
                        </div>
                        <div style="padding:32px; color:#111827; line-height:1.6;">
                            %s
                        </div>
                        <div style="padding:20px 32px; background-color:#f9fafb; color:#9ca3af; font-size:12px;">
                            This is an automated message from HireFlow. Please do not reply to this email.
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(escape(title), bodyHtml);
    }

    private static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
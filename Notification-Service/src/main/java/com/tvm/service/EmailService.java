package com.tvm.service;



import com.tvm.dto.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendSimple(SimpleEmailRequest req) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(req.getTo());
        message.setSubject(req.getSubject());
        message.setText(req.getBody());
        mailSender.send(message);
    }

    public void sendLeaveStatusMail(LeaveStatusEmailRequest req) {
        String subject = "Leave " + req.getStatus();
        String body = "Hi " + req.getEmployeeName() + ",\n\n" +
                "Your leave request (" + req.getLeaveRange() + ") has been " + req.getStatus() + ".\n" +
                (req.getManagerComments() != null ? "Manager comments: " + req.getManagerComments() + "\n\n" : "\n") +
                "Regards,\nHR Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(req.getTo());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendOnboardingMail(OnboardingEmailRequest req) {
        String subject = "Welcome to the company!";
        String body = "Hi " + req.getEmployeeName() + ",\n\n" +
                "Welcome aboard! We're excited to have you on the team.\n\n" +
                "Regards,\nHR Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(req.getTo());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendPayslip(PayslipEmailRequest req) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(req.getTo());
            helper.setSubject("Payslip for " + req.getMonth());
            helper.setText("Hi " + req.getEmployeeName() + ",\n\nPlease find your payslip attached.\n\nRegards,\nPayroll Team");

            helper.addAttachment(req.getFileName(), () -> new java.io.ByteArrayInputStream(req.getPdf()));

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send payslip email: " + e.getMessage(), e);
        }
    }
}


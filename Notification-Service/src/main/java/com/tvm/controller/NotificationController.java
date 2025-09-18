package com.tvm.controller;


import com.tvm.dto.*;
import com.tvm.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PostMapping("/send")
    public ResponseEntity<String> sendSimple(@Valid @RequestBody SimpleEmailRequest request) {
        emailService.sendSimple(request);
        return ResponseEntity.ok("Mail sent");
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PostMapping("/leave-status")
    public ResponseEntity<String> sendLeaveStatus(@Valid @RequestBody LeaveStatusEmailRequest request) {
        emailService.sendLeaveStatusMail(request);
        return ResponseEntity.ok("Leave status email sent");
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PostMapping("/onboarding")
    public ResponseEntity<String> onboarding(@Valid @RequestBody OnboardingEmailRequest request) {
        emailService.sendOnboardingMail(request);
        return ResponseEntity.ok("Onboarding email sent");
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PostMapping("/payslip")
    public ResponseEntity<String> payslip(@Valid @RequestBody PayslipEmailRequest request) {
        emailService.sendPayslip(request);
        return ResponseEntity.ok("Payslip email sent");
    }
}


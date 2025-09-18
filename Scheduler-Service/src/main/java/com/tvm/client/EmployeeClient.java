package com.tvm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "employee-service", path = "/api/employees", configuration = FeignClientConfig.class)
public interface EmployeeClient {
    @GetMapping("/all/ids")
    List<Long> getAllEmployeeIds();

    @GetMapping("/all/emails")
    List<String> getAllEmployeeEmails();

    @PostMapping("/emails/by-usernames")
    List<String> getEmailsByUsernames(@RequestBody List<String> usernames);
}

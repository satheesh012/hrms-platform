package com.tvm.controller;


import com.tvm.model.Role;
import com.tvm.model.UserEntity;
import com.tvm.repository.UserRepository;
import com.tvm.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserEntity user){
        try{
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword()
            ));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");
            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(Map.of("token",token,
                    "username", userDetails.getUsername(),
                    "role", role
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error","Invalid Username and Password"));
        }
    }


    @GetMapping("/usernames/by-role")
    public List<String> getUsernamesByRole(@RequestParam String role) {
        return userRepo.findByRole(Role.valueOf(role.toUpperCase()))
                .stream()
                .map(UserEntity::getUsername)
                .toList();
    }


}


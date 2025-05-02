package com.avswasthya.healthapp.auth.service;

import com.avswasthya.healthapp.auth.dto.LoginRequest;
import com.avswasthya.healthapp.auth.model.User;
import com.avswasthya.healthapp.auth.repository.UserRepository;
import com.avswasthya.healthapp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder ,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;

    }


    @Autowired
    AuthenticationManager authenticationManager;

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }


    public String verify(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                String username = authentication.getName();

                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found after authentication"));
                return jwtUtil.generateToken(username,user.getId());
            } else {
                throw new BadCredentialsException("Authentication failed.");
            }
        } catch (BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during authentication: ", e);
            throw new RuntimeException("Internal server error");
        }
    }

    // Assuming you have a repository method to check if the email exists
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsMobileNumber(String mobileNumber) {
        return userRepository.existsByMobileNumber(mobileNumber);
    }

}

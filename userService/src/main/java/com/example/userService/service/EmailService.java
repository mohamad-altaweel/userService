package com.example.userService.service;

import com.example.userService.exception.DuplicateException;
import com.example.userService.exception.UserNotFoundException;
import com.example.userService.model.EmailVerificationToken;
import com.example.userService.model.Passwordtoken;
import com.example.userService.model.User;
import com.example.userService.repository.EmailVerificationTokenRepository;
import com.example.userService.repository.PasswordTokenRepository;
import com.example.userService.repository.RoleRepository;
import com.example.userService.repository.UserRepository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final SecureRandom secureRandom;
    private final Base64.Encoder base64Encoder;
    private final int BYTELENGTH = 8;

    @Autowired
    public EmailService(UserRepository userRepository, PasswordTokenRepository passwordTokenRepository, EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.secureRandom = new SecureRandom(); // threadsafe
        this.base64Encoder = Base64.getUrlEncoder();

    }

    private String generateToken(int byteLength) {
        byte[] randomBytes = new byte[byteLength];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }


    public void sendPasswordResetEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("User with the email address '%s' not found.", email));
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 15);
        String string_token = generateToken(BYTELENGTH);
        Passwordtoken token = new Passwordtoken(user.get(), calendar.getTime(), string_token);
        passwordTokenRepository.save(token);
        mailMessage.setTo(user.get().getEmail());
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the link below:\n" + "http://localhost:8080/reset-password?token=" + token.getToken());
        mailSender.send(mailMessage);
    }

    public void sendVerificationToken(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("User with the email address '%s' not found.", email));
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 2);
        String string_token = generateToken(16);
        EmailVerificationToken token = new EmailVerificationToken(user.get(), calendar.getTime(), string_token);
        emailVerificationTokenRepository.save(token);
        mailMessage.setTo(user.get().getEmail());
        mailMessage.setSubject("Email Verification Request");
        mailMessage.setText("To verifiy your email, click the link below:\n" + "http://localhost:8080/verifyEmail?token=" + token.getToken());
        mailSender.send(mailMessage);
    }
}

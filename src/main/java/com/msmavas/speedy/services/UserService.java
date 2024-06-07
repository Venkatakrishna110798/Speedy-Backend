package com.msmavas.speedy.services;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.msmavas.speedy.models.User;
import com.msmavas.speedy.repositories.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(User user) {
        String otp = generateOtp();
        try {
            sendOtpToMail(user.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send the OTP to the given email address", e);
        }

        user.setOtp(otp);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       
        userRepository.save(user);

        return "An OTP has been sent to your email. Please check and verify.";
    }

    private void sendOtpToMail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Your One Time Password (OTP)");
        mimeMessageHelper.setText("Your OTP is: " + otp);
        javaMailSender.send(mimeMessage);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public User verifyUser(String email, String otp) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        if (!user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
       
        user.setOtp(null);  
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; 
        }
        return null;
    }

    public User getCustomerById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateCustomerById(int id, User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setName(newUser.getName());
                    user.setMobileNumber(newUser.getMobileNumber());
                    user.setPassword(passwordEncoder.encode(newUser.getPassword())); // encode the password
                    user.setOtp(newUser.getOtp());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllCustomers() {
        return userRepository.findAll();
    }
}

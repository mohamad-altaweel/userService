package com.example.userService.service;

import com.example.userService.model.*;
import com.example.userService.repository.EmailVerificationTokenRepository;
import com.example.userService.repository.PasswordTokenRepository;
import com.example.userService.repository.RoleRepository;
import com.example.userService.repository.UserRepository;
import com.example.userService.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import com.example.userService.controller.dto.SignupRequest;
import com.example.userService.exception.DuplicateException;
import com.example.userService.exception.TokenInvalid;



@SuppressWarnings("preview")
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PasswordTokenRepository passwordtokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
     PasswordTokenRepository passwordtokenRepository, EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.passwordtokenRepository = passwordtokenRepository;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    // Method to create/save a new user
    public User createUser(User user) {
        // Here, you might want to encode the password or perform other pre-save actions
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }    

    // Method to retrieve a user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Method to retrieve all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Method to update a user
    public User updateUser(User updatedUser) {
        // Find the user to be updated from the repository
        User user = userRepository.findById(updatedUser.getId())
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + updatedUser.getId()));
        // Update user properties here, e.g.:
        user.setEmail(updatedUser.getEmail());
        user.setAddress(updatedUser.getStreet(), updatedUser.gethouse(), user.getpostcode());
        user.setPhonenumber(updatedUser.getPhonenumber());
        return userRepository.save(user);
    }

    // Method to delete a user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void signup(SignupRequest request) {
      String email = request.email();
      String hashedPassword = passwordEncoder.encode(request.password());
      // Check if user exists
      Optional<User> existingUser = userRepository.findByEmail(email);
      if (existingUser.isPresent()) {
        throw new DuplicateException(String.format("User with the email address '%s' already exists.", email));
      }
      String phonenumber = (request.phonenumber().isEmpty()) ? "" : request.phonenumber();
      String street = (request.street().isEmpty()) ? "" : request.street();
      String house = (request.house().isEmpty()) ? "" : request.house();
      String postcode = (request.postcode().isEmpty()) ? "" : request.postcode();
      Role role = roleRepository.findByName("USER");
      User user = new User(email, hashedPassword, role,street,phonenumber, house, postcode);
      userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword){
        Optional<Passwordtoken> tokenObject = passwordtokenRepository.findByToken(token);
        if (!tokenObject.isPresent()) {
            throw new TokenInvalid("Token is invalid");
        }
        Long Id = tokenObject.get().getUser().getId();
        User user = userRepository.findById(Id)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + Id));
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public boolean isPasswordResetTokenValid(String token){
        Optional<Passwordtoken> tokenObject = passwordtokenRepository.findByToken(token);
        if (!tokenObject.isPresent()) {
            return false;
        }
        return !isTokenExpired(tokenObject.get());
    }

    private boolean isTokenExpired(Passwordtoken token) {
            final Calendar cal = Calendar.getInstance();
            return token.getExpiryDate().before(cal.getTime());
    }

    public boolean verifyUser(String token) {
        boolean verified = false;
        Optional<EmailVerificationToken> verificationToken = emailVerificationTokenRepository.findByToken(token);
        final Calendar cal = Calendar.getInstance();
        if (!verificationToken.isPresent() || verificationToken.get().getExpiryDate().before(cal.getTime())) {
            throw new TokenInvalid("Token is invalid");
        }
        Long Id = verificationToken.get().getUser().getId();
        User user = userRepository.findById(Id)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + Id));
        user.setVerified(true);
        userRepository.save(user);
        verified = true;
        return verified;
    }

    // Additional methods as per your business logic can be added here
}

package com.eyeshop.user.service;

import com.eyeshop.user.entity.Role;
import com.eyeshop.user.entity.User;
import com.eyeshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // CUSTOMER Methods

    // ----Register User----
    public User registerUser(User user) {
        // Check if user with the same email already exists
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists: "+ user.getEmail());
        }
        // Set the default role to CUSTOMER
        user.setRole(Role.CUSTOMER);
        // Save the user to the database
        return userRepository.save(user);

    }
    // ----Update User----
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    // Admin Methods

    // ----Get User By Id----
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id ));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }



    // ----Delete User----
    public void deleteUser(Long id) {
        getUserById(id); // Check if user exists before deleting
        userRepository.deleteById(id);
    }
}

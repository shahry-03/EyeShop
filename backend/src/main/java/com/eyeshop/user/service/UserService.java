package com.eyeshop.user.service;

import com.eyeshop.user.dto.request.UserRegisterRequest;
import com.eyeshop.user.dto.request.UserUpdateRequest;
import com.eyeshop.user.dto.response.UserResponse;
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
    public UserResponse registerUser(UserRegisterRequest request) {
        // Check if user with the same email already exists
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists: "+ request.getEmail());
        }
        // Create a new user entity
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.CUSTOMER)  // Set the default role to CUSTOMER
                .build();
        // Save the user to the database
        return UserResponse.fromEntity(userRepository.save(user));
    }
    // ----Update User----
    public UserResponse updateUser(Long id, UserUpdateRequest updateRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        existingUser.setUsername(updateRequest.getUsername());
        existingUser.setEmail(updateRequest.getEmail());
        return UserResponse.fromEntity(userRepository.save(existingUser));
    }

    // Admin Methods

    // ----Get User By Id----
    public UserResponse getUserById(Long id) {
        return UserResponse.fromEntity(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id )));
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }



    // ----Delete User----
    public void deleteUser(Long id) {
        // Check if user exists before deleting
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.deleteById(id);
    }
}

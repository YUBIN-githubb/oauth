package com.example.oauth.domain.user.service;

import com.example.oauth.common.exception.CustomException;
import com.example.oauth.domain.user.entity.User;
import com.example.oauth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public Boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));
    }
}

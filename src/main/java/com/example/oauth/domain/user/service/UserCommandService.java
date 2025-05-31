package com.example.oauth.domain.user.service;

import com.example.oauth.common.exception.CustomException;
import com.example.oauth.domain.user.entity.User;
import com.example.oauth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
// create, delete
public class UserCommandService {

    private final UserRepository userRepository;

    public User create(String email, String password) {
        User user = User.create(email, password);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "User Not Found")
        );
        foundUser.updateDeletedAt(LocalDateTime.now());
    }
}

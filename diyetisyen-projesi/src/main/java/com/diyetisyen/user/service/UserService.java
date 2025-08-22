package com.diyetisyen.user.service;

import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Set<User> findAllUsers() {
        return Set.copyOf(userRepository.findAll());
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}

package com.diyetisyen.user.service;

import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- Genel CRUD ve Sorgulama Metotları ---

    public User save(User u) { return userRepository.save(u); }
    public Optional<User> findById(Long id) { return userRepository.findById(id); }
    public Optional<User> findByUsername(String username) { return userRepository.findByUsername(username); }
    public List<User> findAll() { return userRepository.findAll(); }
    public void deleteById(Long id) { userRepository.deleteById(id); }

    // --- Spring Security Metodu (Düzeltilmiş) ---

    /**
     * Kullanıcı adı ile veritabanından kullanıcıyı yükler ve Spring Security için bir UserDetails nesnesi döndürür.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Sadece entity'yi bul ve döndür.
        // Kendi User sınıfın UserDetails olduğu için otomatik olarak kabul edilecektir.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
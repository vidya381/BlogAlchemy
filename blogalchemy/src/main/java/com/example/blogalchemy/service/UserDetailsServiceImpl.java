package com.example.blogalchemy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blogalchemy.model.User;
import com.example.blogalchemy.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new UsernameNotFoundException("Invalid user data for: " + username);
        }

        // Handle role - ensure it's not null and add ROLE_ prefix if needed
        String role = user.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "USER"; // Default role
        }
        
        // Ensure role doesn't have ROLE_ prefix (Spring Security will add it)
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
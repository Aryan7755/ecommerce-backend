package com.aryan.ecommerce_backend.security.user;

import com.aryan.ecommerce_backend.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email){
        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User not found: " + email));
    }

}

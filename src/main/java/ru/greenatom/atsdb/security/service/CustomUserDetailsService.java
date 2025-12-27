package ru.greenatom.atsdb.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.greenatom.atsdb.model.database.entity.User;
import ru.greenatom.atsdb.model.database.repository.UserRepository;
import ru.greenatom.atsdb.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found"));
        return new CustomUserDetails(user);
    }
}

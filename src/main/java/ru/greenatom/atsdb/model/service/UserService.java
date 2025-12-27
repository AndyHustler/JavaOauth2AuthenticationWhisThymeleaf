package ru.greenatom.atsdb.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.greenatom.atsdb.model.database.entity.User;
import ru.greenatom.atsdb.model.database.repository.UserRepository;
import ru.greenatom.atsdb.model.dto.request.RegisterRequest;
import ru.greenatom.atsdb.model.enumeration.ERole;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public boolean registration(RegisterRequest dto) {
        if(dto.getUserLogin().length() < 4 || dto.getPassword().length() < 1 || repository.existsByLogin(dto.getUserLogin())) return false;
        User user = User.builder()
                    .userName(dto.getFirstName() + " " + dto.getLastName() + " " + dto.getSurName())
                    .login(dto.getUserLogin())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .role(ERole.admin())
                    .build();
        repository.save(user);
        return true;
    }

    public User getByLogin(String login) {
        return repository.findByLogin(login).orElseThrow();
    }
}

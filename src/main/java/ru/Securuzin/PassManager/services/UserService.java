package ru.Securuzin.PassManager.services;


import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Securuzin.PassManager.dto.AuthResponse;
import ru.Securuzin.PassManager.dto.LoginRequest;
import ru.Securuzin.PassManager.dto.RegisterRequest;
import ru.Securuzin.PassManager.model.User;
import ru.Securuzin.PassManager.repositories.UserRepository;
import ru.Securuzin.PassManager.security.JwtProvider;
import ru.Securuzin.PassManager.util.NotValidData;
import ru.Securuzin.PassManager.util.User.UserAlreadyExistsException;
import ru.Securuzin.PassManager.util.User.UserNotFound;
import ru.Securuzin.PassManager.util.password.PasswordNotCorrect;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (request.getMasterPassword() == null || request.getMasterPassword().isEmpty()) {
            throw new NotValidData("Пароль не может быть пустым");
        }
        if (!request.getMasterPassword().equals(request.getMasterPasswordConfirm())) {
            throw new NotValidData("Пароли не совпадают");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email уже занят");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username уже занят");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setSalt(UUID.randomUUID().toString());
        user.setMasterPasswordHash(passwordEncoder.encode(request.getMasterPassword()));
        User savedUser = userRepository.save(user);

        String token = jwtProvider.generateToken(savedUser.getUsername(), savedUser.getId());

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                token,
                "Успешная регистрация"
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UserNotFound("User not found"));

        if (!passwordEncoder.matches(request.getMasterPassword(), user.getMasterPasswordHash())) {
            throw new PasswordNotCorrect("Pass is wrong");
        }


        String token = jwtProvider.generateToken(user.getUsername(), user.getId());
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                token,
                "Успешная авторизация"
        );
    }



}

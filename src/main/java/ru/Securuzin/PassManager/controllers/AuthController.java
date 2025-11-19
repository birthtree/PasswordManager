package ru.Securuzin.PassManager.controllers;


import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.Securuzin.PassManager.dto.AuthResponse;
import ru.Securuzin.PassManager.dto.LoginRequest;
import ru.Securuzin.PassManager.dto.RegisterRequest;
import ru.Securuzin.PassManager.model.User;
import ru.Securuzin.PassManager.services.UserService;
import ru.Securuzin.PassManager.util.NotValidData;
import ru.Securuzin.PassManager.util.User.UserAlreadyExistsException;
import ru.Securuzin.PassManager.util.User.UserNotFound;
import ru.Securuzin.PassManager.util.password.PasswordNotCorrect;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = userService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotValidData e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (PasswordNotCorrect e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

package ru.Securuzin.PassManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.Securuzin.PassManager.dto.Password.CreatePasswordRequest;
import ru.Securuzin.PassManager.dto.Password.PasswordEntryResponse;
import ru.Securuzin.PassManager.dto.Password.UpdatePasswordRequest;
import ru.Securuzin.PassManager.security.UserDetailsImpl;
import ru.Securuzin.PassManager.services.PasswordEntryService;

import java.util.List;

@RestController
@RequestMapping("/api/passwords")
public class PasswordEntryController {

    private final PasswordEntryService passwordEntryService;

    @Autowired
    public PasswordEntryController(PasswordEntryService passwordEntryService) {
        this.passwordEntryService = passwordEntryService;
    }


    @PostMapping
    public ResponseEntity<?> createPassword(@RequestBody CreatePasswordRequest request) throws Exception {
        Long userId = getUserIdFromAuth();
        PasswordEntryResponse response = passwordEntryService.createPasswordEntry(request, userId);
        return ResponseEntity.ok(response);
    }



    @GetMapping
    public ResponseEntity<?> getAllPasswords() {
        try {
            Long userId = getUserIdFromAuth();
            List<PasswordEntryResponse> passwords = passwordEntryService.getAllPasswordsByUser(userId);
            return ResponseEntity.ok(passwords);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка получения паролей: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPasswordById(@PathVariable Long id) {
        try {
            Long userId = getUserIdFromAuth();
            PasswordEntryResponse response = passwordEntryService.getPasswordById(id, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка получения пароля: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable Long id,
                                            @RequestBody UpdatePasswordRequest request) {
        try {
            Long userId = getUserIdFromAuth();
            PasswordEntryResponse response = passwordEntryService.updatePassword(id, request, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка обновления пароля: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePassword(@PathVariable Long id) {
        try {
            Long userId = getUserIdFromAuth();
            passwordEntryService.deletePassword(id, userId);
            return ResponseEntity.ok("Пароль успешно удалён");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка удаления пароля: " + e.getMessage());
        }
    }

    private Long getUserIdFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getDetails();
    }
}

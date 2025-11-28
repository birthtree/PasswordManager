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
import ru.Securuzin.PassManager.services.AuditLogService;
import ru.Securuzin.PassManager.services.ExportService;
import ru.Securuzin.PassManager.services.PasswordEntryService;

import java.util.List;

@RestController
@RequestMapping("/api/passwords")
public class PasswordEntryController {

    private final PasswordEntryService passwordEntryService;
    private final ExportService exportService;
    private final AuditLogService auditLogService;
    @Autowired
    public PasswordEntryController(PasswordEntryService passwordEntryService, ExportService exportService, AuditLogService auditLogService) {
        this.passwordEntryService = passwordEntryService;
        this.exportService = exportService;
        this.auditLogService = auditLogService;
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchPasswords(@RequestParam String q) {
        try {
            if (q == null || q.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Параметр поиска 'q' не может быть пустым");
            }

            Long userId = getUserIdFromAuth();
            List<PasswordEntryResponse> results = passwordEntryService.searchPasswords(q, userId);

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка поиска: " + e.getMessage());
        }
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
    @GetMapping("/export/json")
    public ResponseEntity<?> exportJSON() {
        try {
            Long userId = getUserIdFromAuth();
            String jsonContent = exportService.exportToJSON(userId);

            auditLogService.logUserAction(userId, "EXPORT_JSON");

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=passwords.json")
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(jsonContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка экспорта: " + e.getMessage());
        }
    }

    /**
     * GET /api/passwords/export/csv
     * Скачать все пароли в CSV
     */
    @GetMapping("/export/csv")
    public ResponseEntity<?> exportCSV() {
        try {
            Long userId = getUserIdFromAuth();
            String csvContent = exportService.exportToCSV(userId);

            auditLogService.logUserAction(userId, "EXPORT_CSV");

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=passwords.csv")
                    .header("Content-Type", "text/csv; charset=UTF-8")
                    .body(csvContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка экспорта: " + e.getMessage());
        }
    }
}

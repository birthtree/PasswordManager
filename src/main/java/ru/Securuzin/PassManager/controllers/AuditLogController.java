package ru.Securuzin.PassManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.Securuzin.PassManager.model.AuditLog;
import ru.Securuzin.PassManager.services.AuditLogService;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Autowired
    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    /**
     * GET /api/audit-logs - получить все логи текущего пользователя
     */
    @GetMapping
    public ResponseEntity<?> getUserAuditLogs() {
        try {
            Long userId = getUserIdFromAuth();
            List<AuditLog> logs = auditLogService.getUserAuditLogs(userId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка получения логов: " + e.getMessage());
        }
    }

    /**
     * GET /api/audit-logs?limit=10 - получить последние N логов
     */
    @GetMapping(params = "limit")
    public ResponseEntity<?> getUserAuditLogsLimited(@RequestParam int limit) {
        try {
            Long userId = getUserIdFromAuth();
            List<AuditLog> logs = auditLogService.getUserAuditLogsLimited(userId, limit);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка получения логов: " + e.getMessage());
        }
    }

    /**
     * GET /api/audit-logs/password/{passwordId} - получить логи конкретного пароля
     */
    @GetMapping("/password/{passwordId}")
    public ResponseEntity<?> getPasswordAuditLogs(@PathVariable Long passwordId) {
        try {
            List<AuditLog> logs = auditLogService.getPasswordAuditLogs(passwordId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка получения логов: " + e.getMessage());
        }
    }

    /**
     * Вспомогательный метод для получения userId из SecurityContext
     */
    private Long getUserIdFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getDetails();
    }
}

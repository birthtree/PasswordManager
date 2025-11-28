package ru.Securuzin.PassManager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Securuzin.PassManager.model.AuditLog;
import ru.Securuzin.PassManager.model.PasswordEntry;
import ru.Securuzin.PassManager.model.User;
import ru.Securuzin.PassManager.repositories.AuditLogRepository;
import ru.Securuzin.PassManager.repositories.UserRepository;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    /**
     * Логирует действие с паролем
     * @param userId ID пользователя
     * @param passwordEntry запись пароля (может быть null для действий без привязки)
     * @param action действие (CREATE, READ, UPDATE, DELETE)
     */
    public void logAction(Long userId, PasswordEntry passwordEntry, String action) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            AuditLog auditLog = new AuditLog(user, passwordEntry, action);
            auditLogRepository.save(auditLog);

            System.out.println("✅ Logged action: " + action + " for user: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("❌ Error logging action: " + e.getMessage());
        }
    }

    /**
     * Логирует действие пользователя (без привязки к паролю)
     */
    public void logUserAction(Long userId, String action) {
        logAction(userId, null, action);
    }

    /**
     * Получить все логи пользователя
     */
    public List<AuditLog> getUserAuditLogs(Long userId) {
        return auditLogRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    /**
     * Получить логи конкретного пароля
     */
    public List<AuditLog> getPasswordAuditLogs(Long passwordEntryId) {
        return auditLogRepository.findByPasswordEntryIdOrderByTimestampDesc(passwordEntryId);
    }

    /**
     * Получить последние N логов пользователя
     */
    public List<AuditLog> getUserAuditLogsLimited(Long userId, int limit) {
        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(0, limit);
        return auditLogRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
    }
}

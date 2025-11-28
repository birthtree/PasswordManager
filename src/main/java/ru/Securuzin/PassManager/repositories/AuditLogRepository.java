package ru.Securuzin.PassManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Securuzin.PassManager.model.AuditLog;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserId(Long userId);
    List<AuditLog> findByUserIdOrderByTimestampDesc(Long userId);

    List<AuditLog> findByPasswordEntryIdOrderByTimestampDesc(Long passwordEntryId);

    // Получить последние N логов пользователя
    List<AuditLog> findByUserIdOrderByTimestampDesc(Long userId, org.springframework.data.domain.Pageable pageable);
}

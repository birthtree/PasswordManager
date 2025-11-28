package ru.Securuzin.PassManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.Securuzin.PassManager.model.PasswordEntry;

import java.util.List;
import java.util.Optional;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {
    List<PasswordEntry> findByUserId(Long userId);
    List<PasswordEntry> findByUserIdAndCategory(Long userId, String category);
    Optional<PasswordEntry> findByIdAndUserId(Long id, Long userId);
    // ← НОВЫЙ МЕТОД для поиска
    List<PasswordEntry> findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndUrlContainingIgnoreCaseOrUserIdAndCategoryContainingIgnoreCase(
            Long userId1, String title,
            Long userId2, String url,
            Long userId3, String category
    );
    @Query("SELECT p FROM PasswordEntry p WHERE p.user.id = :userId AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.url) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<PasswordEntry> searchPasswords(@Param("userId") Long userId, @Param("query") String query);

}

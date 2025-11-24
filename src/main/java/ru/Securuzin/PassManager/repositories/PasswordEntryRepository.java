package ru.Securuzin.PassManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Securuzin.PassManager.model.PasswordEntry;

import java.util.List;
import java.util.Optional;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {
    List<PasswordEntry> findByUserId(Long userId);
    List<PasswordEntry> findByUserIdAndCategory(Long userId, String category);
    Optional<PasswordEntry> findByIdAndUserId(Long id, Long userId);

}

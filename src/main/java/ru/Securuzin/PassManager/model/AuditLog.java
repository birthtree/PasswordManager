package ru.Securuzin.PassManager.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "password_entry_id")
    private PasswordEntry passwordEntry;

    @Column(nullable = false, length = 50)
    private String action;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public AuditLog(User user, PasswordEntry passwordEntry, String action) {
        this.user = user;
        this.passwordEntry = passwordEntry;
        this.action = action;
    }
}

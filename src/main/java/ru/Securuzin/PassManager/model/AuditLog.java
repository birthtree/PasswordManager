package ru.Securuzin.PassManager.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "password_entry_id")
    @JsonBackReference
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

    public AuditLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PasswordEntry getPasswordEntry() {
        return passwordEntry;
    }

    public void setPasswordEntry(PasswordEntry passwordEntry) {
        this.passwordEntry = passwordEntry;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

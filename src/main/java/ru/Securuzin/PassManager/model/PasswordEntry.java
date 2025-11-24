package ru.Securuzin.PassManager.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "password_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // явно указываем колонку
    private User user;


    private String url;

    private String title;

    @Column(name = "encrypted_password")  // ← должно совпадать с БД
    private String encryptedPassword;

    private String category;

    private String notes;
    @Column(name = "last_breach_check")
    private LocalDateTime lastBreachCheck;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String username;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @OneToMany(mappedBy = "passwordEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditLog> auditLogs = new ArrayList<>();


    public PasswordEntry() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastBreachCheck() {
        return lastBreachCheck;
    }

    public void setLastBreachCheck(LocalDateTime lastBreachCheck) {
        this.lastBreachCheck = lastBreachCheck;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

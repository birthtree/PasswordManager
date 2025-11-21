package ru.Securuzin.PassManager.dto.Password;

import java.time.LocalDateTime;

public class PasswordEntryResponse {
    private final Long id;
    private final String title;
    private final String url;
    private final String username;
    private final String password;
    private final String category;
    private final String notes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private PasswordEntryResponse(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.url = builder.url;
        this.username = builder.username;
        this.password = builder.password;
        this.category = builder.category;
        this.notes = builder.notes;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }


    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCategory() { return category; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static class Builder {
        private Long id;
        private String title;
        private String url;
        private String username;
        private String password;
        private String category;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PasswordEntryResponse build() {
            return new PasswordEntryResponse(this);
        }
    }
}

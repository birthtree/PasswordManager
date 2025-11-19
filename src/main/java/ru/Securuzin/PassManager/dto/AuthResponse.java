package ru.Securuzin.PassManager.dto;


import lombok.*;

@Data
@Getter
@Setter
public class AuthResponse {
    private Long userId;
    private String username;
    private String email;
    private String token;
    private String message;

    public AuthResponse(Long userId, String username, String email, String token, String message) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.token = token;
        this.message = message;
    }

    public AuthResponse() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

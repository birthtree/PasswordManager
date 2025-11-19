package ru.Securuzin.PassManager.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String email;
    private String masterPassword;
    private String masterPasswordConfirm;

    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMasterPasswordConfirm() {
        return masterPasswordConfirm;
    }

    public void setMasterPasswordConfirm(String masterPasswordConfirm) {
        this.masterPasswordConfirm = masterPasswordConfirm;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

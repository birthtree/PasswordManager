package ru.Securuzin.PassManager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.Securuzin.PassManager.model.User;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String email;
    private String masterPasswordHash;  // ← используем masterPasswordHash

    // Конструктор создаёт UserDetailsImpl из модели User
    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.masterPasswordHash = user.getMasterPasswordHash();
    }

    // Статический метод для удобного создания
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user);
    }

    // Наш кастомный метод для получения ID
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Методы из интерфейса UserDetails (обязательные):

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return masterPasswordHash;  // ← возвращаем masterPasswordHash
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Возвращаем пустой список (у нас нет ролей пока)
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // аккаунт не истёк
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // аккаунт не заблокирован
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // пароль не истёк
    }

    @Override
    public boolean isEnabled() {
        return true;  // аккаунт активен
    }
}

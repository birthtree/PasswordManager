package ru.Securuzin.PassManager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Securuzin.PassManager.dto.Password.CreatePasswordRequest;
import ru.Securuzin.PassManager.dto.Password.PasswordEntryResponse;
import ru.Securuzin.PassManager.dto.Password.UpdatePasswordRequest;
import ru.Securuzin.PassManager.model.PasswordEntry;
import ru.Securuzin.PassManager.model.User;
import ru.Securuzin.PassManager.repositories.PasswordEntryRepository;
import ru.Securuzin.PassManager.repositories.UserRepository;
import ru.Securuzin.PassManager.util.User.UserNotFound;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PasswordEntryService {
    private final PasswordEntryRepository passwordEntryRepository;
    private final EncryptionService encryptionService;
    private final UserRepository userRepository;
    @Autowired
    public PasswordEntryService(PasswordEntryRepository passwordEntryRepository,
                                EncryptionService encryptionService, UserRepository userRepository) {
        this.passwordEntryRepository = passwordEntryRepository;
        this.encryptionService = encryptionService;
        this.userRepository = userRepository;
    }

    public PasswordEntryResponse createPasswordEntry(CreatePasswordRequest request, Long userId) throws Exception {
        String password = encryptionService.encrypt(request.getPassword());
        PasswordEntry passwordEntry = new PasswordEntry();
        passwordEntry.setEncryptedPassword(password);
        passwordEntry.setTitle(request.getTitle());
        passwordEntry.setUrl(request.getUrl());
        passwordEntry.setCategory(request.getCategory());
        passwordEntry.setNotes(request.getNotes());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound("Пользователь не найден"));
        passwordEntry.setUser(user);
        passwordEntry.setUsername(request.getUsername());
        PasswordEntry saved = passwordEntryRepository.save(passwordEntry);
        return new PasswordEntryResponse.Builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .url(saved.getUrl())
                .username(saved.getUsername())
                .password(request.getPassword())  // НЕ шифрованный! Клиенту возвращаем оригинал
                .category(saved.getCategory())
                .notes(saved.getNotes())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public List<PasswordEntryResponse> getAllPasswordsByUser(Long userId) throws Exception {
        List<PasswordEntry> passwordEntries = passwordEntryRepository.findByUserId(userId);
        List<PasswordEntryResponse> passwordEntriesResponse = new ArrayList<>();
        for(PasswordEntry passwordEntry : passwordEntries) {
            String decryptedPassword = encryptionService.decrypt(passwordEntry.getEncryptedPassword());
            PasswordEntryResponse response = new PasswordEntryResponse.Builder()
                    .id(passwordEntry.getId())
                    .title(passwordEntry.getTitle())
                    .url(passwordEntry.getUrl())
                    .username(passwordEntry.getUsername())
                    .password(decryptedPassword)
                    .category(passwordEntry.getCategory())
                    .notes(passwordEntry.getNotes())
                    .createdAt(passwordEntry.getCreatedAt())
                    .updatedAt(passwordEntry.getUpdatedAt())
                    .build();
            passwordEntriesResponse.add(response);
        }
        return passwordEntriesResponse;
    }


    public PasswordEntryResponse getPasswordById(Long id, Long userId) throws Exception {
        PasswordEntry passwordEntry = passwordEntryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена или доступ запрещён"));

        String decryptedPassword = encryptionService.decrypt(passwordEntry.getEncryptedPassword());

        return new PasswordEntryResponse.Builder()
                .id(passwordEntry.getId())
                .title(passwordEntry.getTitle())
                .url(passwordEntry.getUrl())
                .username(passwordEntry.getUsername())
                .password(decryptedPassword)
                .category(passwordEntry.getCategory())
                .notes(passwordEntry.getNotes())
                .createdAt(passwordEntry.getCreatedAt())
                .updatedAt(passwordEntry.getUpdatedAt())
                .build();
    }


    public PasswordEntryResponse updatePassword(Long id, UpdatePasswordRequest request, Long userId) throws Exception {
        PasswordEntry passwordEntry = passwordEntryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена или доступ запрещён"));

        if (request.getTitle() != null) {
            passwordEntry.setTitle(request.getTitle());
        }
        if (request.getUrl() != null) {
            passwordEntry.setUrl(request.getUrl());
        }
        if(request.getCategory() != null) {
            passwordEntry.setCategory(request.getCategory());
        }
        if(request.getNotes() != null) {
            passwordEntry.setNotes(request.getNotes());
        }
        if(request.getUsername() != null) {
            passwordEntry.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            String encryptedPassword = encryptionService.encrypt(request.getPassword());
            passwordEntry.setEncryptedPassword(encryptedPassword);
        }
        PasswordEntry saved = passwordEntryRepository.save(passwordEntry);
        String decryptedPassword = encryptionService.decrypt(saved.getEncryptedPassword());

        return new PasswordEntryResponse.Builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .url(saved.getUrl())
                .username(saved.getUsername())
                .password(decryptedPassword)
                .category(saved.getCategory())
                .notes(saved.getNotes())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public void deletePassword(Long id, Long userId) {
        PasswordEntry passwordEntry = passwordEntryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена или доступ запрещён"));
        passwordEntryRepository.delete(passwordEntry);
    }


}

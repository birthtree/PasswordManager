package ru.Securuzin.PassManager.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Securuzin.PassManager.dto.Password.PasswordEntryResponse;
import ru.Securuzin.PassManager.model.PasswordEntry;
import ru.Securuzin.PassManager.repositories.PasswordEntryRepository;
import ru.Securuzin.PassManager.services.EncryptionService;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ExportService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExportService(PasswordEntryRepository passwordEntryRepository,
                         EncryptionService encryptionService) {
        this.passwordEntryRepository = passwordEntryRepository;
        this.encryptionService = encryptionService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }


    /**
     * Экспорт паролей в JSON
     */
    public String exportToJSON(Long userId) throws Exception {
        List<PasswordEntry> passwords = passwordEntryRepository.findByUserId(userId);

        List<PasswordEntryResponse> responses = passwords.stream()
                .map(entry -> {
                    try {
                        String decrypted = encryptionService.decrypt(entry.getEncryptedPassword());
                        return new PasswordEntryResponse.Builder()
                                .id(entry.getId())
                                .title(entry.getTitle())
                                .url(entry.getUrl())
                                .username(entry.getUsername())
                                .password(decrypted)
                                .category(entry.getCategory())
                                .notes(entry.getNotes())
                                .createdAt(entry.getCreatedAt())
                                .updatedAt(entry.getUpdatedAt())
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responses);
    }

    /**
     * Экспорт паролей в CSV
     */
    public String exportToCSV(Long userId) throws Exception {
        List<PasswordEntry> passwords = passwordEntryRepository.findByUserId(userId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(
                "Title", "URL", "Username", "Password", "Category", "Notes", "Created", "Updated"
        );

        CSVPrinter printer = new CSVPrinter(writer, csvFormat);

        for (PasswordEntry entry : passwords) {
            try {
                String decrypted = encryptionService.decrypt(entry.getEncryptedPassword());
                printer.printRecord(
                        entry.getTitle(),
                        entry.getUrl(),
                        entry.getUsername(),
                        decrypted,
                        entry.getCategory(),
                        entry.getNotes(),
                        entry.getCreatedAt(),
                        entry.getUpdatedAt()
                );
            } catch (Exception e) {
                System.err.println("Ошибка при экспорте: " + e.getMessage());
            }
        }

        printer.flush();
        return out.toString(StandardCharsets.UTF_8);
    }
}

package ru.Securuzin.PassManager.dto.Password;

public class CreatePasswordRequest {
    private String title;
    private String url;
    private String password;
    private String category;
    private String notes;
    private String user;

    public CreatePasswordRequest(String title, String url, String password, String category, String notes, String user) {
        this.title = title;
        this.url = url;
        this.password = password;
        this.category = category;
        this.notes = notes;
        this.user = user;
    }

    public CreatePasswordRequest() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

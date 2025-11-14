package ru.Securuzin.PassManager.util.password;

public class PasswordNotCorrect extends RuntimeException{
    public PasswordNotCorrect(String message) {
        super(message);
    }
}

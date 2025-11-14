package ru.Securuzin.PassManager.util;

public class NotValidData extends RuntimeException {
    public NotValidData(String message) {
        super(message);
    }
}

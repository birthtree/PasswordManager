package ru.Securuzin.PassManager.util;

public class jwtNotValid extends RuntimeException {
    public jwtNotValid(String message) {
        super(message);
    }
}

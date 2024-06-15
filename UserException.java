package estateSystem;

import java.util.HashMap;
import java.util.Map;

public class UserException extends Exception {
    private final int errorCode;
    private static final Map<Integer, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(0xA1, "Nazwa użytkownika jest wymagana");
        errorMessages.put(0xA2, "Nazwa użytkownika musi mieć minimum 5 znaków");
        errorMessages.put(0xA3, "Nazwa użytkownika nie może być dłuższa niż 25 znaków");
        errorMessages.put(0xA4, "Adres email jest wymagany");
        errorMessages.put(0xA5, "Podany adres email jest nieprawidłowy");
        errorMessages.put(0xA6, "Hasło jest wymagane");
        errorMessages.put(0xA7, "Hasło musi mieć minimum 5 znaków");
        errorMessages.put(0xA8, "Hasło nie może być dłuższe niż 25 znaków");
        errorMessages.put(0xA9, "Nieznany błąd - skontaktuj się z Administratorem (0xA9)");
        errorMessages.put(0xA10, "Błąd przy zapisie bazy użytkowników, skontaktuj się z Administratorem (0xA10)");
        errorMessages.put(0xA11, "Użytkownik o takiej nazwie już istnieje");
        errorMessages.put(0xA12, "Podany adres email jest już zajęty");
        errorMessages.put(0xB1, "Nie znaleziono użytkownika z taką nazwą");
        errorMessages.put(0xB2, "Podano nieprawidłowe hasło dla tego użytkownika");
        errorMessages.put(0xB3, "Nieznany błąd - skontaktuj się z Administratorem (0xB3)");
    }

    public UserException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return getErrorMessage(this.errorCode);
    }

    private String getErrorMessage(int errorCode) {
        return errorMessages.getOrDefault(errorCode, "Nieznany błąd");
    }
}

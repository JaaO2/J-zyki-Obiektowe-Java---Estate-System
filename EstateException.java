package estateSystem;

import java.util.HashMap;
import java.util.Map;

public class EstateException extends Exception {
    private final int errorCode;
    private static final Map<Integer, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(0xE1, "Nazwa nieruchomości jest wymagana");
        errorMessages.put(0xE2, "Nazwa nieruchomości musi mieć przynajmniej 5 znaków");
        errorMessages.put(0xE3, "Nazwa nieruchomości nie może być dłuższa niż 45 znaków");
        errorMessages.put(0xE4, "Cena nieruchomości nie może być mniejsza niż 1500 PLN");
        errorMessages.put(0xE5, "Liczba pokoi nie może być mniejsza niż 1");
        errorMessages.put(0xE6, "Powierzchnia nieruchomości nie może być mniejsza niż 25 m2");
        errorMessages.put(0xE7, "Liczba mieszkań w domu wielorodzinnym nie może być mniejsza niż 1");
        errorMessages.put(0xEF1, "Nie można zapisać do pliku");
        errorMessages.put(0xEF2, "Nie można pobrać z pliku, sprawdź czy plik istnieje");
    }

    public EstateException(int errorCode) {
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
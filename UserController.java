package estateSystem;
import java.util.Scanner;

public class UserController {

    public static String[] registerForm () {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nazwe uzytkownika: ");
        String username = scanner.next();
        System.out.println("Podaj haslo: ");
        String password = scanner.next();
        System.out.println("Podaj adres email: ");
        String email = scanner.next();

        return new String[] { username, email, password };
    }

    public static void register () {
        String[] data = registerForm();
        try {
            User user = new User(User.getNextID(), data[0], data[1], 1, data[2], true);
        } catch (UserException e) {
            System.out.println(e.getMessage());
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }
    }

    public static void createAdmin () {
        System.out.println("W systemie nie znaleziono użytkownika administracyjnego, zarejestruj go!");
        String [] data = registerForm();

        try {
            User user = new User(User.getNextID(), data[0], data[1], 2, data[2], true);
        } catch (UserException e) {
            System.out.println(e.getMessage());
            createAdmin();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }
    }

    public static void isAdmin () {
        if (User.getAll() == null || User.getAll().length < 1) {
            UserController.createAdmin();
        }
    }

    public static void login () {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Podaj nazwe uzytkownika: ");
    String username = scanner.next();
    System.out.println("Podaj haslo: ");
    String password = scanner.next();

    try {
        User user = User.find(username);

        if (user == null) {
            throw new UserException(0xB1);
        }

        if (user.checkPassword(password)) {
            Session.createNew(user);
            System.out.println("Poprawnie zalogowano jako: " + username);
        }

    } catch (UserException e) {
        System.out.println(e.getMessage());
    }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }

    }

    public static void logout () {
        Session.clear();
        System.out.println("Poprawnie wylogowano użytkownika");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }
    }


}

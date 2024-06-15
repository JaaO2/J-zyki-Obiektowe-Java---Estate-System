package estateSystem;

import java.io.FileNotFoundException;
import java.lang.*;

public class Main {

	public static void main(String[] args) throws UserException {

		menuItem[] menuItems = {
				new menuItem(1, "Zaloguj się", "login", 0),
				new menuItem(2, "Przeglądaj nieruchomości", "showEstates", 1),
				new menuItem(3, "Dodaj nieruchomość", "addEstate", 1),
				new menuItem(4, "Zapisz plik", "save", 1),
				new menuItem(5, "Zapisz plik jako", "saveAsFile", 1),
				new menuItem(6, "Utwórz nowy plik", "createFile", 1),
				new menuItem(7, "Otwórz plik", "openFile", 1),
				new menuItem(8, "Dodaj nowego użytkownika", "register", 2),
				new menuItem(9, "Wyloguj się", "logout", 1),
				new menuItem(10, "Zamknij program", "exit", 1)
		};

			try {
				User.getUsersFromFile();
				UserController.isAdmin();
			} catch (FileNotFoundException e) {
				UserController.isAdmin();
			} finally {
				while (1==1) {
					Session session = Session.getCurrent();
					System.out.println("Witaj, " + session.getUser().getUsername() + "!");

					if (EstateController.getFilename() != null) {
						System.out.println("Aktualnie otwarty plik: " + EstateController.getFilename());
					}

					MenuDisplay menu = new MenuDisplay(menuItems);
					menu.render(session, true);
					menu.input();
			}
		}
	}
	

}

package estateSystem;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuDisplay {
	private final menuItem[] items;

	public MenuDisplay (menuItem[] items) {
		this.items = items;
	}
	
	public void render (Session session, boolean vertical) {
		for (menuItem item : items) {
			if ((session.getUser().getRole() >= item.getRole() && item.getRole() != 0) || (session.getUser().getRole() == 0 && item.getRole() == 0) ) {
				System.out.print(item.getItem() + ((vertical) ? "\n" : "\t"));
			}
		}
		System.out.println();
	}

	public menuItem getItemFromKey(int key) {
		for (menuItem item : items) {
			if (item.getKey() == key) {
				return item;
			}
		}
		return null;
	}

	public void input () {
		try {
			Scanner input = new Scanner(System.in);
			int choose = input.nextInt();
			menuItem item = this.getItemFromKey(choose);
			if ((item.getRole() > Session.getCurrent().getUser().getRole()) || (item.getRole() == 0 && Session.getCurrent().getUser().getRole() != 0)) {
				System.out.println("Brak uprawnień");
				return;
			}
			this.invokeFunction(item.getFunction());
		} catch (InputMismatchException | UserException e) {
			System.out.println("Wybrnano niepoprawną opcję");
			input();
		}
	}



	public void invokeFunction (String name) throws UserException {
        switch (name) {
            case "showEstates" -> EstateController.show();
            case "addEstate" -> EstateController.add();
            case "exit" -> ProgramController.exitProgram(3);
			case "register" -> UserController.register();
			case "login" -> UserController.login();
			case "logout" -> UserController.logout();
			case "next" -> EstateController.next();
			case "previous" -> EstateController.previous();
			case "sortByName" -> EstateController.sortByName();
			case "sortByCost" -> EstateController.sortByCost();
			case "searchByName" -> EstateController.searchByName();
			case "searchByCost" -> EstateController.searchByCost();
			case "searchReset" -> EstateController.searchReset();
			case "backToMenu" -> Main.main(null);
			case "delete" -> EstateController.delete();
			case "saveAsFile" -> EstateController.saveAsFile();
			case "openFile" -> EstateController.openFile();
			case "save" -> EstateController.save();
			case "createFile" -> EstateController.createFile();
			case "edit" -> EstateController.edit();
			default -> System.out.println("Wybrano niepoprawną opcję");
        }
	}
}

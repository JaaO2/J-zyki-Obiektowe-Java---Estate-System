package estateSystem;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FilenameFilter;

public class EstateController {

    private static int index = 0;
    private static Estate[] estates;
    private static String filename;
    private static boolean searchStatus = false;

    public static void add () {
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Wybierz typ nieruchomości: \n1. Mieszanie \t 2. Chatka \t 3. Dom wielorodzinny");
            int estateType = scan.nextInt();
            scan.nextLine();
            System.out.println("Podaj nazwę nieruchomości: ");
            String[] name = scan.nextLine().split(" ");
            System.out.println("Podaj cenę nieruchomości: ");
            float cost = scan.nextFloat();
            System.out.println("Podaj liczbę pokoi w nieruchomości (lub pojedynczym mieszkaniu): ");
            int roomCount = scan.nextInt();
            System.out.println("Podaj powierzchnię nieruchomości (m2): ");
            float area = scan.nextFloat();

            switch (estateType) {
                case 1:
                    System.out.println("Czy mieszanie posiada balkon? \n 1. Tak \t 2. Nie");
                    boolean isBalcony = (scan.nextInt() == 1);
                    System.out.println("Czy mieszkanie posiada parking? \n 1. Tak \t 2. Nie");
                    boolean isParking = (scan.nextInt() == 1);
                    System.out.println("Czy mieszankie posiada piwnicę? \n 1. Tak \t 2. Nie");
                    boolean isCellar = (scan.nextInt() == 1);
                    try {
                        Flat flat = new Flat(name, cost, roomCount, area, isBalcony, isParking, isCellar);
                    } catch (EstateException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Czy chatka posiada ogród? \n 1. Tak \t 2. Nie");
                    boolean isGarden = (scan.nextInt() == 1);
                    System.out.println("Czy chatka posiada kominek? \n 1. Tak \t 2. Nie");
                    boolean isFireplace = (scan.nextInt() == 1);

                    try {
                        Cottage cottage = new Cottage(name, cost, roomCount, area, isGarden, isFireplace);
                    } catch (EstateException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Ile mieszkań posiada nieruchomość?: ");
                    int flatCount = scan.nextInt();
                    System.out.println("Czy przy nieruchomości znajduje się parking? \n 1. Tak \t 2. Nie");
                    boolean isParkingMultiFamily = (scan.nextInt() == 1);
                    System.out.println("Czy przy nieruchomości znajduje się garaż? \n 1. Tak \t 2. Nie");
                    boolean isGarage = (scan.nextInt() == 1);

                    try {
                        MultiFamily multifamily = new MultiFamily(name, cost, roomCount, area, flatCount, isParkingMultiFamily, isGarage);
                    } catch (EstateException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Wybrano nieprawidłowy typ nieruchomości");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Wprowadzono nieprawidłowy typ danej (tekst zamiast liczby)");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }
    }

    public static void showCurrent(Estate estate) {
        System.out.println("Nieruchomość nr: " + estate.getID());
        System.out.println(estate.getName());
        System.out.println("Agent nieruchomości: " + estate.getAgent());
        System.out.println("Cena nieruchomości: " + estate.getCost() + " PLN");
        System.out.println("Powierzchnia nieruchomości: " + estate.getArea() + " m2");
        System.out.println("Liczba pokoi (w pojedynczym mieszaniu): " + estate.getRoomCount());

        if (estate instanceof Flat flat) {
            System.out.println("Typ nieruchomości: mieszkanie");
            System.out.println("Dodatkowe udogodnienia: \n" + ((flat.getIsBalcony()) ? "Balkon" : "") + "\t" + ((flat.getIsCellar()) ? "Piwnica" : "") + " \t" + ((flat.getIsParking()) ? "Parking" : ""));
        }

        if (estate instanceof Cottage cottage) {
            System.out.println("Typ nieruchomości: chatka");
            System.out.println("Dodatkowe udogodnienia: \n" + ((cottage.getIsGarden()) ? "Ogród" : "") + "\t" + ((cottage.getIsFireplace()) ? "Kominek" : ""));
        }

        if (estate instanceof MultiFamily multiFamily) {
            System.out.println("Typ nieruchomości: dom wielorodzinny");
            System.out.println("Liczba mieszkań: " + multiFamily.getFlatCount());
            System.out.println("Dodatkowe udogodnienia: \n" + ((multiFamily.getIsParking()) ? "Parking" : "") + "\t" + ((multiFamily.getIsGarage()) ? "Garaż" : ""));
        }
    }

    public static void show () {

        menuItem[] menuItems = {
                new menuItem(1, "Następny", "next", 1),
                new menuItem(2, "Poprzedni", "previous", 1),
                new menuItem(3, "Edytuj", "edit", 1),
                new menuItem(4, "Usuń", "delete", 2),
                new menuItem(5, "Sortuj według nazwy", "sortByName", 1),
                new menuItem(6, "Sortuj według ceny", "sortByCost", 1),
                new menuItem(7, "Wyszukaj po nazwie", "searchByName", 1),
                new menuItem(8, "Wyszukaj po cenie", "searchByCost", 1),
                new menuItem(9, "Resetuj wyniki wyszukiwania", "searchReset", 1),
                new menuItem(10, "Wróć do menu", "backToMenu", 1),
        };

        while (1==1) {

            Estate[] estates = Estate.getEstates();

            if (estates == null || estates.length == 0) {
                System.out.println("Brak nieruchomości do wyświetlenia");
                return;
            }

            if (!searchStatus) {
                EstateController.estates = Arrays.copyOf(estates, estates.length);
            }

            if (EstateController.estates[EstateController.index] == null) {
                EstateController.next();
            }

            EstateController.showCurrent(EstateController.estates[EstateController.index]);
            MenuDisplay menu = new MenuDisplay(menuItems);
            menu.render(Session.getCurrent(), false);
            menu.input();
        }

    }

    public static void next () {
        if (EstateController.index + 1 < EstateController.estates.length)
            EstateController.index = EstateController.index + 1;
        else
            EstateController.index = 0;

        if (EstateController.estates[EstateController.index] == null) {
            EstateController.index = EstateController.index + 1;
        }
    }

    public static void previous () {
        if (EstateController.index > 0)
            EstateController.index = EstateController.index - 1;
        else
            EstateController.index = EstateController.estates.length - 1;

        if (EstateController.estates[EstateController.index] == null) {
            EstateController.index = EstateController.index - 1;
        }
    }

    public static void delete () {
        System.out.println("Czy na pewno chcesz usunąć ten element?: \n 1. Tak \t 2. Nie");
        Scanner scan = new Scanner(System.in);
        boolean confirm = scan.nextInt() == 1;
        if (confirm) {
            Estate estate = Estate.getById(EstateController.estates[EstateController.index].getID());
            estate.delete();
            EstateController.searchStatus = false;
            EstateController.next();
            System.out.println("Element został usunięty");

        }
    }

    public static void saveAsFile () {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwę pliku: ");
        String name = scan.next();
        try {
            Estate.saveToFile(name);
            EstateController.filename = name;
            System.out.println("Pomyślnie zapisano dane do pliku");
        } catch (EstateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getFiles () {
        String projectDirectory = System.getProperty("user.dir");
        File folder = new File(projectDirectory);

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".bin") && !name.equals("user.bin");
            }
        };

        File[] files = folder.listFiles(filter);

        if (files != null && files.length != 0) {
            System.out.println("Pliki możliwe do otwarcia: ");
            for (File file : files) {
                System.out.println("- " + file.getName());
            }
        }

    }

    public static void openFile () {
        getFiles();
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwę pliku: ");
        String name = scan.next();

        try {
            Estate.loadFromFile(name);
            EstateController.filename = name;
            System.out.println("Pomyślnie pobrano dane z pliku");
        } catch (EstateException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void save () {
        if (EstateController.filename == null) {
            EstateController.saveAsFile();
            return;
        }

        try {
            Estate.saveToFile(EstateController.filename);
            System.out.println("Pomyślnie zapisano dane do pliku");
        } catch (EstateException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createFile () {
        System.out.println("Czy na pewno chcesz utworzyć nowy plik? Niezapisane dane zostaną utracone \n1. Tak \t 2. Nie");
        Scanner scan = new Scanner(System.in);
        boolean confirm = scan.nextInt() == 1;

        if (confirm) {
            EstateController.filename = null;
            Estate.flush();
            System.out.println("Utworzono nowy plik");
        }
    }

    public static String getFilename () {
        return EstateController.filename;
    }

    public static void edit () {
        Scanner scan = new Scanner(System.in);
        Estate estate = Estate.getById(EstateController.estates[EstateController.index].getID());
        System.out.println("Pozostawienie pustej wartości nie spowoduje zmiany");
        System.out.println("Wpisz nazwę nieruchomości: \t\t" + estate.getName());
        String[] name = scan.nextLine().split(" ");
        System.out.println("Wprowadź cene nieruchomości: \t\t" + estate.getCost() + " PLN");
        String cost = scan.nextLine();
        System.out.println("Wprowadź liczbę pomieszczeń: \t\t" + estate.getRoomCount());
        String roomCount = scan.nextLine();
        System.out.println("Wprowadź powierzchnię nieruchomośći: \t\t" + estate.getArea() + " m2");
        String area = scan.nextLine();

        if (estate instanceof Flat flat) {
            System.out.println("Czy mieszkanie posiada balkon? \n 1. Tak \t 2. Nie \t\t" + (flat.getIsBalcony() ? "Tak" : "Nie"));
            String isBalcony = scan.nextLine();
            System.out.println("Czy mieszkanie posiada piwnicę?\n1. Tak \t 2. Nie \t\t" + ((flat.getIsCellar() ? "Tak" : "Nie")));
            String isCellar = scan.nextLine();
            System.out.println("Czy mieszkanie posiada parking? \n 1. Tak \t 2. Nie \t\t" + ((flat.getIsParking() ? "Tak" : "Nie")));
            String isParking = scan.nextLine();
            try {
                flat.setData(name, cost, roomCount, area, isBalcony, isParking, isCellar);
            } catch (EstateException e) {
                System.out.println(e.getMessage());
            }
        }

        if (estate instanceof Cottage cottage) {
            System.out.println("Czy chatka posiada ogródek? \n 1. Tak \t 2. Nie \t\t" + ((cottage.getIsGarden() ? "Tak" : "Nie")));
            String isGarden = scan.nextLine();
            System.out.println("Czy chatka posiada kominek? \n 1. Tak \t 2. Nie \t\t" + ((cottage.getIsFireplace() ? "Tak" : "Nie")));
            String isFireplace = scan.nextLine();

            try {
                cottage.setData(name, cost, roomCount, area, isGarden, isFireplace);
            } catch (EstateException e) {
                System.out.println(e.getMessage());
            }
        }

        if (estate instanceof MultiFamily multiFamily) {
            System.out.println("Ile mieszkań jest w budynku?: \t\t" + multiFamily.getFlatCount());
            String flatCount = scan.nextLine();
            System.out.println("Czy przy budynku znajduje się parking?: \n 1. Tak \t 2. Nie \t\t" + (multiFamily.getIsParking() ? "Tak" : "Nie"));
            String isParking = scan.nextLine();
            System.out.println("Czy przy budynku znajduje się garaż?: \n 1. Tak \t 2. Nie \t\t" + (multiFamily.getIsGarage() ? "Tak" : "Nie"));
            String isGarage = scan.nextLine();

            try {
                multiFamily.setData(name, cost, roomCount, area, flatCount, isParking, isGarage);
            } catch (EstateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void sortByName () {
        Scanner scan = new Scanner(System.in);
        System.out.println("Sortowanie według nazwy \n 1. Rosnąco \t 2. Malejąco");
        int sortMethod = scan.nextInt();
        Estate.sortByName(sortMethod == 1);
    }

    public static void sortByCost () {
        Scanner scan = new Scanner(System.in);
        System.out.println("Sortowanie według ceny \n 1. Rosnąco \t 2. Malejąco");
        int sortMethod = scan.nextInt();
        Estate.sortByCost(sortMethod == 1);
    }

    public static void searchByName () {
        Scanner scan = new Scanner(System.in);
        System.out.println("Wpisz szukaną nazwę: ");
        String[] searchName = scan.nextLine().split(" ");
        Estate[] searchEstates = Estate.searchByName(searchName);
        if (searchEstates == null || searchEstates.length == 0) {
            System.out.println("Nie znaleziono wyników");
            return;
        }
        EstateController.estates = searchEstates;
        EstateController.searchStatus = true;
        EstateController.index = 0;
    }

    public static void searchByCost () {
        Scanner scan = new Scanner(System.in);
        System.out.println("Wprowadź cene minimalną: ");
        Float minCost = scan.nextFloat();
        System.out.println("Wprowadź cenę maksymalną: ");
        Float maxCost = scan.nextFloat();
        Estate[] searchEstates = Estate.searchByCost(minCost, maxCost);
        if (searchEstates == null || searchEstates.length == 0) {
            System.out.println("Nie znaleziono wyników");
            return;
        }
        EstateController.estates = searchEstates;
        EstateController.searchStatus = true;
        EstateController.index = 0;
    }

    public static void searchReset () {
        EstateController.searchStatus = false;
    }
}

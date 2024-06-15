package estateSystem;

import java.util.Scanner;

public class ProgramController {
    public static void exitProgram (int confirmation) {
        if (confirmation == 3) {
            System.out.println("Czy na pewno chcesz wyjść z programu?\n1.Tak\t2.Nie");
            Scanner input = new Scanner(System.in);
            int choose = input.nextInt();
            exitProgram(choose);
        } else if (confirmation == 1) {
            System.exit(0);
        }
    }

}

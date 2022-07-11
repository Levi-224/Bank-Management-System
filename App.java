import java.util.Scanner;

public class App {

  static Error error = new Error();
  Admin admin = new Admin();
  static Customer customer = new Customer();

  public static void main(String[] args) throws Exception {
    System.out.println(
      "\033[1;95m" + "[=============================]" + "\u001B[0m"
    );
    System.out.println(
      "\033[1;95m" +
      "[" +
      "\033[1;96m" +
      "         BANK OF ABC        " +
      "\033[1;95m" +
      " ]" +
      "\u001B[0m"
    );
    System.out.println(
      "\033[1;95m" + "[=============================]" + "\u001B[0m"
    );
    System.out.println(
      "\033[1;92m" + "              MENU             " + "\u001B[0m"
    );

    menu();
  }

  public static void menu() {
    System.out.println(
      "\u001B[33m" + "[=============================]" + "\u001B[0m"
    );
    System.out.println(
      "\u001B[33m" +
      "[" +
      "\033[1;96m" +
      "1) Admin 2) Customer 3) Exit" +
      "\u001B[33m" +
      " ]" +
      "\u001B[0m"
    );
    System.out.println(
      "\u001B[33m" + "[=============================]" + "\u001B[0m"
    );
    try (Scanner sc = new Scanner(System.in)) {
      if (sc.hasNextInt()) {
        int a = sc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        switch (a) {
          case 1:
            Admin.admin();
            break;
          case 2:
            Customer.customer();
            break;
          case 3:
            error.error("Exiting Program");
            System.exit(0);
            break;
          default:
            error.error("Wrong Input");
            menu();
            break;
        }
      } else {
        error.error("Invalid Input Use A Number [1-3]");
      }
    }
  }
}

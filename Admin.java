import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Admin {

  static Error error = new Error();
  static boolean err = false;

  static void admin() {
    System.out.println("\n\033[1;95m" + "ADMIN LOGIN" + "\u001B[0m");
    System.out.println("\033[1;93m" + "=============" + "\u001B[0m");
    try (Scanner dataSc = new Scanner(System.in)) {
      String name = "";
      int pin = 0;
      String pin_con = "";
      System.out.println("Enter Your Name");
      if (dataSc.hasNextLine()) {
        name = dataSc.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
      } else {
        error.error("Invalid Input Name must be a string");
        App.menu();
      }
      System.out.println("Enter Your PIN");
      if (dataSc.hasNextInt()) {
        pin = dataSc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        pin_con = Integer.toString(pin);
      } else {
        error.error("Invalid Input PIN must be a number of length < 9");
        App.menu();
      }

      ArrayList<String> read = Customer.readFile();

      for (int i = 0; i < read.size(); i++) {
        List<String> details = Arrays.asList(read.get(i).split("\\s*,\\s*"));
        String readName = details.get(0);
        String readPin = details.get(2);
        Boolean readAdmin = Boolean.parseBoolean(details.get(7));
        if ((name.toLowerCase() + " " + pin_con).equals(readName + " " + readPin) &&
            readAdmin == true) {
          Customer adm = new Customer();
          adm.name = readName.toLowerCase();
          adm.gender = details.get(1).toLowerCase();
          adm.address = details.get(5);
          adm.pin = Integer.parseInt(details.get(2));
          adm.email = details.get(3);
          adm.phone = details.get(4);
          adm.bal = Float.parseFloat(details.get(6));
          adm.admin = Boolean.parseBoolean(details.get(7));
          adminLoggedIn(adm);
          return;
        }
      }
      error.error("User Not An Admin");
      App.menu();
    }
  }

  public static void adminLoggedIn(Customer c) {
    System.out.println(
        "\n\u001B[33m" +
            "[==============================================================]" +
            "\u001B[0m");
    System.out.println(
        "\u001B[33m" +
            "[" +
            "\u001B[0m" +
            "1)All Accounts 2)Add Admin 3)Remove Admin 4)Remove User 5)Back" +
            "\u001B[33m" +
            "]" +
            "\u001B[0m");
    System.out.println(
        "\u001B[33m" +
            "[==============================================================]" +
            "\u001B[0m");
    try (Scanner sc = new Scanner(System.in)) {
        if (sc.hasNextInt()) {
          int a = sc.nextInt();
          System.out.print("\033[H\033[2J");
          System.out.flush();
          switch (a) {
            case 1:
              showUsers(c);
              adminLoggedIn(c);
              sc.close();
              break;
            case 2:
              addAdmin(c);
              sc.close();
              break;
            case 3:
              removeAdmin(c);
              sc.close();
              break;
            case 4:
              removeUser(c);
              sc.close();
              break;
            case 5:
              App.menu();
              sc.close();
              break;
            default:
              error.error("Wrong Input");
              sc.close();
              break;
          }
        } else {
          error.error("Invalid Input Use A Number [1-5]");
          adminLoggedIn(c);
        }
    }
  }

  static void showUsers(Customer c) {
    System.out.println("\nHere Are the list of all registered accounts\n");
    ArrayList<String> read = Customer.readFile();
    String[][] arrays = new String[read.size() + 1][];
    for (int n = 1; n < (read.size() + 1); n++) {
      List<String> details = Arrays.asList(read.get(n - 1).split("\\s*,\\s*"));
      String index = Integer.toString(n);
      String readName = details.get(0);
      String readPhone = details.get(4);
      String readBal = details.get(6);
      String readAdm = details.get(7);
      String[] header = { "index", "Name", "PhoneNo", "Balance", "Admin" };
      String[] data = { index, readName, readPhone, readBal, readAdm };
      arrays[0] = header;
      arrays[n] = data;
    }
    Table.table(arrays);
  }

  static void addAdmin(Customer c) {
    showUsers(c);
    try (Scanner addSc = new Scanner(System.in)) {
      ArrayList<String> read = Customer.readFile();
      System.out.println("\nChoose which user to make admin");
      if (addSc.hasNextInt()) {
        int add = addSc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if (add > 0 && add <= (read.size())) {
          int ind = (add - 1);
          List<String> addData = Arrays.asList(read.get(ind).split("\\s*,\\s*"));
          ArrayList<String> newWrite = new ArrayList<>();
          for (int n = 0; n <= (read.size() - 1); n++) {
            List<String> details = Arrays.asList(read.get(n).split("\\s*,\\s*"));
            String readName = addData.get(0);
            String readPin = addData.get(2);
            if (addData.get(7).equals("true")) {
              error.error("The selected User is already an Admin");
              adminLoggedIn(c);
              break;
            } else if ((details.get(0) + " " + details.get(2)).equals(
                readName + " " + readPin)) {
              String newData = String.format(
                  "%s,%s,%s,%s,%s,%s,%s,%s",
                  addData.get(0),
                  addData.get(1),
                  addData.get(2),
                  addData.get(3),
                  addData.get(4),
                  addData.get(5),
                  addData.get(6),
                  "true");
              newWrite.add(newData);
            } else {
              newWrite.add(
                  String.format(
                      "%s,%s,%s,%s,%s,%s,%s,%s",
                      details.get(0),
                      details.get(1),
                      details.get(2),
                      details.get(3),
                      details.get(4),
                      details.get(5),
                      details.get(6),
                      details.get(7)));
            }
          }
          Customer.writeFile(newWrite);
          System.out.println("\n\033[1;95m" + "Added New Admin" + "\u001B[0m");
          adminLoggedIn(c);
        } else {
          error.error("Please enter a proper index");
          adminLoggedIn(c);
        }
        addSc.close();
      } else {
        error.error("Please enter a proper index");
        adminLoggedIn(c);
      }
    }
  }

  static void removeAdmin(Customer c) {
    System.out.println("\nHere Are the list of all registered accounts\n");
    ArrayList<String> read = Customer.readFile();
    showUsers(c);
    try (Scanner remAdmSc = new Scanner(System.in)) {
      System.out.println("\nChoose which user to remove admin");
      if (remAdmSc.hasNextInt()) {
        int add = remAdmSc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if (add > 0 && add <= (read.size())) {
          int ind = (add - 1);
          List<String> addData = Arrays.asList(read.get(ind).split("\\s*,\\s*"));
          ArrayList<String> newWrite = new ArrayList<>();
          for (int n = 0; n <= (read.size() - 1); n++) {
            List<String> details = Arrays.asList(read.get(n).split("\\s*,\\s*"));
            String readName = addData.get(0);
            String readPin = addData.get(2);
            if ((addData.get(0) + " " + addData.get(2)).equals(
                (c.name) + " " + (Integer.toString(c.pin)))) {
              error.error("You Cant Remove Your Admin Role");

              adminLoggedIn(c);
              break;
            } else if (addData.get(7).equals("false")) {
              error.error("The selected User is not an Admin");
              adminLoggedIn(c);
              break;
            } else if ((details.get(0) + " " + details.get(2)).equals(
                readName + " " + readPin)) {
              String newData = String.format(
                  "%s,%s,%s,%s,%s,%s,%s,%s",
                  addData.get(0),
                  addData.get(1),
                  addData.get(2),
                  addData.get(3),
                  addData.get(4),
                  addData.get(5),
                  addData.get(6),
                  "false");
              newWrite.add(newData);
            } else {
              newWrite.add(
                  String.format(
                      "%s,%s,%s,%s,%s,%s,%s,%s",
                      details.get(0),
                      details.get(1),
                      details.get(2),
                      details.get(3),
                      details.get(4),
                      details.get(5),
                      details.get(6),
                      details.get(7)));
            }
          }
          Customer.writeFile(newWrite);
          System.out.println("\n\033[1;95m" + "Removed Admin" + "\u001B[0m");
          adminLoggedIn(c);
        } else {
          error.error("Please enter a proper index");
          adminLoggedIn(c);
        }
      } else {
        error.error("Please enter a proper index");
        adminLoggedIn(c);
      }
    }
  }

  static void removeUser(Customer c) {
    System.out.println("\nHere Are the list of all registered accounts\n");
    ArrayList<String> read = Customer.readFile();
    showUsers(c);
    try (Scanner remSc = new Scanner(System.in)) {
      System.out.println("\nChoose which user to remove");
      if (remSc.hasNextInt()) {
        int rem = remSc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if (rem > 0 && rem <= (read.size())) {
          int ind = (rem - 1);
          List<String> addData = Arrays.asList(read.get(ind).split("\\s*,\\s*"));
          ArrayList<String> newWrite = new ArrayList<>();
          for (int n = 0; n <= (read.size() - 1); n++) {
            List<String> details = Arrays.asList(read.get(n).split("\\s*,\\s*"));
            String readName = addData.get(0);
            String readPin = addData.get(2);
            if ((addData.get(0) + " " + addData.get(2)).equals(
                (c.name) + " " + (Integer.toString(c.pin)))) {
              error.error("You Cant Remove Yourself");
              adminLoggedIn(c);
              break;
            } else if (!(details.get(0) + " " + details.get(2)).equals(
                readName + " " + readPin)) {
              newWrite.add(
                  String.format(
                      "%s,%s,%s,%s,%s,%s,%s,%s",
                      details.get(0),
                      details.get(1),
                      details.get(2),
                      details.get(3),
                      details.get(4),
                      details.get(5),
                      details.get(6),
                      details.get(7)));
            } else {
              continue;
            }
          }
          Customer.writeFile(newWrite);
          System.out.println("\n\033[1;95m" + "Removed User" + "\u001B[0m");
          adminLoggedIn(c);
        } else {
          error.error("Please enter a proper index");
          adminLoggedIn(c);
        }
      } else {
        error.error("Please enter a proper index");
        adminLoggedIn(c);
      }
    }
  }
}

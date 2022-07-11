import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Customer implements java.io.Serializable {

  public String name;
  public String gender;
  public int pin;
  public String email;
  public String phone;
  public String address;
  public Float bal;
  public Boolean admin;

  static App app = new App();
  static Error error = new Error();

  static void customer() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    System.out.println(
        "\n\u001B[33m" + "[=============================]" + "\u001B[0m");
    System.out.println(
        "\u001B[33m" +
            "[" +
            "\033[1;96m" +
            "1) Register 2) Login 3) Back" +
            "\u001B[33m" +
            " ]" +
            "\u001B[0m");
    System.out.println(
        "\u001B[33m" + "[=============================]" + "\u001B[0m");
    try (Scanner sc = new Scanner(System.in)) {
      if (sc.hasNextInt()) {
        int a = sc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        switch (a) {
          case 1:
            register();
            break;
          case 2:
            login();
            break;
          case 3:
            System.out.println(
                "\033[4;34m" +
                    "===========[Exiting Customer Panel]===========" +
                    "\u001B[0m\n");
            App.menu();
            break;
          default:
            error.error("Wrong Input");
            customer();
            break;
        }
      } else {
        error.error("Invalid Input Use A Number [1-3]");
        customer();
      }
    }
  }

  public static boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  public static void register() {
    System.out.println("\n\033[1;95m" + "REGISTER" + "\u001B[0m");
    System.out.println("\033[1;93m" + "========" + "\u001B[0m");
    try (Scanner dataSc = new Scanner(System.in)) {
      String name = "";
      String gender = "";
      String email = "";
      String address = "";
      String phone = "";
      int pin = 0;
      System.out.println("1) Enter Your Name:");
      if (dataSc.hasNextLine()) {
        name = dataSc.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
      } else {
        error.error("Please Enter a correct name!");
        App.menu();
      }
      System.out.println("2) Enter Your Gender (male/female):");
      if (dataSc.hasNextLine()) {
        gender = dataSc.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if (!gender.equalsIgnoreCase("female") && !gender.equalsIgnoreCase("male")) {
          error.error("Please Enter a correct gender!");
          App.menu();
        }
      } else {
        error.error("Please Enter a correct gender of type string!");
        App.menu();
      }
      System.out.println("3) Enter Your Email:");
      if (dataSc.hasNextLine()) {
        email = dataSc.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if (!email.endsWith(".com")) {
          error.error("Invalid Email Provided");
          App.menu();
        }
      } else {
        error.error("Invalid Email Provided");
        App.menu();
      }
      System.out.println("4) Enter Your Address:");
      if (dataSc.hasNextLine()) {
        address = dataSc.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
      } else {
        error.error("Invalid Address Provided!");
        App.menu();
      }
      System.out.println("5) Enter Your PhoneNo:");
      if (dataSc.hasNextLine()) {
        phone = dataSc.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if ((phone.length()) != 10 && !isNumeric(phone)) {
          error.error("Phone Number Should be A Number 10 Digits Long.");
          App.menu();
        }
      } else {
        error.error("Invalid Phone Number Provided!");
        App.menu();
      }
      System.out.println("6) Enter Your Pin:");
      if (dataSc.hasNextInt()) {
        pin = dataSc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
      } else {
        error.error("Pin Should Be A Number Less Than 9 Digits");
        App.menu();
      }

      Customer c = new Customer();
      c.name = name.toLowerCase();
      c.gender = gender.toLowerCase();
      c.pin = pin;
      String pin_con = Integer.toString(c.pin);
      c.email = email;
      c.phone = phone;
      c.address = address;
      c.bal = (float) 0;
      String bal_con = Float.toString(c.bal);

      ArrayList<String> read = readFile();

      for (int i = 0; i < read.size(); i++) {
        List<String> details = Arrays.asList(read.get(i).split("\\s*,\\s*"));
        String readName = details.get(0);
        String readPin = details.get(2);
        if ((name.toLowerCase() + " " + pin_con).equals(readName + " " + readPin)) {
          error.error("User Already Exsists");
          App.menu();
          return;
        }
      }
      ArrayList<String> writeList = new ArrayList<>();
      writeList.add(c.name);
      writeList.add(c.gender);
      writeList.add(pin_con);
      writeList.add(email);
      writeList.add(phone);
      writeList.add(address);
      writeList.add(bal_con);
      writeList.add("false"); // admin
      String listString = String.join(", ", writeList);

      writeToFile(listString);

      customer();
    }
  }

  static ArrayList<String> trans = new ArrayList<String>();
  static Float initBal;

  public static void login() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    System.out.println("\n\033[1;95m" + "LOGIN" + "\u001B[0m");
    System.out.println("\033[1;93m" + "=====" + "\u001B[0m");
    String name = "";
    int pin = 0;
    String pin_con = "";
    try (Scanner dataSc = new Scanner(System.in)) {
      System.out.println("Enter Your Name");
      if (dataSc.hasNextLine()) {
        name = dataSc.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
      } else {
        error.error("Name Should Be A String");
        login();
      }
      System.out.println("Enter Your PIN");
      if (dataSc.hasNextInt()) {
        pin = dataSc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        pin_con = Integer.toString(pin);
      } else {
        error.error("Pin Should Be A Number Less Than 9 Digits");
        login();
      }

      ArrayList<String> read = readFile();

      for (int i = 0; i < read.size(); i++) {
        List<String> details = Arrays.asList(read.get(i).split("\\s*,\\s*"));
        String readName = details.get(0).toLowerCase();
        String readPin = details.get(2);
        if ((name.toLowerCase() + " " + pin_con).equals(readName + " " + readPin)) {
          Customer cus = new Customer();
          cus.name = readName;
          cus.gender = details.get(1);
          cus.address = details.get(5);
          cus.pin = Integer.parseInt(details.get(2));
          cus.email = details.get(3);
          cus.phone = details.get(4);
          cus.bal = Float.parseFloat(details.get(6));
          cus.admin = Boolean.parseBoolean(details.get(7));

          String[][] pfp = {
              {
                  "\033[1;96m" + "     ,       " + "\033[0m",
                  "\033[1;96m" + "    /(...    " + "\033[0m",
                  "\033[1;96m" + "   / ,-(_`   " + "\033[0m",
                  "\033[1;96m" + "   [ )_  _;  " + "\033[0m",
                  "\033[1;96m" + "   ([[_][_]) " + "\033[0m",
                  "\033[1;96m" + "    |  L  |  " + "\033[0m",
                  "\033[1;96m" + "    | '__/   " + "\033[0m",
              },
              {
                  "\033[1;92m" + " ,-`=-.      " + "\033[0m",
                  "\033[1;92m" + " .       }   " + "\033[0m",
                  "\033[1;92m" + " `='`='   '  " + "\033[0m",
                  "\033[1;92m" + " `@] @'|   ) " + "\033[0m",
                  "\033[1;92m" + "  ; ` ' ),-` " + "\033[0m",
                  "\033[1;92m" + "   '^_, ],   " + "\033[0m",
                  "\033[1;92m" + "   ,([,/ )`-." + "\033[0m",
              },
          };

          int gen = 0;
          if (cus.gender.equalsIgnoreCase("FEMALE")) {
            gen = 1;
          }

          System.out.println("\n[=============]");
          System.out.println("]" + pfp[gen][0] + "]");
          System.out.println("[" + pfp[gen][1] + "[" + " name   :" + cus.name);
          System.out.println(
              "]" + pfp[gen][2] + "]" + " gender :" + cus.gender);
          System.out.println("[" + pfp[gen][3] + "[" + " email  :" + cus.email);
          System.out.println("]" + pfp[gen][4] + "]" + " phone  :" + cus.phone);
          System.out.println(
              "[" + pfp[gen][5] + "[" + " balance:" + cus.bal + "$");
          System.out.println("]" + pfp[gen][6] + "]");
          System.out.println("[=============]");

          initBal = cus.bal;

          loggedIn(cus);
          return;
        }
      }
      error.error("User not registered");
      App.menu();
    }
  }

  static void loggedIn(Customer c) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    System.out.println(
        "\u001B[33m" + "[=========================================]" + "\u001B[0m");
    System.out.println(
        "\u001B[33m" +
            "[" +
            "\u001B[0m" +
            "1) Deposit 2) Withdraw 3) Balance 4) Exit" +
            "\u001B[33m" +
            "]" +
            "\u001B[0m");
    System.out.println(
        "\u001B[33m" + "[=========================================]" + "\u001B[0m");

    try (Scanner sc = new Scanner(System.in)) {
      if (sc.hasNextInt()) {
        int a = sc.nextInt();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        switch (a) {
          case 1:
            System.out.println("Enter amount to deposit");
            try (Scanner depSc = new Scanner(System.in)) {
              if (depSc.hasNextFloat()) {
                Float amt = depSc.nextFloat();
                c.bal += amt;
                trans.add(
                    String.format("Deposited: %f  - %s", amt, dtf.format(now)));
                System.out.println(String.format("Deposited %f\n", amt));
                loggedIn(c);
              } else {
                error.error("The Amount To Deposit Must Be Valid");
                loggedIn(c);
              }
            }
            break;
          case 2:
            System.out.println("Enter amount to withdraw");
            try (Scanner wthSc = new Scanner(System.in)) {
              if (wthSc.hasNextFloat()) {
                Float amt = wthSc.nextFloat();
                if ((c.bal - amt) < 0) {
                  error.error("You dont have enough money to withdraw");
                  loggedIn(c);
                } else {
                  c.bal -= amt;
                  trans.add(
                      String.format("Withdrawn: %f  - %s", amt, dtf.format(now)));
                  System.out.println(String.format("Withdrawn %f $\n", amt));
                  loggedIn(c);
                }
              } else {
                error.error("The Amount To Withdraw Must Be Valid");
                loggedIn(c);
              }
            }
            break;
          case 3:
            System.out.println(String.format("You have %f\n", c.bal));
            loggedIn(c);
            break;
          case 4:
            System.out.println(
                "\u001B[33m" +
                    "[==================================================]" +
                    "\u001B[0m");
            System.out.println(
                "\u001B[33m" +
                    "[" +
                    "\033[0;92m" +
                    "              GENERATING TRANSCRIPT               \u001B[33m" +
                    "]" +
                    "\u001B[0m");
            System.out.println(
                "\u001B[33m" +
                    "[=================================================]" +
                    "\u001B[0m");
            Thread.sleep(1000);
            System.out.println(
                "\u001B[33m" +
                    "[" +
                    "\033[0;92m" +
                    "                Start Balance:" +
                    initBal +
                    "              \u001B[33m" +
                    "]" +
                    "\u001B[0m");
            System.out.println(
                "\u001B[33m" +
                    "[==================================================]" +
                    "\u001B[0m");
            for (String tr : trans) {
              Thread.sleep(1000);
              System.out.println(
                  "\u001B[33m" +
                      "[   " +
                      "\033[0;92m" +
                      tr +
                      "\u001B[33m" +
                      "   ]" +
                      "\u001B[0m");
            }
            Thread.sleep(1000);
            System.out.println(
                "\u001B[33m" +
                    "[" +
                    "\033[0;92m" +
                    "              Current Balance:" +
                    c.bal +
                    "              \u001B[33m" +
                    "]" +
                    "\u001B[0m");
            System.out.println(
                "\u001B[33m" +
                    "[==================================================]" +
                    "\u001B[0m");
            Thread.sleep(2000);
            System.out.println(
                "\033[4;34m" +
                    "===========[Exiting Program]===========" +
                    "\u001B[0m\n");

            ArrayList<String> read = readFile();
            ArrayList<String> newWrite = new ArrayList<>();
            for (int n = 0; n < read.size(); n++) {
              List<String> details = Arrays.asList(
                  read.get(n).split("\\s*,\\s*"));
              String readName = details.get(0);
              String readPin = details.get(2);
              if ((c.name + " " + Integer.toString(c.pin)).equals(
                  readName + " " + readPin)) {
                String newData = String.format(
                    "%s,%s,%s,%s,%s,%s,%s,%s",
                    c.name,
                    c.gender,
                    readPin,
                    details.get(3),
                    details.get(4),
                    details.get(5),
                    String.format(java.util.Locale.US, "%.2f", c.bal),
                    details.get(7));
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
            writeFile(newWrite);
            System.exit(0);
            break;
          default:
            error.error("Wrong Input");
            loggedIn(c);
            break;
        }
      } else {
        error.error("Invalid Input");
        loggedIn(c);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<String> readFile() {
    ArrayList<String> Lines = new ArrayList<>();
    try {
      FileInputStream fis = new FileInputStream("data/user.txt");
      Scanner sc = new Scanner(fis);
      while (sc.hasNextLine()) {
        Lines.add(sc.nextLine());
      }
      sc.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Lines;
  }

  public static void writeToFile(String writeData) {
    try {
      BufferedWriter out = new BufferedWriter(
          new FileWriter("data/user.txt", true));

      out.write(String.join(",", writeData + System.lineSeparator()));
      out.close();
    } catch (IOException e) {
      System.out.println("exception occurred" + e);
    }
  }

  public static void writeFile(ArrayList<String> arrData) {
    try (FileWriter writer = new FileWriter("data/user.txt")) {
      int size = arrData.size();
      for (int i = 0; i < size; i++) {
        String str = arrData.get(i).toString();
        writer.write(str);
        if (i <= size) {
          writer.write("\r\n");
        }
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

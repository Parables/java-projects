import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class _202145387 {
  private static final Scanner scanner = new Scanner(System.in);
  private static final String FILENAME = "Random Numbers.txt";
  private static final Random random = new Random();
  private static final String alphanumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static Map<String, Map<String, Object>> registeredData = new HashMap<>();
  private static Map<String, Object> profile = new HashMap<>();

  public static void main(String[] args) {
    displayMenu();
  }

  /**
   * displays a menu for the user to enter his option.
   * Any invalid input will trigger the menu to display again.
   * Enter 0if you want to exit the program
   */
  private static void displayMenu() {
    try {
      String menuPrompt = "\nPlease select an option below \n\t 1. Register \n\t 2. View \n\t 0. Exit \n Enter your option: ";
      int option = -1; // anything other than -1 means the user entered an option

      do {
        if (option == -1) print(menuPrompt);
        else print("Invalid option... " + menuPrompt);
        option = scanner.nextInt();
      } while (option != 0 && option != 1 && option != 2);


      switch (option) {
        case 0:
          print("Goodbye...", true);
          break;
        case 1:
          Register();
          break;
        case 2:
          findUser("", true);
          continueSession();
          break;
        default:
          print("Invalid option", true);
      }
    } catch (Exception e) {
      print(e.getMessage());
      continueSession();
    }
  }

  /**
   * prevents the program from terminating by prompting the user if they wish to continue.
   * Default input is Yes so any letter will be treated as a Yes except all case insensitive of 'n' or 'no'
   */
  private static void continueSession() {
    try {
      print("\n\nDo you want to continue your session? (Default option: Yes) [Y/n] \t\n Enter your option: ");
      String choice = scanner.next();
      if (choice.equalsIgnoreCase("n") || choice.equalsIgnoreCase("no")) print("Goodbye");
      else displayMenu();
    } catch (Exception e) {
      print(e.getMessage());
    }
  }

  /**
   * registers a new user if there index number doesn't exists in the Random Numbers.txt
   */
  private static void Register() {
    try {
      print("\nPlease provided the your details to register", true);
      // prompt for Index Number
      print("Enter your Index Number(text): ");
      String indexNumber = scanner.next();
      // if already registered prevent user from registering again
      if (findUser(indexNumber, false)) {
        print("\nSomeone has already registered with the Index Number you provided. " +
            "If it was you, please select option 2. View from the menu below", true);
        displayMenu();
        // we could also have displayed the user's random number directly
        // print(profile);
      } else {
        // Create a new user
        profile = new HashMap<>();
        profile.put("indexNumber", indexNumber);
        // prompt for First Name
        print("Enter your First Name(text): ");
        profile.put("firstName", scanner.next());

        // prompt for Last Name
        print("Enter your Last Name(text): ");
        profile.put("lastName", scanner.next());

        // prompt for Department
        print("Enter your Department(text): ");
        profile.put("department", scanner.next());

        // prompt for Level
        print("Enter your Level(number): ");
        profile.put("level", scanner.nextInt());

        // prompt for Age
        print("Enter your Age(number > 0): ");
        int age = scanner.nextInt();
        profile.put("age", age);

        // Generate a random number
        profile.put("randomNumber", generateRandomNumber(age));

        // Finally register the user
        registeredData.putIfAbsent(indexNumber, profile);
        writeFile();
        // prompt whether to continue or terminate
        continueSession();
      }
    } catch (Exception e) {
      print(e.getMessage() == null ? "Input is invalid... please check and try again" : e.getMessage());
      continueSession();
    }
  }

  /**
   * Checks if user already exists in the Random Numbers.txt file
   *
   * @param indexNumber : used to search for the user
   * @param printOut    : if true, prints out the user's profile
   */
  private static boolean findUser(String indexNumber, boolean printOut) {
    try {
      profile = new HashMap<>();
      if (indexNumber.isEmpty()) {
        // prompt for Index Number
        print("Enter your Index Number: ");
        indexNumber = scanner.next();
        profile.put("indexNumber", indexNumber);
      }
      // populate the registeredData
      if (registeredData == null || registeredData.isEmpty()) readFile(printOut);
      profile = registeredData.get(indexNumber);
      if (printOut) {
        if (profile != null && !profile.isEmpty())
          print("User found... Random Number: " + profile.get("randomNumber") + "\n\tFull Profile: " + profile, true);
        else print("No user found with that index number");
      }
      return profile != null && !profile.isEmpty();
    } catch (Exception e) {
      print(e.getMessage());
      return true;
    }
  }

  /**
   * Generates a random number in the format XXXX - XXXX - XXXX
   * where X can be any alphanumeric character
   *
   * @param seed : age of the user used to seed the random number
   */
  private static String generateRandomNumber(int seed) {
    try {
      StringBuilder stringBuilder = new StringBuilder(12);
      for (int i = 0; i < 12; i++) {
        int randomInt = random.nextInt(36);
        stringBuilder.append(alphanumericChars.charAt(randomInt));
        if (i == 3 || i == 7) stringBuilder.append(" - ");
      }
      print("New Random number is: " + stringBuilder);
      return stringBuilder.toString();
    } catch (Exception e) {
      print(e.getMessage());
      return "";
    }
  }

  /**
   * @return the full file path of the Random Numbers.txt
   */
  private static String getFilePath() {
    return System.getProperty("user.dir") + "/" + FILENAME;
  }

  /**
   * writes the registeredData to the Random Numbers.txt
   */
  private static void writeFile() {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(getFilePath());
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(registeredData);
      objectOutputStream.close();
      fileOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * reads the contents of the Random Numbers.txt and assign it to the registeredData
   *
   * @param printOut : if true, prints out the registeredData
   */
  private static void readFile(boolean printOut) {
    try {
      registeredData = new HashMap<>();
      FileInputStream fileInputStream = new FileInputStream(getFilePath());
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      registeredData = (Map<String, Map<String, Object>>) objectInputStream.readObject();
      objectInputStream.close();
      fileInputStream.close();
      if (printOut) print("\nAll Registered Users: \n\t" + registeredData);
    } catch (IOException | ClassNotFoundException e) {
      print(e.getMessage());
    }
  }

  /**
   * Alias for System.out.print(default) and System.out.println
   *
   * @param output    What to print out
   * @param printLine If true uses System.out.println. Defaults to false
   */
  private static void print(Object output, boolean printLine) {
    if (printLine) System.out.println(output);
    else System.out.print(output);
  }

  /**
   * Alias for System.out.print(default) and System.out.println
   *
   * @param output What to print out
   */
  private static void print(Object output) {
    print(output, false);
  }

}

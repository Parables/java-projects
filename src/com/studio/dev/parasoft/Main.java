package com.studio.dev.parasoft;


import java.io.*;
import java.util.*;

public class Main {
  private static final Scanner scanner = new Scanner(System.in);
  private static final String FILENAME = "Random Numbers.txt";
  private static Map<String, Map<String, Object>> registeredData = new HashMap<>();
  private static Map<String, Object> profile = new HashMap<>();
  private static final Random random = new Random();
  private static final String alphanumericChars =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public static void main(String[] args) {
    //      Load the file into the registeredData
    readFile(false);
//    displayMenu();
  }

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
          break;
        default:
          print("Invalid option", true);
      }
    } catch (InputMismatchException e) {
      print("Invalid input...  Goodbye");
    }
    continueSession();
  }

  private static void continueSession() {
    try {
      print("\n\nDo you want to continue your session? (Default option: Yes) [Y/n] \t\n Enter your option: ");
      String choice = scanner.next();
      if (choice.equalsIgnoreCase("n") || choice.equalsIgnoreCase("no")) print("Goodbye");
      else displayMenu();
    } catch (Error e) {
      print("Invalid input... goodbye");
    }
  }

  private static void Register() {
    print("\nPlease provided the your details to register", true);
    // prompt for Index Number
    print("Enter your Index Number: ");
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
      print("Enter your First Name: ");
      profile.put("firstName", scanner.next());

      // prompt for Last Name
      print("Enter your Last Name: ");
      profile.put("lastName", scanner.next());

      // prompt for Department
      print("Enter your Department: ");
      profile.put("department", scanner.next());

      // prompt for Level
      print("Enter your Level: ");
      profile.put("level", scanner.next());

      // prompt for Age
      print("Enter your Age: ");
      int age = scanner.nextInt();
      profile.put("age", age);

      // Generate a random number
      profile.put("randomNumber", generateRandomNumber(age));

      // Finally register the user
      registeredData.putIfAbsent(profile.get("indexNumber").toString(), profile);
      writeFile();
    }
  }


  private static boolean findUser(String indexNumber, boolean printOut) {
    profile = new HashMap<>();
    if (indexNumber.isEmpty()) {
      // prompt for Index Number
      print("Enter your Index Number: ");
      indexNumber = scanner.next();
      profile.put("indexNumber", indexNumber);
    }
    if (registeredData == null || registeredData.isEmpty()) readFile(printOut);
    profile = registeredData.get(indexNumber);
    if (printOut) {
      if (profile != null && !profile.isEmpty())
        print("User found... Random Number: " + profile.get("randomNumber") + "\n\tFull Profile: " + profile, true);
      else print("No user found with that index number");
    }
    return profile != null && !profile.isEmpty();
  }


  private static String generateRandomNumber(int seed) {
    StringBuilder stringBuilder = new StringBuilder(12);
    for (int i = 0; i < 12; i++) {
      int randomInt = random.nextInt(62);
      stringBuilder.append(alphanumericChars.charAt(randomInt));
      if (i == 3 || i == 7) stringBuilder.append(" - ");
    }
    print("New Random number is: " + stringBuilder);
    return stringBuilder.toString();
  }

  private static String getFilePath() {
    return System.getProperty("user.dir") + "/" + FILENAME;
  }

  private static void writeFile() {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(getFilePath());
      DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(fileOutputStream));
      dataOutputStream.writeUTF(registeredData.toString());
      dataOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void readFile(boolean printOut) {
    try {

      FileInputStream fileInputStream = new FileInputStream(getFilePath());
      DataInputStream reader = new DataInputStream(fileInputStream);
      String data = reader.readUTF();
      reader.close();
      if (printOut) print(data);
//      convert data to registerData
      transformData(data);
    } catch (IOException e) {
//      e.printStackTrace();
      if (printOut) print("Random Number.txt file does not exist. ");
    }
  }

  private static void transformData(String strData) {
    // clear the content of registeredData
    registeredData = new HashMap<>();
    /*
    {
      UEDP40402000= {
        firstName=PRINCW,
        lastName=Asiedu,
        level=400,
        randomNumber=XXXX - XXXX - XXXX,
        indexNumber=UEDP40402000,
        department=IGraphics, age=25
      },
      UEDP20004017={
        firstName=Emma,
        lastName=Bolt,
        level=400,
        randomNumber=XXXX - XXXX - XXXX,
        indexNumber=UEDP20004017,
        department=COmps,
        age=27
      }
   }
     */

    strData = strData.substring(1, strData.length() - 1);
    print("remove curly brackets: \n" + strData, true);
    String[] profileMaps = strData.split("=");
//    print("Profile Maps" + profileMaps, true);
    for (String profileData : profileMaps)                        //iterate over the pairs
    {
      print("Profile DAta: \n " + profileData + "\n\n");

//      String[] profile = profileData.split("=");
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

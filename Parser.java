import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

// Cameron Moe
// HTTP Log File Parsing
// 10/24/20

public class Parser {

  public static void main(String[] args) {
    //By default, the file name is the one assigned in the problem, can also be changed with -f flag in terminal
    String fileName = "NASA_access_log_Aug95";
    int option = 0;
    // By default, top number is set to 10 (i.e. top 10), can be changed to any number of top results for reports
    // The number 10 is shown throughout the UI, all pulling from this variable, change once change everywhere
    int topNumber = 10;
    // File name can be given in terminal with -f flag
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-f") && i != args.length - 1) {
        fileName = args[i + 1];
        break;
      }
    }
    // Report option can be given in terminal with -o flag, if not used, menu prompts option choices
    // Includes error checking to ensure proper option is chosen
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-o") && i != args.length - 1) {
        try {
          option = Integer.parseInt(args[i + 1]);
        } catch(NumberFormatException e) {
          System.out.println("Option argument given was invalid");
          return;
        }
        if (option < 1 || option > 5) System.out.println("Option argument must be between 1 and 5");
        break;
      }
    }
    // If no option given when calling program, presents user with menu to choose which report they'd like
    if (option == 0) {
      String divider = "-------------------------------------------------------------------------";
      System.out.println(divider);
      System.out.println("Which report would you like to generate? Please type a number 1-5");
      System.out.println("1. Top " + topNumber + " requested pages");
      System.out.println("2. Percentage of successful requests");
      System.out.println("3. Percentage of unsuccessful requests");
      System.out.println("4. Top " + topNumber + " unsuccessful page requests");
      System.out.println("5. Top " + topNumber + " hosts making the most requests");
      System.out.println(divider);
      Scanner optionScan = new Scanner(System.in);
      option = Integer.parseInt(optionScan.nextLine());
      // Error handling to ensure proper option is chosen
      while (option < 1 || option > 5) {
        System.out.println("Please enter a valid option by typing a number from 1 to 5");
        try {
          option = Integer.parseInt(optionScan.nextLine());
        } catch(NumberFormatException e) {
          System.out.println("Please enter a valid option by typing a number from 1 to 5");
        }
      }
    }
    // switch case to call appropriate method based on desired report
    // separating reports into methods eliminates storing unneeded data not relevant to the desired report
    switch (option) {
    case 1:
      generateTopRequests(fileName, topNumber);
      break;
    case 2:
      generateResponseCountPercentage(fileName, true);
      break;
    case 3:
      generateResponseCountPercentage(fileName, false);
      break;
    case 4:
      generateUnsuccessfulRequests(fileName, topNumber);
      break;
    case 5:
      generateTopHosts(fileName, topNumber);
      break;
    }
    return;
  }

  // Validate Request method validates a line being read from the file to see that its formatted correctly
  // It is assumed, if the line is not formatted correctly, it will not be used in the report
  // Parameters: String request
  // Returns: boolean true if valid, false if invalid
  private static boolean validateRequest(String request) {
    String[] words = request.split(" ");
    if (words.length != 10) return false;
    if (words[0].equals("-")) return false;
    if (words[8].equals("-")) return false;
    return true;
  }

  // Sort Map method takes an unsorted HashMap of <String, Integer> and sorts by descending order
  // Takes advantage of Java 8, streams, Lambda, Collectors
  // Parameters: HashMap <String, Integer> input (an unsorted HashMap), int topNumber (top number of results, i.e top 10)
  // Returns: A sorted HashMap by descending order
  private static HashMap <String, Integer> sortMap(HashMap <String, Integer> input, int topNumber) {
    HashMap <String, Integer> sortedMap = input.entrySet().stream()
    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
    .limit(topNumber)
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (first, second) ->first, LinkedHashMap::new));
    return sortedMap;
  }

  // Generate Top Requests (option 1) creates a report of the top requests and the counts of each
  // Parameters: String fileName to report on, int topNumber (top number of results, i.e top 10)
  // Returns: A string representing the last result of the report, used for testing purposes
  public static String generateTopRequests(String fileName, int topNumber) {
    File file = new File("./" + fileName);
    try {
      Scanner logScanner = new Scanner(file);
      // requests are stored in a hashmap to also store the count of each request
      HashMap <String, Integer> requestCounts = new HashMap <String, Integer>();
      while (logScanner.hasNextLine()) {
        String line = logScanner.nextLine();
        // If the line is valid, grab the request and either add to hash map, or if exists increase the count by 1
        if (validateRequest(line)) {
          String[] words = line.split(" ");
          String request = words[5].substring(1) + " " + words[6] + " " + words[7].substring(0, words[7].length() - 1);
          requestCounts.put(request, requestCounts.getOrDefault(request, 0) + 1);
        }
      }
      String divider = "-------------------------------------------------------------------------";
      System.out.println(divider + "\nTop " + topNumber + " requested pages and number of requests made for each:\n");
      // sort the requests to get the top number to report, and print the report
      HashMap <String, Integer> topRequests = sortMap(requestCounts, topNumber);
      int ranking = 1;
      String result = "";
      for (Map.Entry <String, Integer> entry: topRequests.entrySet()) {
        String string1 = ranking+++". " + entry.getKey();
        String string2 = Integer.toString(entry.getValue());
        result = entry.getKey() + " " + Integer.toString(entry.getValue());
        System.out.printf("%-60s %10s %n", string1, string2);
      }
      System.out.println(divider);
      return result;

    } catch(FileNotFoundException e) {
      System.out.println("Could not find the file given");
    }
    return null;
  }

  // Generate Response Count Percentage (options 2, 3) creates a report of the response count percentage
  // Parameters: String fileName to report on, boolean successResponseCode
  // for successResponseCode, true reports on successful response codes, false reports on unsuccessful response codes
  // Returns: A string representing the percentage calculated, used for testing purposes
  public static String generateResponseCountPercentage(String fileName, boolean successResponseCode) {
    File file = new File("./" + fileName);
    try {
      Scanner logScanner = new Scanner(file);
      // response codes are stored in a hashmap to also store the count of each response code
      HashMap <Integer, Integer> responseCodeCounts = new HashMap <Integer, Integer>();
      while (logScanner.hasNextLine()) {
        String line = logScanner.nextLine();
        if (validateRequest(line)) {
          // If the line is valid, grab the request and either add to hash map, or if exists increase the count by 1
          String[] words = line.split(" ");
          responseCodeCounts.put(Integer.parseInt(words[8]), responseCodeCounts.getOrDefault(Integer.parseInt(words[8]), 0) + 1);
        }
      }
      String divider = "-------------------------------------------------------------------------";
      double success = 0;
      double fail = 0;
      // calculate the number of success and fail response codes by checking each entry of the hash map
      for (Map.Entry <Integer, Integer> entry: responseCodeCounts.entrySet()) {
        if (entry.getKey() >= 200 && entry.getKey() <= 399) {
          success += entry.getValue();
        } else {
          fail += entry.getValue();
        }
      }
      double result;
      String resultMessage;
      // calculate either successful or unsuccessful percentage, and prepare message to be sent in report
      // this is based on the boolean successResponseCode parameter based on which report is desired
      if (successResponseCode) {
        result = success / (success + fail) * 100;
        resultMessage = "successful";
      } else {
        result = fail / (success + fail) * 100;
        resultMessage = "unsuccessful";
      }
      // if there are no requests, print this message, avoids math errors with divide by 0
      if (success + fail == 0) {
        System.out.printf("%s%nPercentage of requests that were %s could not be calculated, as there were no requests%n%s%n", divider, resultMessage, divider);
        return "";
      }
      // print the report
      System.out.printf("%s%nPercentage of requests that were %s: %.2f%%\n%s%n", divider, resultMessage, result, divider);
      return String.format("%.2f", result);
    } catch(FileNotFoundException e) {
      System.out.println("Could not find the file given");
    }
    return "";
  }

  // Generate Unsuccessful Requests (option 4) creates a report of the top unsuccessful requests and the counts of each
  // Parameters: String fileName to report on, int topNumber (top number of results, i.e top 10)
  // Returns: A string representing the last result of the report, used for testing purposes
  public static String generateUnsuccessfulRequests(String fileName, int topNumber) {
    File file = new File("./" + fileName);
    try {
      Scanner logScanner = new Scanner(file);
      // requests are stored in a hashmap to also store the count of each request
      HashMap <String, Integer> unsuccessfulCounts = new HashMap <String, Integer>();
      while (logScanner.hasNextLine()) {
        String line = logScanner.nextLine();
        if (validateRequest(line)) {
          // if the line is valid, grab the request
          // if it has an unsuccessful response code and either add to hash map, or if exists increase the count by 1
          String[] words = line.split(" ");
          if (Integer.parseInt(words[8]) < 200 || Integer.parseInt(words[8]) > 399) {
            String unsuccessfulRequest = words[5].substring(1) + " " + words[6] + " " + words[7].substring(0, words[7].length() - 1);
            unsuccessfulCounts.put(unsuccessfulRequest, unsuccessfulCounts.getOrDefault(unsuccessfulRequest, 0) + 1);
          }
        }
      }
      String divider = "-------------------------------------------------------------------------";
      System.out.println(divider + "\nTop " + topNumber + " unsuccessful page requests:\n");
      Map <String, Integer> topUnsuccessfulRequests = sortMap(unsuccessfulCounts, topNumber);
      // sort the requests to get the top number to report, and print the report
      int ranking = 1;
      String result = "";
      for (Map.Entry <String, Integer> entry: topUnsuccessfulRequests.entrySet()) {
        String string1 = ranking+++". " + entry.getKey();
        String string2 = Integer.toString(entry.getValue());
        result = entry.getKey() + " " + Integer.toString(entry.getValue());
        System.out.printf("%-60s %10s %n", string1, string2);
      }
      System.out.println(divider);
      return result;
    } catch(FileNotFoundException e) {
      System.out.println("Could not find the file given");
    }
    return null;
  }

  // Generate Top Hosts (option 5) creates a report of the top hosts making requests and the counts of each
  // Parameters: String fileName to report on, int topNumber (top number of results, i.e top 10)
  // Returns: A string representing the last result of the report, used for testing purposes
  public static String generateTopHosts(String fileName, int topNumber) {
    File file = new File("./" + fileName);
    try {
      Scanner logScanner = new Scanner(file);
      // hosts are stored in a hashmap to also store the count of each host
      HashMap <String, Integer> hostCounts = new HashMap <String, Integer>();
      while (logScanner.hasNextLine()) {
        String line = logScanner.nextLine();
        if (validateRequest(line)) {
          // If the line is valid, grab the host and either add to hash map, or if exists increase the count by 1
          String[] words = line.split(" ");
          hostCounts.put(words[0], hostCounts.getOrDefault(words[0], 0) + 1);
        }
      }
      String divider = "-------------------------------------------------------------------------";
      System.out.println(divider + "\nTop " + topNumber + " hosts making the most requests:");
      Map <String, Integer> topHosts = sortMap(hostCounts, topNumber);
      // sort the hosts to get the top number to report, and print the report
      int ranking = 1;
      String result = "";
      for (Map.Entry <String, Integer> entry: topHosts.entrySet()) {
        String string1 = ranking+++". " + entry.getKey();
        String string2 = Integer.toString(entry.getValue());
        result = entry.getKey() + " " + Integer.toString(entry.getValue());
        System.out.printf("%-60s %10s %n", string1, string2);
      }
      System.out.println(divider);
      return result;

    } catch(FileNotFoundException e) {
      System.out.println("Could not find the file given");
    }
    return null;
  }

}

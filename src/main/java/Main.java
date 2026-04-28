import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       ArrayList<Transaction> transactions = transactionList();
        Scanner scanner = new Scanner(System.in);
        double moneyTransferred;
        String fullTransactionInfo;

        while (true){
           System.out.print("""
                   Welcome to BlackFire Accounting
                   A) Add a deposit
                   B) Make a payment
                   C) Open Ledger
                   X) Exit
                   Select an option:""");
           String userInput = scanner.nextLine();

           switch (userInput.toLowerCase()){
               case "a":
                   System.out.print("Enter amount deposited: ");
                   moneyTransferred = scanner.nextDouble();
                   scanner.nextLine();
                   if (moneyTransferred < 0){
                       moneyTransferred *= -1;
                   }

                   fullTransactionInfo = transactionInfo(scanner) + moneyTransferred;

                   transactionWriter(fullTransactionInfo);

                   break;
               case "b":
                   System.out.println("Enter Payment: ");
                   moneyTransferred = scanner.nextDouble();
                   scanner.nextLine();
                   if (moneyTransferred > 0){
                       moneyTransferred *= -1;
                   }

                   fullTransactionInfo = transactionInfo(scanner) + moneyTransferred;

                   transactionWriter(fullTransactionInfo);

                   break;
               case "c":
                   ledgerMenu(scanner, transactions);
                   break;


               case "x":
                   System.err.println("EXITING PROGRAM...");
                   System.exit(0);
               default:
                   System.err.println("INVALID INPUT TRY AGAIN");
           }


       }


    }

    private static void ledgerMenu(Scanner scanner, ArrayList<Transaction> transactions) {


        //Anchor for searches
        LocalDate today = LocalDate.now();
        //First of the month
        LocalDate start = today.withDayOfMonth(1);
        //End of search
        LocalDate end = today;


        boolean active = true;
        while (active){

            String userInput;
            System.out.print("""
                Select How to display your Transactions
                1) Month to date
                2) Previous month
                3) Year to date
                4) Previous year
                5) Search by vendor
                0) Return to menu:""");
            userInput = scanner.nextLine();

        switch (userInput){
            case "1":

                //Month to date search
                for (Transaction t : transactions){
                    if (!t.transactionDate.isBefore(start) && !t.transactionDate.isAfter(end)){
                        System.out.println(t);
                    }
                }
                break;
            case "2":

                 // Previous month search
                start = today.withDayOfMonth(1).minusMonths(1);
                end = today.withDayOfMonth(1).minusDays(1);
                for (Transaction t : transactions){
                    if (!t.transactionDate.isBefore(start) && !t.transactionDate.isAfter(end)){
                        System.out.println(t);
                    }
                }
                break;
            case "3":

                //Year to date search
                start = today.withDayOfYear(1);
                for (Transaction t : transactions){
                    if (!t.transactionDate.isBefore(start) && !t.transactionDate.isAfter(end)){
                        System.out.println(t);
                    }
                }
                break;
            case "4":

                //Previous year search
                start = today.minusYears(1).withDayOfYear(1);
                end = today.minusYears(1).withMonth(12).withDayOfMonth(31);
                for (Transaction t : transactions){
                    if (!t.transactionDate.isBefore(start) && !t.transactionDate.isAfter(end)){
                        System.out.println(t);
                    }
                }
                break;
            case "5":

                //Search by vendor
                System.out.println("Enter Vendor Name: ");
                userInput = scanner.nextLine();
                for (Transaction t : transactions){
                    if (t.vendor.toLowerCase().contains(userInput.toLowerCase())){
                        System.out.println(t);
                    }
                }
                break;
            case "0":
                active = false;
                break;
            default:
                System.out.println("ENTER CORRECT OPTION");
        }
         }
    }

    // Writes transactions to csv
    private static void transactionWriter(String fullTransactionInfo) {
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv",true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.newLine();
            bufferedWriter.write(fullTransactionInfo);
            System.out.println("Your info has been processed");



             bufferedWriter.close();
        } catch (IOException e) {
            System.err.print("Error Writing File");
            throw new RuntimeException(e);
        }
    }

    // Reads and returns Date,Time,Description,VendorName
    private static String transactionInfo(Scanner scanner) {

        LocalDate date = null;
        while (true){
            System.out.print("Enter Transaction date(YYYY-MM-DD): ");
            String inputDate = scanner.nextLine();
            
            try {
                date = LocalDate.parse(inputDate);
                break;

            } catch (DateTimeParseException e) {
                System.out.println("INCORRECT FORMAT ENTER(YYYY-MM-DD)");
            }
        }

        LocalTime time = null;
        String outPutTime;
        while (true){
            System.out.print("Enter Transaction time(HH:mm:ss): ");
            String inputTime = scanner.nextLine();
            
            try {

                time = LocalTime.parse(inputTime);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                outPutTime = time.format(formatter);
                break;
            }catch (DateTimeParseException e){
                System.out.println("INCORRECT FORMAT PLEASE ENTER(HH:mm:ss)");
            }
        }

        System.out.print("Please enter short description of deposit: ");
        String description = scanner.nextLine();

        System.out.print("Enter Vendor name: ");
        String vendor = scanner.nextLine();
        
        String stringDate = date.toString();

        return stringDate+ "|" + outPutTime + "|" + description + "|" + vendor + "|";
    }

    // Reads and returns transactions from csv
    private static ArrayList<Transaction> transactionList() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader buffReader = new BufferedReader(fileReader);


            String line;
            buffReader.readLine();
            while ((line = buffReader.readLine()) != null) {

                String[] transactionInfo = line.split("\\|");

                Transaction transaction = new Transaction(
                        LocalDate.parse(transactionInfo[0]),
                        LocalTime.parse(transactionInfo[1]),
                        transactionInfo[2],
                        transactionInfo[3],
                        Double.parseDouble(transactionInfo[4]));
                transactions.add(transaction);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       ArrayList<Transaction> transactions = transactionList();
        Scanner scanner = new Scanner(System.in);

       while (true){
           System.out.print("""
                   Welcome to BlackFire Accounting
                   A) Add a deposit
                   B) Make a payment
                   C) Open Ledger
                   X) Exit
                   Select an option:""");
           String userInput = scanner.nextLine();

           switch (userInput){
               case "a", "A":

                   System.out.println("Enter amount deposited: ");
                   double depositedAmount = scanner.nextDouble();
                   scanner.nextLine();
                   if (depositedAmount < 0){
                       depositedAmount *= -1;
                   }

                   String fullTransactionInfo = transactionInfo(scanner) + depositedAmount;


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

                   break;
               case "b","B":
                   break;
               case "x","X":
                   System.err.println("EXITING PROGRAM...");
                   System.exit(0);
               default:
                   System.err.println("INVALID INPUT TRY AGAIN");
           }


       }


    }

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
        while (true){
            System.out.print("Enter Transaction time(HH:mm:ss): ");
            String inputTime = scanner.nextLine();
            
            try {
                time = LocalTime.parse(inputTime);
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
        String stringTime = time.toString();


        return stringDate+ "|" + stringTime + "|" + description + "|" + vendor + "|";
    }

    // Reads and returns transaction.csv
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

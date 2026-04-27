import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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
                   B) Add a payment
                   X) Exit
                   Select an option:
                   """);
           String userInput = scanner.nextLine();

           switch (userInput){
               case "a", "A":
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

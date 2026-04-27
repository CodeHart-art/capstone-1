import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
       ArrayList<Transaction> transactions = transactionList();



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

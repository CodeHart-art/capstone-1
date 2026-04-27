import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    LocalDate transactionDate;
    LocalTime transactionTime;
    String description;
    String vendor;
    double transactionAmount;

    public Transaction(LocalDate transactionDate, LocalTime transactionTime, String description, String vendor, double transactionAmount) {
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.description = description;
        this.vendor = vendor;
        this.transactionAmount = transactionAmount;
    }

    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String outPutTime = transactionTime.format(formatter);
        return transactionDate + "|" + outPutTime + "|" + description + "|" + vendor + "|" + transactionAmount;
    }
}

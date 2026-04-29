import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public LocalTime getTransactionTime() {
        return transactionTime;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    private final LocalDate transactionDate;
    private final LocalTime transactionTime;
    private final String description;
    private final String vendor;
    private final double transactionAmount;

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

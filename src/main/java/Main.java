import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String GOLD = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";
    public static final String TRANSACTIONS_CSV = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        ArrayList<Transaction> transactions = transactionList();
        Scanner scanner = new Scanner(System.in);
        double moneyTransferred;
        String fullTransactionInfo;

        while (true) {
            System.out.print(CYAN + """
                    ╔══════════════════════════════════╗
                    ║      ⚔️ BlackFire Ledger ⚔️      ║
                    ╚══════════════════════════════════╝
                    
                    [A] 🪙 Record Tribute (Deposit)
                    [B] 💸 Pay a Debt (Payment)
                    [C] 📜 View the Ledger
                    
                    [X] 🚪 Leave the Hall
                    
                    ➤ Choose thy action:""" + RESET);
            String userInput = scanner.nextLine();

            switch (userInput.toLowerCase()) {
                case "a":
                    // First case writes a deposit
                    System.out.print("Enter amount deposited: ");
                    moneyTransferred = scanner.nextDouble();
                    scanner.nextLine();
                    if (moneyTransferred < 0) {
                        moneyTransferred *= -1;
                    }

                    fullTransactionInfo = transactionInfo(scanner) + moneyTransferred;
                    transactionWriter(fullTransactionInfo);

                    break;
                case "b":
                    //Second case writes a payment
                    System.out.println("Enter Payment: ");
                    moneyTransferred = scanner.nextDouble();
                    scanner.nextLine();
                    if (moneyTransferred > 0) {
                        moneyTransferred *= -1;
                    }

                    fullTransactionInfo = transactionInfo(scanner) + moneyTransferred;
                    transactionWriter(fullTransactionInfo);

                    break;
                case "c":
                    // first sub menu
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

    // displays ledger menu options
    private static void ledgerMenu(Scanner scanner, ArrayList<Transaction> transactions) {
        String userInput;
        boolean active = true;
        while (active) {

            System.out.print(GOLD +"""
                    ----- 📜 Ledger Archives 📜 -----
                    
                    [A] All Records
                    [D] Deposits (Tributes)
                    [P] Payments (Debts)
                    [R] Reports of the Realm
                    [M] Return to the Great Hall
                    
                    ➤ Choose thy record:"""+ RESET);
            userInput = scanner.nextLine();

            switch (userInput.toLowerCase()) {
                case "a":
                    ledgerHeading();
                    transactions.sort(Comparator.comparing(Transaction::getTransactionDate));
                    for (Transaction t : transactions) {
                        formattedMenuDisplay(t);
                    }
                    break;
                case "d":
                    ledgerHeading();
                    for (Transaction t : transactions) {
                        if (t.getTransactionAmount() > 0) {
                            formattedMenuDisplay(t);
                        }
                    }
                    break;
                case "p":
                    ledgerHeading();
                    for (Transaction t : transactions) {
                        if (t.getTransactionAmount() < 0) {
                            formattedMenuDisplay(t);
                        }
                    }
                    break;
                case "r":
                    reportsMenu(scanner, transactions);
                    break;
                case "m":
                    active = false;
                    break;
            }
        }
    }

    private static void ledgerHeading(){
        System.out.println(                 "────── 📜 Royal Ledger 📜 ──────");

        System.out.printf("%-15s %-15s %-30s %-30s %-11s%n",
                "Date",
                "Time",
                "Description",
                "Guild/Vendor",
                "Gold"
        );

        System.out.println( "-----------------------------------------------------------------------------------------------");
    }

    private static void formattedMenuDisplay(Transaction t) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String outPutTime = t.getTransactionTime().format(formatter);

        String amountStr;

        if (t.getTransactionAmount() > 0) {
            amountStr = GREEN + String.format("%.2f", t.getTransactionAmount()) + RESET;
        } else {
            amountStr = RED + String.format("%.2f", t.getTransactionAmount()) + RESET;
        }

        System.out.printf("%-15s %-15s %-30s %-30s $%-10s%n",
                t.getTransactionDate(),
                outPutTime,
                t.getDescription(),
                t.getVendor(),
                amountStr);
    }

    // displays report menu options
    private static void reportsMenu(Scanner scanner, ArrayList<Transaction> transactions) {


        //Anchor for searches
        LocalDate today = LocalDate.now();

        boolean active = true;
        while (active) {

            String userInput;
            System.out.print(GOLD + """
                    ════════════════════════════════════
                            📜 Reports of the Realm 📜
                    ════════════════════════════════════
                    [1] 🗓️  Records of the Current Month
                    [2] 🕰️  Records of the Previous Month
                    [3] 📆  Records of the Current Year
                    [4] 🏛️  Records of the Previous Year
                    
                    [5] 🔎 Seek Records by Vendor
                    
                    [0] 🚪 Return to the Great Hall:"""+ RESET);
            userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    int thisMonth = today.getMonthValue();

                    //Month to date search
                    ledgerHeading();
                    for (Transaction t : transactions) {
                        if (t.getTransactionDate().getMonthValue() == thisMonth) {
                            formattedMenuDisplay(t);
                        }
                    }
                    break;
                case "2":
                    // Previous month search

                    int lastMonth = today.getMonthValue() - 1;
                    int sameYear = today.getYear();

                    ledgerHeading();
                    for (Transaction t : transactions) {
                        if (t.getTransactionDate().getMonthValue() == lastMonth && t.getTransactionDate().getYear() == sameYear) {
                            formattedMenuDisplay(t);
                        }
                    }
                    break;
                case "3":
                    //Year to date search
                    int thisYear = today.getYear();

                    ledgerHeading();
                    for (Transaction t : transactions) {
                        if (thisYear == t.getTransactionDate().getYear()) {
                            formattedMenuDisplay(t);
                        }
                    }
                    break;
                case "4":
                    //last year search
                    int lastYear = today.getYear() - 1;

                    ledgerHeading();
                    for (Transaction t : transactions) {
                        if (t.getTransactionDate().getYear() == lastYear) {
                            formattedMenuDisplay(t);
                        }

                    }
                    break;
                case "5":
                    //Search by vendor
                    System.out.println("Enter Vendor Name: ");
                    userInput = scanner.nextLine();

                    ledgerHeading();
                    for (Transaction t : transactions) {
                        if (t.getVendor().toLowerCase().contains(userInput.toLowerCase())) {
                            formattedMenuDisplay(t);
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
            FileWriter fileWriter = new FileWriter(TRANSACTIONS_CSV, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.newLine();
            bufferedWriter.write(fullTransactionInfo);
            System.out.println("-----");
            System.out.println("✔ The record has been inscribed into the ledger.");


            bufferedWriter.close();
        } catch (IOException e) {
            System.err.print("Error Writing File: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Reads and returns Date,Time,Description,VendorName
    private static String transactionInfo(Scanner scanner) {

        LocalDate date;
        while (true) {
            System.out.print("Enter Transaction date(YYYY-MM-DD): ");
            String inputDate = scanner.nextLine();

            try {
                date = LocalDate.parse(inputDate);
                break;

            } catch (DateTimeParseException e) {
                System.out.println("INCORRECT FORMAT ENTER(YYYY-MM-DD)");
            }
        }

        LocalTime time;
        String outPutTime;
        while (true) {
            System.out.print("Enter Transaction time(HH:mm:ss): ");
            String inputTime = scanner.nextLine();

            try {
                time = LocalTime.parse(inputTime);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                outPutTime = time.format(formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("✖ That is not a valid command, traveler this is(HH:mm:ss)");
            }
        }

        System.out.print("Please enter short description of deposit: ");
        String description = scanner.nextLine();

        System.out.print("Enter Vendor name: ");
        String vendor = scanner.nextLine();

        String stringDate = date.toString();
        return stringDate + "|" + outPutTime + "|" + description + "|" + vendor + "|";
    }

    // Reads and returns transactions from csv
    private static ArrayList<Transaction> transactionList() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(TRANSACTIONS_CSV);
            BufferedReader buffReader = new BufferedReader(fileReader);

            String line;
            buffReader.readLine();
            while ((line = buffReader.readLine()) != null) {

                String[] transactionInfo = line.split("\\|");

                Transaction transaction = new Transaction(LocalDate.parse(transactionInfo[0]), LocalTime.parse(transactionInfo[1]), transactionInfo[2], transactionInfo[3], Double.parseDouble(transactionInfo[4]));
                transactions.add(transaction);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}

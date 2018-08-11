import javax.persistence.*;
import java.time.LocalDate;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Transaction")
public class Transaction {

    @Id
    @Column(name="transactionId")
    private int transactionId;

    @Column(name = "transactionType")
    private String transactionType;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "transactionAmount")
    private float transactionAmount;

    @Column(name = "balance")
    private float balance;

    @Column(name = "transactionDate")
    private LocalDate transactionDate;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public float getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public void statement(String accountNumber){

        connectJDBC con=new connectJDBC();
        con.statement(accountNumber,new Transaction());

    }

    public void updateTransaction(float amount, LocalDate d, String type, String accno,float balance)    {
        Transaction t=new Transaction();
        t.setTransactionAmount(amount);
        t.setTransactionDate(d);
        t.setTransactionType(type);
        Random rand=new Random();
        int number = rand.nextInt(999)+1000 ;
        t.setTransactionId(number);
        t.setBalance(balance);
        t.setAccountNumber(accno);
        connectJDBC con=new connectJDBC();
        con.insert(t);

    }
}
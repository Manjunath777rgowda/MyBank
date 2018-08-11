//Divyaa's module
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;

@Entity
@Table(name="ReccurringDeposite")
public class RecurringAccount {

    @Id
    @Column(name="refferenceNumber")
    private int referenceNumber;


    @Column(name="creationDate")
    private LocalDate creationDate;

    @Column(name="accountNumber")
    private String accountNumber;

    @Column(name="depositPeriod")
    private int depositPeriod;

    @Column(name="initialDeposit")
    private float initialDeposit;

    @Column(name="balance")
    private float balance;

    @Column(name="nominee")
    private String nominee;

    @Column(name="relationship")
    private String relationship;

    @Column(name="installmentCount")
    private int installmentCount;

    private int status;

    private LocalDate nextInstallment;

    public LocalDate getNextInstallment() {
        return nextInstallment;
    }

    public void setNextInstallment(LocalDate nextInstallment) {
        this.nextInstallment = nextInstallment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setInitialDeposit(float initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public int getInstallmentCount() {
        return installmentCount;
    }

    public void setInstallmentCount(int installmentCount) {
        this.installmentCount = installmentCount;
    }

    public String getRelationship() {
        return relationship;
    }


    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(int referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getDepositPeriod() {
        return depositPeriod;
    }

    public void setDepositPeriod(int depositPeriod) {
        this.depositPeriod = depositPeriod;
    }

    public float getInitialDeposit() {
        return initialDeposit;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getNominee() {
        return nominee;
    }

    public void setNominee(String nominee) {
        this.nominee = nominee;
    }

    public void createRD(String accno) {
        connectJDBC con=new connectJDBC();
        RecurringAccount rd=new RecurringAccount();

        Random random = new Random();
        int randomPin;

        randomPin = random.nextInt(9000) + 1000;

        rd.setReferenceNumber(randomPin);
        rd.setAccountNumber(accno);

        Validation v=new Validation();
        Scanner input = new Scanner(System.in);
        String nominee;
        do {
            System.out.println("Who is your nominee?");
            nominee= input.next();
        }while (!v.nameValidation(nominee));
        rd.setNominee(nominee);

        String rel;
        do {
            System.out.println("What is the relationship with the nominee?");
            rel = input.next();
        }while (!v.nameValidation(rel));
        rd.setRelationship(rel);

        System.out.println("Choose the deposit period between 1 to 10 years");
        String depositPeriod = input.next();

        while (!Pattern.matches("[1-9]|[1][0]", depositPeriod)) {
            System.out.println("Invalid deposit period");
            depositPeriod = input.next();
        }

        rd.setCreationDate(LocalDate.now());
        rd.setDepositPeriod(Integer.parseInt(depositPeriod));

        rd.setInstallmentCount((rd.getDepositPeriod()*365)/31);

        System.out.println("Your Reference number : "+rd.getReferenceNumber());
//        System.out.println("Number of installments : "+buffer.get(randomPin).getInstallmentCount());

        deposit(1,accno,rd);

        rd.setStatus(1);

        con.insert(rd);
    }

    public void deposit(int firstTransaction,String accountNumber,RecurringAccount rd) {
        float deposit;
        if (firstTransaction == 1) {

            System.out.println("\nHow much you want to deposit?");
            try {
                Scanner input=new Scanner(System.in);
                deposit = input.nextFloat();
                while (deposit <=0 ||  deposit > 100000) {
                    System.out.println("Enter proper amount.");
                    deposit = input.nextFloat();
                }

                rd.setBalance(deposit);

                rd.setInitialDeposit(deposit);

                rd.setInstallmentCount(rd.getInstallmentCount() - 1);
                System.out.println("Remaining installments : " + rd.getInstallmentCount());
                System.out.println("Your next installment is on : "+LocalDate.now().plusDays(31));

                rd.setNextInstallment(LocalDate.now().plusDays(31));

                Transaction transaction=new Transaction();
                transaction.updateTransaction(deposit,LocalDate.now(),"Deposit",accountNumber,deposit+getBalance());

            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }
        else {
            RecurringAccount obj=new RecurringAccount();
            obj=checkReferenceNumber();
            if (obj!=null) {
                if(DAYS.between(obj.getNextInstallment(),LocalDate.now())==31) {
                    System.out.println("Please deposit " + String.format("%.2f",obj.getInitialDeposit()));
                    try {
                        Scanner input = new Scanner(System.in);
                        deposit = input.nextFloat();
                        while (deposit != obj.getInitialDeposit() || deposit <= 0.0 || deposit >= 100000.0) {
                            System.out.println("Enter proper amount.");
                            deposit = input.nextFloat();
                        }

                        obj.setBalance(obj.getBalance() + deposit);

                        obj.setInstallmentCount(obj.getInstallmentCount() - 1);

                        obj.setNextInstallment(LocalDate.now().plusDays(31));
                        connectJDBC con = new connectJDBC();
                        con.update(obj, obj.getReferenceNumber());
                        System.out.println("Remaining installments : " + obj.getInstallmentCount());
                        System.out.println("Your next installment is on : "+obj.getNextInstallment());

                        Transaction transaction = new Transaction();
                        transaction.updateTransaction(deposit, LocalDate.now(), "Deposit", accountNumber, deposit + getBalance());


                    } catch (Exception e) {
                        System.out.println("Invalid input");
                    }
                }
                else
                {
                    System.out.println("Please deposit on : "+obj.getNextInstallment());
                }
            }
            else {
                System.out.println("RD doesnt exist");
            }
        }
    }

    public void withdraw() {
        RecurringAccount random=checkReferenceNumber();
        if(random!=null) {
            if (YEARS.between(random.getCreationDate(), LocalDate.now()) == random.getDepositPeriod()) {
                random.interest(random);
                System.out.println("Your Final Amount" + String.format("%.2f",random.getBalance()));
            } else {
                System.out.println("You can only withdraw money when RD is matured !");
                System.out.println("Remaining installments : "+random.getInstallmentCount());
            }
        }
        else{
            System.out.println("RD doesnt exist");
        }
    }

    public void interest(RecurringAccount random) {
        random.setBalance(random.getBalance() + (random.getBalance() * random.getDepositPeriod() * (6/100)));
    }

    public void showAll(String accountNumber) {
        connectJDBC con=new connectJDBC();
        RecurringAccount rd=new RecurringAccount();

        con.showAllRD(accountNumber,rd);

    }

    public void viewDetails() {
        RecurringAccount random=new RecurringAccount();
        random=checkReferenceNumber();
        if(random!=null && random.getStatus()!=0) {
            System.out.println("--------------------------Your Details----------------------------");
            System.out.println("Your Reference Number     : " + random.getReferenceNumber());
            System.out.println("Your nominee              : " + random.getNominee());
            System.out.println("Relationship with nominee : " + random.getRelationship());
            System.out.println("Your RD lifetime          : " + random.getDepositPeriod());
            System.out.println("Your initial Deposit      : " + String.format("%,2f",random.getInitialDeposit()));
            System.out.println("Balance                   : " + String.format("%.2f",random.getBalance()));
            System.out.println("Remaining Installment     : " + random.getInstallmentCount());
            System.out.println("Your RD creation Date     : " + random.getCreationDate());
            System.out.println("-----------------------------------------------------------------");
        }
        else{
            System.out.println("RD doesnt exist");
        }
    }

    public void recurringAccount(String accountNum) {
        while (true) {
            System.out.println("Press 1-Create RD 2-Deposit into existing RD 3-Show All RD 4-View Details 5-Withdraw 6-Drop RD 7-logout");
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            //choice validation
            switch (choice) {
                case 1:
                    createRD(accountNum);
                    break;
                case 2:
                    deposit(2,accountNum,null);
                    break;
                case 3: showAll(accountNum);
                    break;
                case 4:
                    viewDetails();
                    break;

                case 5:withdraw();
                    break;
                case 6:dropRecurring();
                    break;
                case 7:
                    System.out.println("\n\n####----Logged out Successfully----####");
                    return;

                default:
                    System.out.println("\n\nInvalid choice");
            }
        }
    }

    public void dropRecurring(){
        RecurringAccount rd = checkReferenceNumber();
        if(rd!=null) {
            if (YEARS.between(rd.getCreationDate(), LocalDate.now()) != rd.getDepositPeriod()) {
                System.out.println("Your balance is : " + String.format("%.2f",rd.getBalance()));
                System.out.println("To drop your rd you must pay the penalty");
                System.out.println("The following amount will be deducted from the RD : " + String.format("%.2f",rd.getBalance() * 0.1));
                System.out.println("Do you want to continue? yes | no ");
                Scanner input=new Scanner(System.in);
                String choice=input.nextLine();
                while (!Pattern.matches("[yY][eE][sS]|[nN][oO]", choice)) {
                    System.out.println("Nope! Enter yes | no : ");
                    choice = input.nextLine();
                }

                if(choice.equalsIgnoreCase("yes"))
                {
                    rd.setBalance((float) (rd.getBalance() - rd.getBalance() * 0.1));

                    System.out.println("Please collect : " + String.format("%.2f",rd.getBalance()));

                    rd.setStatus(0);

                    connectJDBC con=new connectJDBC();
                    con.update(rd,rd.getReferenceNumber());

                    System.out.println("Your RD is successfully closed.");
                }
                else{
                    System.out.println("ok");
                    return;
                }
            } else {
                System.out.println("Please collect : " + String.format("%.2f",rd.getBalance()));
                rd.setStatus(0);
                connectJDBC con=new connectJDBC();
                con.update(rd,rd.getReferenceNumber());
                System.out.println("Your RD is successfully closed.");
            }
        }
        else
        {
            System.out.println("RD doesnt exist");
        }


    }

    public RecurringAccount checkReferenceNumber(){
        connectJDBC con=new connectJDBC();
        RecurringAccount rd=new RecurringAccount();

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the reference number : ");
        String random = input.nextLine();
        while (!Pattern.matches("[0-9]{4}", random)) {
            System.out.println("Invalid Refference : ");
            random = input.nextLine();
        }

        rd=con.retriveRD(rd,Integer.parseInt(random));

        if (rd==null)
        {
            return null;
        }
        else {return rd;}
    }

}

import sun.security.util.Password;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.System.exit;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;

@Entity
public class FixedAccount {
    @Column
    private String accountNumber;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column
    private int status;

    @Column
    private String nominee;

    @Column
    private String relation;

    @Column
    private int depositPeriod;

    @Column
    @Id
    private int refNum;

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Column
    private LocalDate creationDate;

    @Column
    private float accountBalance;

    @Column
    private LocalDate depositDate;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(LocalDate depositDate) {
        this.depositDate = depositDate;
    }

    public int getDepositPeriod() {
        return depositPeriod;
    }

    public void setDepositPeriod(int depositPeriod) {
        this.depositPeriod = depositPeriod;
    }

    public String getNominee() {
        return nominee;
    }

    public void setNominee(String nominee) {
        this.nominee = nominee;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getRefNum() {
        return refNum;
    }

    public void setRefNum(int refNum) {
        this.refNum = refNum;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void withdraw() {
        FixedAccount random=checkRef();
        if(random!=null) {
            if (YEARS.between(random.getDepositDate(), LocalDate.now()) == random.getDepositPeriod()) {
                random.interest(random);
                System.out.println("Your Final Amount" + random.getAccountBalance());
            } else {
                System.out.println("You cannot withdraw from your FD account untill maturity....either you can close your Fd by paying penalty!");
            }
        }
        else{
            System.out.println("FD doesnt exist");
        }
    }

    public float interest(FixedAccount fd) {

        float intrest;
        intrest=(fd.getAccountBalance()*fd.getDepositPeriod()*(float)0.06);
        return intrest;
    }

    public void deposit(int random, FixedAccount fd) {

        Scanner input = new Scanner(System.in);
        System.out.println("\nhow much you want to deposit");
        try {
            float deposit = input.nextFloat();
            while (deposit <= 0 || deposit > 1000000) {
                System.out.println("Enter proper amount.");
                deposit = input.nextFloat();
            }

            fd.setAccountBalance(deposit);
        } catch (Exception e) {
            System.out.println("Invalid input");
        }

    }

    public void CreateFd(String accountnum) {

        FixedAccount fd = new FixedAccount();
        Validation v=new Validation();
        refNum = (int) ((Math.random() * 9000) + 1000);
        fd.setRefNum(refNum);
        fd.setAccountNumber(accountnum);
        //System.out.println(refNum);
        Scanner sc = new Scanner(System.in);
        boolean flag;
        do{       System.out.println("enter your nominee name");
            nominee = sc.nextLine();
            flag=v.nameValidation(nominee);
            fd.setNominee(nominee);}
        while(!flag);

        do{
            System.out.println("Enter the relationship between the nominee");
            relation = sc.nextLine();
            flag=v.nameValidation(relation);
            fd.setRelation(relation);}
        while(!flag);
        fd.setRelation(relation);

        Scanner input = new Scanner(System.in);
        System.out.println("Choose the deposit period between 1 to 10 years");
        String depositPeriod = input.next();
        while (!Pattern.matches("[1-9]|[1][0]", depositPeriod)) {
            System.out.println("Invalid deposit period");
            depositPeriod = input.next();
        }
        fd.setDepositPeriod(Integer.parseInt(depositPeriod));
        fd.setDepositDate(LocalDate.now());

        deposit(refNum, fd);

        connectJDBC con = new connectJDBC();

        //interest();
        fd.setStatus(1);
        System.out.println(getStatus());
        fd.setCreationDate(LocalDate.now());
        System.out.println("Your FD account is created and the reference number is "+getRefNum());
        con.insert(fd);
    }

    public void showAll(String accountNumber) {
        connectJDBC con = new connectJDBC();
        FixedAccount fd = new FixedAccount();

        con.showAllFD(accountNumber, fd);

    }

    public FixedAccount checkRef() {
        connectJDBC con = new connectJDBC();
        FixedAccount fd = new FixedAccount();

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the Reference number : ");
        String random = input.nextLine();
        while (!Pattern.matches("[0-9]{4}", random)) {
            System.out.println("Invalid refference number : ");
            random = input.nextLine();
        }

        fd = con.retriveFD(fd, Integer.parseInt(random));

        if (fd == null) {
            System.out.println("FD doesnot exxit");
            return null;
        } else {
            return fd;
        }
    }

    public void fixedAccount(String accountnum) {
        while (true) {
            System.out.println("Press 1-Create FD 2-Show All FD Deposit 3-View Details of particular FD 4-Withdraw 5-logout 6-close any FD");
            Scanner input = new Scanner(System.in);
            //   int choice = input.nextInt();

            String str=input.next();
            Validation v=new Validation();
            int choice = v.choiceValidation(str);

            switch (choice) {
                case 1:
                    CreateFd(accountnum);
                    break;
                case 2:
                    showAll(accountnum);
                    break;
                case 3:
                    viewDetails();
                    break;

                case 4:withdraw();
                    break;
                case 5:
                    System.out.println("\n\n####----Logged out Successfully----####");
                    return;
                case 6:
                    closeFd();
                    break;
                default:
                    System.out.println("\n\nInvalid choice");
            }
        }
    }

    public void closeFd(){
        FixedAccount random = new FixedAccount();
        random = checkRef();
        if(random!=null)
        {

            if(YEARS.between(random.getCreationDate(),LocalDate.now())!=random.getDepositPeriod()){
                float fbal=random.getAccountBalance();
                System.out.println("your present account balance is "+fbal);
                System.out.println("your balance will be deducted by 10% of the amount as you are closing before maturity \nto continue press 1...TO RETURN PRESS ANY OTHER KEY");
                Scanner scn=new Scanner(System.in);
                String var=scn.nextLine();
                Validation v=new Validation();
                int num=v.choiceValidation(var);

                if(num==1) {
                    fbal = (float) (fbal - (fbal * 0.1));
                    random.setAccountBalance(fbal);

                    System.out.println("the amount you obtained is " + random.getAccountBalance());
                    random.setStatus(0);
                    random.setAccountBalance(0);
                    //del query
                }
                else
                { return;}
                // while(var.matches("[1]"));
                connectJDBC c=new connectJDBC();
                c.update(random,random.getRefNum());
                System.out.println("your fd has been deactivated");
            }
            else
            {
                System.out.println("your amount is "+random.getAccountBalance());
                System.out.println(YEARS.between(random.getCreationDate(),LocalDate.now())!=getDepositPeriod());
                random.setStatus(0);
                System.out.println("your fd has been deactivated");
            }


        }}

    public void viewDetails() {
        FixedAccount random = new FixedAccount();
        random = checkRef();
        if (random != null) {
            System.out.println("--------------------------Your Details----------------------------");
            System.out.println("Your Reference Number         : " + random.getRefNum());
            System.out.println("Your FD creation Date         : " + random.getDepositDate());
            System.out.println("Your nominee                  : " + random.getNominee());
            System.out.println("Relationship with nomine      : " + random.getRelation());
            System.out.println("Your FD lifetime              : " + random.getDepositPeriod());
            System.out.println("Your Deposition amount        : " + random.getAccountBalance());
            System.out.println("Your interest on Deposit      : " + interest(random));
            System.out.println("Your total amount(onMaturity) : " + (float)(random.getAccountBalance()+interest(random)));
            System.out.println("-----------------------------------------------------------------");
        }
    }
}

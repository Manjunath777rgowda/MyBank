import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

@Entity
public class Account {
    @Id
    private String accountNumber;
    private int status;
    float balance;
    private LocalDate creationDate;
    private String password;


    @Column(name="customerID")
    private int customeId;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCustomeId() {
        return customeId;
    }

    public void setCustomeId(int customeId) {
        this.customeId = customeId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public boolean createAccount(Details sd,String accountNumber,float limit,int choice) {

        boolean flag=true;
        Account account=new Account();
        Scanner sc=new Scanner(System.in);
        Validation v=new Validation();
        String temp3="";
        String temp;
        Encryption e1=new Encryption();

        do {
            System.out.println("Create your Password ( Password must be at least 4 characters, no more than 8 characters) : ");
            temp = sc.next();

            flag=v.passwordValidation(temp);
            if(!flag){
                System.out.println("the password does not meet the conditions mentioned");
            }

            //temp = sc.next();
            if(flag){
                System.out.println("Re-Enter your Password");
                String temp2 = sc.next();
                if (temp.equals(temp2)) {
                    System.out.println("Password created Successfully");
                    temp3=e1.encrypt(temp);
                    account.setPassword(temp3);
                    flag = true;}
                else{
                    System.out.println(" Password not matched");
                    flag=false;
                }
            }
        }while(!flag);



        if (choice == 1 || choice == 2) {
            while (flag) {
                System.out.println("Enter Initial Amount");
                String str = sc.next();
                if (v.amountValidation(str)){
                account.setBalance(Float.parseFloat(str));
                if (account.getBalance() > 0 && account.getBalance() >= limit) {
                    flag = false;
                } }else System.out.println("Please enter Initial Balance");
            }
        }

        account.setStatus(1);

        account.setAccountNumber(accountNumber);

        account.setCreationDate(java.time.LocalDate.now());

        if (setInputDetails(sd,accountNumber))
        {
            connectJDBC con=new connectJDBC();

            if (con.insert(sd)) {
                sd=(Details)con.retrive(sd,sd.getEmailId());
                if (sd!=null) {
                    account.setCustomeId(sd.getCustomeId());
                    System.out.println("customerId="+account.getCustomeId());
                    if (con.insert(account))
                    {
                        Transaction t=new Transaction();
                        LocalDate depositeDate=java.time.LocalDate.now();
                        t.updateTransaction(account.getBalance(),depositeDate,"Deposite",account.getAccountNumber(),account.getBalance());
                        // System.out.println("inserted");
                }
                else System.out.println("error account");
                }
                else
                {
                    System.out.println("error");
                }
                return true;
            }
            else
                return false;
        }

        return false;

    }

    public boolean setInputDetails( Details sd,String accountNumber) {

        Scanner sc=new Scanner(System.in);
        String str;
        int choice;
        boolean flag=true;

        System.out.println("-----------------------------------Verify your details------------------------------");
        System.out.println("Account Number      : "+accountNumber);
        System.out.println("Account Name        : "+sd.getAccountName());
        System.out.println("Pan number          : "+sd.getPanNumber());
        System.out.println("Email id            : "+sd.getEmailId());
        System.out.println("Adress              : "+sd.getAddress());
        SimpleDateFormat format=new SimpleDateFormat("dd-mm-yyyy");
        Date date1= new Date();
        date1=sd.getDob();
        System.out.println("Date Of Birth       : "+format.getDateInstance().format(date1));
        System.out.println("Phone Number        : "+sd.getPhoneNumber());
        System.out.println("Adhar Number        : "+sd.getAdharNumber());
        //System.out.println("Initial Amount      : "+sd.getInitialamount());
        System.out.println("------------------------------------------------------------------------------------");
        while (flag)
        {
            System.out.println("Please Enter your Choice");
            System.out.println("1-Done 2-Cancel");
            str=sc.next();
            Validation v=new Validation();
            choice=v.choiceValidation(str);
            switch (choice)
            {
                case 1:
                    return true;
                case 2:return false;
                default:System.out.println("Invalid choice");
                    break;
            }
        }

        return false;
    }

}
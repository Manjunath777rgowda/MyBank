import java.time.LocalDate;
import java.util.Scanner;

public class BankOperation {
    String str;
    float amount;
    Scanner sc = new Scanner(System.in);
    Validation v = new Validation();

    public void functionality(String accnumber) {
        boolean flag = true;
        int choice;
        if (accnumber != null) {
            do {
                System.out.println("\n1) Deposit \n2) Withdraw\n3) Balance " +
                        "\n4) Print account information \n5) Statement  \n6) Close any account \n7)logout\nEnter choice [1-7]: ");
                String temp = sc.next();
                choice = v.choiceValidation(temp);
                switch (choice)
                {
                    case 1:
                        Deposit(accnumber);
                        break;
                    case 2:
                        Withdraw(accnumber);
                        break;
                    case 3:
                        Balance(accnumber, 0);
                        break;
                    case 4:
                        AccountDetails(accnumber);
                        break;
                    case 5:
                        statement(accnumber);
                        break;
                    case 6:
                        closeAccount(accnumber);
                        flag = false;
                    case 7:
                        flag = false;
                        break;
                    default:
                        System.out.println("invalid choice");
                        break;
                }
            } while (flag);
        }
    }

    public void statement(String accnumber) {
        Transaction t = new Transaction();
        t.statement(accnumber);
    }

    public void createAccount() {
        Details d = new Details();
        String accnumber = d.getInputDetails();
        if (accnumber != null)
            System.out.println("*****----Account created Successfully----*****");
    }

    public String checkAccount() {
        Account account = new Account();
        System.out.println("enter your account number");
        String accno=sc.next();
        System.out.println("enter your account password");
        String password = sc.next();
        Encryption e1=new Encryption();

        connectJDBC con=new connectJDBC();
        account=(Account)con.retrive(account,accno);

        if (account!=null && account.getStatus()!=0) {
            String pass = account.getPassword();
            String temp5=e1.decrypt(pass);
            //System.out.println();
            String accnum = account.getAccountNumber();
            if (accnum.equals(accno))
                if (password.equalsIgnoreCase(temp5))
                    return accno;
        }
        System.out.println("Invalid Account number or password!!!!!\n Please Enter valid Account number and password");
        return null;
    }

    public void Deposit(String accnumber) {
        connectJDBC con=new connectJDBC();
        Account account=new Account();
        account=(Account)con.retrive(account,accnumber);
        float balance=account.getBalance();
        try {
            System.out.println("\nEnter amount to deposit");
            str = sc.next();
            if (v.amountValidation(str))
            {
                amount = Float.parseFloat(str);
                balance = balance + amount;
                account.setBalance(balance);
                con.update(account,accnumber);
                System.out.println("\n\nRS." + amount + " Deposited Successfully");
                Balance(accnumber,1);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("TRANSACTION FAILURE");
        }
    }

    public void Withdraw(String accnumber) {

        connectJDBC con=new connectJDBC();
        Account account=new Account();
        account=(Account)con.retrive(account,accnumber);
        float balance=account.getBalance();

        System.out.printf("Your current Balance is: Rs.%.2f\n",balance);

        try {
            System.out.println("\nEnter amount to Withdraw (press 0 to go back)");
            str = sc.next();
            if (v.amountValidation(str)) {
                amount = Float.parseFloat(str);

                if((accnumber.charAt(2)=='S' || accnumber.charAt(2)=='s') && (accnumber.charAt(3)=='B' || accnumber.charAt(3)=='b'))
                {
                    if(0 < amount &&  100000< amount)
                    {
                        System.out.println("Maximum withdraw is Rs.1,00,000 only");
                    }

                    else if (0 < amount && balance > amount)
                    {
                        balance = balance - amount;
                        account.setBalance(balance);
                        con.update(account,accnumber);
                        System.out.println("\n\nRS." + amount + " Withdraw Successfully");
                        Balance(accnumber,2);
                    }
                    else System.out.println("Insufficient Balance");


                }
                else if (0 < amount && balance > amount) {
                    balance = balance - amount;
                    account.setBalance(balance);
                    con.update(account,accnumber);
                    System.out.println("\n\nRS." + amount + " Withdraw Successfully");
                    Balance(accnumber,2);
                }
                else
                    System.out.println("Insufficient Balance");
            }

        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println("TRANSACTION FAILURE");
        }
    }

    public void Balance(String accnumber,int choice) {
        connectJDBC con=new connectJDBC();
        Account account=new Account();
        account=(Account)con.retrive(account,accnumber);
        float bal=account.getBalance();

        Transaction t=new Transaction();
        LocalDate depositeDate=java.time.LocalDate.now();
        if (choice==1)
        {
            String type="Deposit";
            t.updateTransaction(amount,depositeDate,type,accnumber,bal);
            System.out.printf("Your current Balance is: Rs.%.2f\n",bal);
        }
        else if(choice==2)
        {
            String type="Withdraw";
            t.updateTransaction(amount,depositeDate,type,accnumber,bal);
            System.out.printf("Your current Balance is: Rs.%.2f\n",bal);
        }
        else System.out.printf("Your current Balance is: Rs.%.2f\n",bal);
    }

    public void closeAccount(String accnumber){
        connectJDBC con=new connectJDBC();
        Account account=new Account();
        account=(Account)con.retrive(account,accnumber);
        System.out.println("Rs."+account.getBalance()+"withdraw Succesfully");
        account.setBalance(0);
        account.setStatus(0);
        con.update(account,accnumber);
        System.out.println("Account has been deActivated");
    }

    public void AccountDetails(String accnumber){
        connectJDBC con=new connectJDBC();
        Details sd = new Details();
        Account account=new Account();
        account=(Account)con.retrive(account,accnumber);
        sd=(Details)con.retrive(sd,account.getCustomeId());

        System.out.println("-------------------------------------- Your details------------------------------");
        System.out.println("Account Number      : "+accnumber);
        System.out.println("Account Name        : "+sd.getAccountName());
        System.out.println("Pan number          : "+sd.getPanNumber());
        System.out.println("Email id            : "+sd.getEmailId());
        System.out.println("Adress              : "+sd.getAddress());
        System.out.println("Date Of Birth       : "+sd.getDob());
        System.out.println("Phone Number        : "+sd.getPhoneNumber());
        System.out.println("Adhar Number        : "+sd.getAdharNumber());
        System.out.println("------------------------------------------------------------------------------------");

    }

}

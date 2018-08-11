import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        Validation v = new Validation();
        BankOperation b = new BankOperation();
        RecurringAccount r=new RecurringAccount();
        FixedAccount f=new FixedAccount();
        while (true) {
            System.out.println("\n------------------------");
            System.out.println("BANK    OF     FRESHERS");
            System.out.println("------------------------\n");
            System.out.println("*********WELCOME**********\n");
            System.out.println("Press 1-CreateAccount 2-Log in to existing account 3-exit");
            String str = sc.next();
            choice = v.choiceValidation(str);
            switch (choice)
            {
                case 1:
                    b.createAccount();
                    break;
                case 2:
                    String accountnum = b.checkAccount();
                    if (accountnum != null) {
                        String match = accountnum.substring(2, 4);
                        switch (match)
                        {
                            case "SB":
                            case "CA":
                                b.functionality(accountnum);
                                break;
                            case "RD":
                                r.recurringAccount(accountnum);
                                break;
                            case "FD":
                                f.fixedAccount(accountnum);
                                break;
                            default:
                                System.out.println("Invalid data\n");
                                break;
                        }
                    }
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice\n");
                    break;
            }
        }
    }
}


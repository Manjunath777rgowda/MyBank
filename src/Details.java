import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.text.DateFormat.getInstance;

@Entity
@Table(name="UserDetails")
public class Details {

    @Column(name = "customedId")
    private int customeId;

    @Column(name = "name")
    private String accountName;

    @Column(name = "DOB")
    private String dob;

    @Column(name = "adress")
    private String address;

    @Column(name = "adharNumber")
    private String adharNumber;

    @Column(name = "PANnumber")
    private String panNumber;

    @Column(name = "contactNumber")
    private String phoneNumber;

    @Id
    @Column(name = "email")
    private String emailId;

    public int getCustomeId() {
        return customeId;
    }

    public void setCustomeId(int customeId) {
        this.customeId = customeId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAdharNumber(String adharNumber) {
        this.adharNumber = adharNumber;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDob() { return dob;}

    public String getAdharNumber() {
        return adharNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAddress() {
        return address;
    }

    public String getInputDetails() {


        Validation v = new Validation();
        Scanner sc = new Scanner(System.in);
        String temp, temp1;
        boolean flag = false;
        boolean temp2=false;
        String accType = "";
        String numberAsString = "";
        Details sd = new Details();

        Random rand = new Random();
        int number = rand.nextInt(999) + 1000;
        float limit = 0;
        int choice = 0;

        do {
            System.out.println("Choose the account type you wish to create:( 1-Savings 2-Current 3-Recurring 4-Fixed)");

            temp = sc.nextLine();
            flag = v.typeValidation(temp);
            if (flag) {
                accType = temp;
                choice = Integer.parseInt(temp);
            } else System.out.println("enter valid account type");

            if (accType.equals("1")) {
                numberAsString = String.valueOf(number);
                numberAsString = "10SB" + numberAsString;
                //a.setAccountNumber(numberAsString);
            } else if (accType.equals("2")) {
                numberAsString = String.valueOf(number);
                numberAsString = "10CA" + numberAsString;
                //a.setAccountNumber(numberAsString);
                limit = 1000;
            } else if (accType.equals("3")) {
                numberAsString = String.valueOf(number);
                numberAsString = "10RD" + numberAsString;
                //a.setAccountNumber(numberAsString);
            } else if (accType.equals("4")) {
                numberAsString = String.valueOf(number);
                numberAsString = "10FD" + numberAsString;
                //setAccountNumber(numberAsString);
            }
        } while (!flag);

        do {
            System.out.println("Enter Account Holders Name( name should not be more than 20 characters and should not contain numbers or any special characters): ");
            temp1 = sc.nextLine();
            flag = v.nameValidation(temp1);
            if (flag) {
                sd.setAccountName(temp1);
            } else
                System.out.println("\nInvalid Name ( either name too long or contains invalid characters).....try again!!\n");
        } while (!flag);


        do {
            System.out.println("Enter pan number ( first 5 letters, next 4 digits followed by a letter):  ");
            temp = sc.nextLine();
            flag = v.panValidation(temp);
            if (flag) {
                temp = temp.toUpperCase();
                sd.setPanNumber(temp);
            } else System.out.println("Enter valid pan number");
        } while (!flag);

        do {
            System.out.println("Enter email id");
            temp = sc.next();
            flag = v.emailValidation(temp);
            if (flag) {
                sd.setEmailId(temp);
            } else System.out.println("Enter valid email id");
        } while (!flag);


        do {
            System.out.println("Enter date of birth (dd/mm/yyyy)");
            temp = sc.next();
            flag = v.dateValidation(temp);
            if (flag) {
                try {
                    sd.setDob(temp);
                } catch (Exception e) {
                    System.out.println("Invalid date");
                }
            } else System.out.println("Invalid date");
        } while (!flag);

        System.out.println("Enter address in the form of( house/flat no, street, city, state, country) : ");
        String address1 = sc.nextLine();
        do{
            Pattern pattern=Pattern.compile("^[#.0-9a-zA-Z\\s,-]+$");
            Matcher matcher=pattern.matcher(address1);
            if(matcher.matches()){
                temp2=true;
                sd.setAddress(address1);
            }
            else{
                address1 = sc.nextLine();
            }

        }while(!temp2);
        do {
            System.out.println("Enter Your 10 digit Phone Number");
            temp = sc.next();
            flag = v.PhoneNumberValidation(temp);
            if (flag) {
                sd.setPhoneNumber(temp);
            }
        } while (!flag);


        do {
            System.out.println("Enter your 12 digit Adhar Number");
            temp = sc.next();
            flag = v.AdharNumberValidation(temp);
            if (flag) {
                sd.setAdharNumber(temp);
            }
        } while (!flag);

         Account account = new Account();
        if (account.createAccount(sd, numberAsString, limit, choice)) {
            return numberAsString;
        } else
            return null;
    }


}


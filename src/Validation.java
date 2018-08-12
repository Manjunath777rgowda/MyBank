import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validation {
    private static Pattern dateFrmtPtrn =
            Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)");

    public int choiceValidation(String str) {
        try {

            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }


    public boolean PhoneNumberValidation(String str) {
        try{
            Pattern pattern=Pattern.compile("\\d{10}");
            Matcher matcher=pattern.matcher(str);
            if(matcher.matches()){
                return true;
            }
            else{
                System.out.println("Mobile number should be 10 digits");
                return false;
            }

        }catch(Exception e){
            System.out.println("Invalid mobile number!!!!") ;
            return false;
        }
    }

    public boolean typeValidation(String str) {
        if (str.matches("^([1-4])$")) {
            return true;
        }
        else return false;
    }

    public boolean accNoValidation(String str){
        if(str.matches("^[10]('SB'|'CA'|'RD'|'FD')[0-9][0-9][0-9][0-9]$")){
            return true;
        }
        else return false;
    }

    public boolean AdharNumberValidation(String str) {
        try{
            Pattern pattern=Pattern.compile("\\d{12}");
            Matcher matcher=pattern.matcher(str);
            if(matcher.matches()){
                return true;
            }
            else{
                System.out.println("adhar number should be 12 digits");
                return false;
            }

        }catch(Exception e){
            System.out.println("Invalid adhar number!!!!") ;
            return false;
        }
    }

    public boolean nameValidation(String str) {
        try{
            Pattern pattern=Pattern.compile("^[a-zA-Z][a-zA-Z-_\\.*\\s]{1,20}$");
            Matcher matcher=pattern.matcher(str);
            if(matcher.matches()){
                return true;
            }
            else{
                System.out.println("\n\nName did not meet conditions");
                return false;
            }

        }catch(Exception e){
            System.out.println("\n\nInvalid name!!!!") ;
            return false;
        }
    }

    public boolean dateValidation(String str)  {
        Matcher mtch = dateFrmtPtrn.matcher(str);
        if(mtch.matches()){
            return true;
        }
        return false;

    }

    public boolean passwordValidation(String str) {
        if(str.matches("^.{4,8}$")) {

            return true;
        }
        else return false;
    }

    public boolean panValidation(String str)    {
        if (str.matches("[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}")) {
            return true;
        }
        else return false;
    }

    public boolean emailValidation(String str) {
        if (str.matches("^([A-Za-z0-9._%+]*)[@]([A-Za-z]*)[.]([A-Za-z]*)$")) {
            return true;
        }
        else return false;
    }


    public boolean amountValidation(String str) {
        try{
            if (Float.parseFloat(str)>0 && Float.parseFloat(str)< Math.pow(10,20))
            {
                return true;
            }
            else
            {
                System.out.println("\n\nInvalid Amount");
                return false;
            }
        }
        catch(Exception e)
        {
            System.out.println("\n\nInvalid Amount");
            return false;
        }
    }
}
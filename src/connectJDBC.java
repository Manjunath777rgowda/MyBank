//import com.sun.istack.internal.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.util.List;

import static java.util.logging.Level.OFF;

public class connectJDBC {

    public Session getSession(Object o){

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(o.getClass())
                .buildSessionFactory();

        Session session = factory.getCurrentSession();
        return session;

    }

    public boolean insert(Object o) {

        Session session = getSession(o);

        try {
            session.beginTransaction();
           session.save(o);
            session.getTransaction().commit();
          //  System.out.println("done!!");
            return true;
        }

        catch (Exception e) {
           // System.out.println("Error while connecting");
            e.printStackTrace();
            return false;
        }

    }

    public Object retrive(Object o,String accountNumber) {


        Session session = getSession(o);

        try {
            session.beginTransaction();
            Object d=new Object();
            d=(Object)session.get(o.getClass(),accountNumber);
            session.getTransaction().commit();
            return d;
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Object retrive(Object o,int customerId) {


        Session session = getSession(o);

        try {
            session.beginTransaction();
            Object d=new Object();
            Query query =  session.createQuery("Select d FROM Details d where customeId=?1").setParameter(1,customerId);
            List<RecurringAccount> results = ((org.hibernate.query.Query) query).list();

            session.getTransaction().commit();
            return results.get(0);
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void statement(String acconum,Object o) {


        Session session = getSession(o);

        try {
            session.beginTransaction();
            Query query =  session.createQuery("Select t FROM Transaction t where accountNumber=?1").setParameter(1,acconum);
            List<Transaction> results = ((org.hibernate.query.Query) query).list();
            System.out.println("--------------------------STATEMENT------------------------------------------");
            System.out.println("Date          | Transaction Type |           Amount          |           NewBalance ");
            for (Transaction t: results
            ) {

                System.out.println(t.getTransactionDate()+"\t\t\t"+t.getTransactionType()+"\t\t\t\t"+String.format("%.2f",t.getTransactionAmount())+"\t\t\t\t"+String.format("%.2f",t.getBalance()));
            }
            System.out.println("------------------------------------------------------------------------------------");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showAllRD(String accountNumber,Object o) {


        Session session = getSession(o);

        try {
            session.beginTransaction();
            Query query = session.createQuery("Select rd FROM RecurringAccount  rd where accountNumber=?1 AND status=1").setParameter(1, accountNumber);
            List<RecurringAccount> results = ((org.hibernate.query.Query) query).list();

            if (!results.isEmpty()) {

                System.out.println("--------------------------YOUR RD DETAILS----------------------------------------");
                System.out.println("ReferenceNumnber | CreationDate | DepositePerios | initialDeposite | NewBalance ");
                for (RecurringAccount rd : results
                ) {

                    if (rd.getStatus() == 0) {
                        continue;
                    } else

                        System.out.println(rd.getReferenceNumber() + "\t\t\t\t" + rd.getCreationDate() + "\t\t\t" + rd.getDepositPeriod() + "\t\t\t " + String.format("%.2f",rd.getInitialDeposit()) + "\t\t  " + String.format("%.2f",rd.getBalance()));
                }
                System.out.println("------------------------------------------------------------------------------------");
            }

            else System.out.println("Sorry!!!!! ------You dont have any RD----");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showAllFD(String accountNumber,Object o)  {


        Session session = getSession(o);
        try {
            session.beginTransaction();

            Query query = session.createQuery("Select fd FROM FixedAccount fd where accountNumber=?1 AND status=1").setParameter(1, accountNumber);
            List<FixedAccount> results = ((org.hibernate.query.Query) query).list();

            if (!results.isEmpty()) {
                System.out.println("--------------------------YOUR FD DETAILS----------------------------------------");
                System.out.println("ReferenceNumnber | CreationDate | DepositePeriod | DepositeAmount ");
                for (FixedAccount fd : results
                ) {
                    if (fd.getStatus() == 0) {
                        continue;
                    } else

                        System.out.println("\t" + fd.getRefNum() + "\t\t\t" + fd.getDepositDate() + "\t\t" + fd.getDepositPeriod() + "\t\t\t\t" + fd.getAccountBalance());
                }
                System.out.println("------------------------------------------------------------------------------------");
            } else System.out.println("Sorry!!!!! ------You dont have any FD----");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RecurringAccount retriveRD(Object o,int accountNumber) {


        Session session = getSession(o);


        try {
            session.beginTransaction();
            RecurringAccount d=new RecurringAccount();
            d=(RecurringAccount) session.get(o.getClass(),accountNumber);
            // session.save(o);
            session.getTransaction().commit();
            //System.out.println("done!!");
            return d;
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public FixedAccount retriveFD(Object o,int accountNumber) {


        Session session =getSession(o);

        try {
            session.beginTransaction();
            FixedAccount d=new FixedAccount();
            d=(FixedAccount) session.get(o.getClass(),accountNumber);
            // session.save(o);
            session.getTransaction().commit();
            //System.out.println("done!!");
            return d;
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Details update(Object o,String accountNumber) {


        Session session = getSession(o);

        try {
            session.beginTransaction();
            Details d=new Details();
            session.update(o);
            session.getTransaction().commit();
            //System.out.println("done!!");
            return d;
        }

        catch (Exception e) {
             e.printStackTrace();
            //System.out.println("error");
            return null;
        }
    }

    public RecurringAccount update(Object o,int accountNumber) {

        Session session = getSession(o);

        try {
            session.beginTransaction();
            RecurringAccount d=new RecurringAccount();
            session.update(o);
            session.getTransaction().commit();
            //System.out.println("done!!");
            return d;
        }

        catch (Exception e) {
             e.printStackTrace();
            //System.out.println("error");
            return null;
        }

    }


}
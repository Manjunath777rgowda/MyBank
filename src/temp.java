import com.sun.javafx.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class temp {

    public static void main(String[] args)
    {


        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Details.class)
                .buildSessionFactory();
        //create session
        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();
            Details d=new Details();
             d=(Details)session.get(Details.class,"");
            // session.save(o);
            session.getTransaction().commit();
            //System.out.println("done!!");
           // return d;
        }

        catch (Exception e) {
            e.printStackTrace();
           // return null;
        }

        finally {
            factory.close();
        }

    }

    }

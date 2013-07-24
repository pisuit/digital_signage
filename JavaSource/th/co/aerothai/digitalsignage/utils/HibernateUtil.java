package th.co.aerothai.digitalsignage.utils;

import java.util.Locale;

import org.hibernate.*;
import org.hibernate.cfg.*;

public abstract class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final SessionFactory sessionFactoryPR;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	Locale.setDefault(Locale.US);
            //sessionFactory = new Configuration().configure().buildSessionFactory();
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            sessionFactoryPR = new Configuration().configure("hibernate_pr.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static SessionFactory getSessionFactoryPR() {
        return sessionFactoryPR;
    }

}
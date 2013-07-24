package th.co.aerothai.digitalsignage.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import th.co.aerothai.digitalsignage.model.SlideShow;
import th.co.aerothai.digitalsignage.model.User;
import th.co.aerothai.digitalsignage.utils.HibernateUtil;

public abstract class UserDao {
	public static void saveUser(User user){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(user);
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static void deleteUser(User user){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.delete(user);
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static List<User> getUsers(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<User> userList = session.createQuery(
					"SELECT user " +
					"from User user ")
					.list();
			
			tx.commit();
			
			return userList;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static User findUsers(String username, String password){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			User user = (User) session.createQuery(
					"SELECT user " +
					"from User user " +
					"where user.username = :pusername " +
					"and user.password = :ppassword")
					.setParameter("pusername", username)
					.setParameter("ppassword", password)
					.uniqueResult();
			
			tx.commit();
			
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
}

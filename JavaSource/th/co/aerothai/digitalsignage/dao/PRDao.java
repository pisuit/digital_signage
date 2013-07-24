package th.co.aerothai.digitalsignage.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import th.co.aerothai.digitalsignage.model.Profile;
import th.co.aerothai.digitalsignage.model.pr.ActiPrsc;
import th.co.aerothai.digitalsignage.model.pr.PrNews;
import th.co.aerothai.digitalsignage.utils.HibernateUtil;

public class PRDao {
	@SuppressWarnings("unchecked")
	public static List<ActiPrsc> getActivity(){
		SessionFactory sf = HibernateUtil.getSessionFactoryPR();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<ActiPrsc> pr = session.createQuery(
					"SELECT pr " +
					"FROM ActiPrsc pr " +
					"left join fetch pr.actiDateSelect actidate " +
					"left join fetch pr.actiOrganization actiorg " +
					"WHERE actiorg.act_date = current_date " +
					"AND actiorg.act_date = actidate.act_date " +
					"AND actidate.delStatus = 'n' " +
					"AND pr.act_prsctype = 'act' " +
					"ORDER BY actiorg.act_start, pr.act_ordshow")
					.list();
			
			tx.commit();
			
			return pr;
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
	
	@SuppressWarnings("unchecked")
	public static List<ActiPrsc> getPr(){
		SessionFactory sf = HibernateUtil.getSessionFactoryPR();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<ActiPrsc> pr = session.createQuery(
					"SELECT pr " +
					"FROM ActiPrsc pr " +
					"left join fetch pr.actiDateSelect actidate " +
					"left join fetch pr.actiPr actipr " +
					"WHERE actidate.act_date = current_date " +
					"AND actidate.delStatus = 'n' " +
					"AND pr.act_prsctype = 'pr' " +
					"ORDER BY pr.pr_ordshow ")
					.list();
			
			tx.commit();
			
			return pr;
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
	
	@SuppressWarnings("unchecked")
	public static List<String> getPrNews(){
		SessionFactory sf = HibernateUtil.getSessionFactoryPR();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> pr = session.createQuery(
					"SELECT news.news_imagerename " +
					"FROM PrNews news " +
					"WHERE news.news_durationsto >= current_date " +
					"AND news.news_status = 1 " +
					"ORDER BY news.news_durationsfrom desc, news.news_durationsto desc")
					.list();
			
			tx.commit();
			
			return pr;
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

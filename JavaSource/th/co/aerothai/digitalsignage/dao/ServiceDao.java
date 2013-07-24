package th.co.aerothai.digitalsignage.dao;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import th.co.aerothai.digitalsignage.model.ClientProfile;
import th.co.aerothai.digitalsignage.model.Feed;
import th.co.aerothai.digitalsignage.model.Image;
import th.co.aerothai.digitalsignage.model.LayoutProfile;
import th.co.aerothai.digitalsignage.model.Panel;
import th.co.aerothai.digitalsignage.model.Layout;
import th.co.aerothai.digitalsignage.model.Profile;
import th.co.aerothai.digitalsignage.model.SlideShow;
import th.co.aerothai.digitalsignage.model.Text;
import th.co.aerothai.digitalsignage.model.Video;
import th.co.aerothai.digitalsignage.model.Web;
import th.co.aerothai.digitalsignage.utils.HibernateUtil;

public abstract class ServiceDao {
	public static void deleteClientProfile(ClientProfile profile){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.delete(profile);
			
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
	
	public static void saveProfile(Profile profile, List<LayoutProfile> layoutProfiles){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if(profile.getId() != null){
				if(profile.getLayoutProfiles().size() != 0){
					for(LayoutProfile layoutProfile : profile.getLayoutProfiles()){
						session.delete(layoutProfile);
					}
				}
			}
			
			session.saveOrUpdate(profile);
			
			for(LayoutProfile layoutProfile : layoutProfiles){
				layoutProfile.setId(null);
				layoutProfile.setProfile(profile);
				session.saveOrUpdate(layoutProfile);
			}
			
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
	
	@SuppressWarnings("unchecked")
	public static List<LayoutProfile> getLayoutProfileListForProfile(Profile profile){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<LayoutProfile> layoutProfiles = session.createQuery(
					"SELECT distinct laypro " +
					"FROM LayoutProfile laypro " +
					"left join fetch laypro.layout layout " +
					"left join fetch layout.panels panel " +
					"left join fetch panel.texts " +
					"left join fetch panel.videos " +
					"left join fetch panel.images " +
					"left join fetch panel.webs " +
					"left join fetch panel.slideShows " +
					"left join fetch panel.feeds " +
					"WHERE laypro.profile = :pprofile " +
					"ORDER BY laypro.ordering")
					.setParameter("pprofile", profile)
					.list()	;
			
			tx.commit();
			
			return layoutProfiles;
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
	
	public static Profile getProfile(Long id){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Profile profile = (Profile) session.createQuery(
					"SELECT distinct profile " +
					"FROM Profile profile " +
					"left join fetch profile.layoutProfiles laypro " +
					"left join fetch laypro.layout " +
					"WHERE profile.id = :pid ")
					.setParameter("pid", id)
					.uniqueResult();
			
			tx.commit();
			
			return profile;
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
	
	public static void deleteProfile(Profile profile){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			for(LayoutProfile layoutProfile : profile.getLayoutProfiles()){
				session.delete(layoutProfile);
			}
			
			session.delete(profile);
			
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
	
	public static void deleteLayout(Layout layout){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if(layout.getPanels().size() != 0){
				for(Panel panel : layout.getPanels()){
					if(panel.getImages().size() != 0){
						for(Image image : panel.getImages()){
							session.delete(image);
						}
					}
					if(panel.getTexts().size() != 0){
						for(Text text : panel.getTexts()){
							session.delete(text);
						}
					}
					if(panel.getVideos().size() != 0){
						for(Video video : panel.getVideos()){
							session.delete(video);
						}
					}
					if(panel.getWebs().size() != 0){
						for(Web web : panel.getWebs()){
							session.delete(web);
						}
					}
					session.delete(panel);
				}
			}
			session.delete(layout);
			
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
	
	public static void updateLayout(Layout layout){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if(layout.getPanels().size() != 0){
				for(Panel panel : layout.getPanels()){
					if(panel.getImages().size() != 0){
						for(Image image : panel.getImages()){
							session.delete(image);
						}
					}
					if(panel.getTexts().size() != 0){
						for(Text text : panel.getTexts()){
							session.delete(text);
						}
					}
					if(panel.getVideos().size() != 0){
						for(Video video : panel.getVideos()){
							session.delete(video);
						}
					}
					if(panel.getWebs().size() != 0){
						for(Web web : panel.getWebs()){
							session.delete(web);
						}
					}
					if(panel.getFeeds().size() != 0){
						for(Feed feed : panel.getFeeds()){
							session.delete(feed);
						}
					}
					if(panel.getSlideShows().size() != 0){
						for(SlideShow slide : panel.getSlideShows()){
							session.delete(slide);
						}
					}
					session.delete(panel);
				}
			}
			
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
	
	public static Layout saveLayout(Layout layout){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(layout);
			
			tx.commit();
			
			return layout;
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
	
	public static void saveFeed(Feed feed){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(feed);
			
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
	
	public static List<Object[]> executeTerminal(String sql){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Object[]> result = session.createSQLQuery(sql).list();
			
			tx.commit();
			
			return result;
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
	
	public static void saveSlideShow(SlideShow slideShow){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(slideShow);
			
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
	
	public static void saveWeb(Web web){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(web);
			
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
	
	public static Panel savePanel(Panel panel){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(panel);
			
			tx.commit();
			
			return panel;
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
	
	public static void saveText(Text text){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(text);
			
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
	
	public static void saveImage(Image image){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(image);
			
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
	
	public static void saveVideo(Video video){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(video);
			
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
	
	public static Layout getLayout(String name){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Layout layout = (Layout) session.createQuery(
					"SELECT layout " +
					"FROM Layout layout " +
					"left join fetch layout.panels panel " +
					"left join fetch panel.texts " +
					"left join fetch panel.videos " +
					"left join fetch panel.images " +
					"left join fetch panel.webs " +
					"left join fetch panel.slideShows " +
					"left join fetch panel.feeds " +
					"where layout.name = :pname")
					.setParameter("pname", name)
					.uniqueResult();
					
			tx.commit();
			return layout;
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
	
	public static Layout getLayout(Long id){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Layout layout = (Layout) session.createQuery(
					"SELECT layout " +
					"FROM Layout layout " +
					"left join fetch layout.panels panel " +
					"left join fetch panel.texts " +
					"left join fetch panel.videos " +
					"left join fetch panel.images " +
					"left join fetch panel.webs " +
					"left join fetch panel.slideShows " +
					"left join fetch panel.feeds " +
					"where layout.id = :pid")
					.setParameter("pid", id)
					.uniqueResult();
					
			tx.commit();
			return layout;
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
	public static List<Layout> getLayoutList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Layout> layouts = session.createQuery(
					"SELECT distinct layout " +
					"FROM Layout layout " +
					"left join fetch layout.panels panel " +
					"left join fetch panel.texts " +
					"left join fetch panel.videos " +
					"left join fetch panel.webs " +
					"left join fetch panel.slideShows " +
					"left join fetch panel.feeds " +
					"left join fetch panel.images ")
					.list();
					
			tx.commit();
			return layouts;
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
	
	public static List<ClientProfile> getClientProfileList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<ClientProfile> profiles = session.createQuery(
					"SELECT distinct profile " +
					"FROM ClientProfile profile ")
					.list();
					
			tx.commit();
			return profiles;
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
	
	public static ClientProfile getClientProfileByDisplay(String display){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			ClientProfile profiles = (ClientProfile) session.createQuery(
					"SELECT distinct client " +
					"FROM ClientProfile client " +
					"left join fetch client.layout layout " +
					"left join fetch client.profile profile " +
					"left join fetch profile.layoutProfiles laypro " +
					"left join fetch layout.panels panel " +
					"left join fetch panel.texts " +
					"left join fetch panel.videos " +
					"left join fetch panel.images " +
					"left join fetch panel.webs " +
					"left join fetch panel.slideShows " +
					"left join fetch panel.feeds " +
					"left join fetch laypro.layout lay " +
					"left join fetch lay.panels pan " +
					"left join fetch pan.texts " +
					"left join fetch pan.videos " +
					"left join fetch pan.images " +
					"left join fetch pan.webs " +
					"left join fetch pan.slideShows " +
					"left join fetch pan.feeds " +
					"WHERE client.clientName = :pname")
					.setParameter("pname", display)
					.uniqueResult();
					
			tx.commit();
			
			if(profiles != null && profiles.getProfile() != null){
				Map<Long, LayoutProfile> map = new LinkedHashMap<Long, LayoutProfile>();
				for(LayoutProfile layoutProfile : profiles.getProfile().getLayoutProfiles()){
					map.put(layoutProfile.getId(), layoutProfile);
				}
				profiles.getProfile().getLayoutProfiles().clear();
				profiles.getProfile().getLayoutProfiles().addAll(map.values());
			}
			
			return profiles;
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
	public static List<Profile> getProfileList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Profile> profiles = session.createQuery(
					"SELECT distinct profile " +
					"FROM Profile profile " +
					"left join fetch profile.layoutProfiles laypro " +
					"left join fetch laypro.layout " +
					"ORDER BY profile.name, laypro.ordering ")
					.list();
					
			tx.commit();
			return profiles;
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
	
	public static void saveClientProfile(ClientProfile profile){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(profile);
			
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
	
	public static void duplicateDisplayData(ClientProfile profile){
		Session session = null;
		SessionFactory sf = null;
		Transaction tx = null;
		
		try{
			Configuration configuration = new AnnotationConfiguration().configure("dynamic_hibernate.cfg.xml");
			configuration.setProperty("hibernate.connection.url", "jdbc:mysql://"+profile.getConnectionName()+":3306/digital_signage");
			sf = configuration.buildSessionFactory();
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.createSQLQuery("delete from clientprofile").executeUpdate();
			session.createSQLQuery("delete from layoutprofile").executeUpdate();
			session.createSQLQuery("delete from feed").executeUpdate();
			session.createSQLQuery("delete from image").executeUpdate();
			session.createSQLQuery("delete from layout").executeUpdate();
			session.createSQLQuery("delete from panel").executeUpdate();
			session.createSQLQuery("delete from profile").executeUpdate();
			session.createSQLQuery("delete from slideshow").executeUpdate();
			session.createSQLQuery("delete from text").executeUpdate();
			session.createSQLQuery("delete from video").executeUpdate();
			session.createSQLQuery("delete from web").executeUpdate();
			
			if(profile.getLayout() != null){
				session.save(profile.getLayout());
				if(profile.getLayout().getBgImage() != null){
					duplicateFile(profile.getConnectionName(), "image", profile.getLayout().getBgImage());
				}
				
				profile.setLayout(profile.getLayout());		
				
				for(Panel panel : profile.getLayout().getPanels()){
					panel.setLayout(profile.getLayout());
					session.save(panel);
					
					if(panel.getFeeds().size() != 0){
						panel.getFeeds().iterator().next().setPanel(panel);
						session.save(panel.getFeeds().iterator().next());
					}
					if(panel.getImages().size() != 0){
						panel.getImages().iterator().next().setPanel(panel);
						session.save(panel.getImages().iterator().next());
						if(panel.getImages().iterator().next().getUrl().substring(0, 7).equals("/image/")){
							duplicateFile(profile.getConnectionName(), "image", panel.getImages().iterator().next().getUrl().substring(7, panel.getImages().iterator().next().getUrl().length()));
						}
					}
					if(panel.getSlideShows().size() != 0){
						panel.getSlideShows().iterator().next().setPanel(panel);
						session.save(panel.getSlideShows().iterator().next());
						for(String s : panel.getSlideShows().iterator().next().getImages().split(";")){
							duplicateFile(profile.getConnectionName(), "image", s);
						}
					}
					if(panel.getTexts().size() != 0){
						panel.getTexts().iterator().next().setPanel(panel);
						session.save(panel.getTexts().iterator().next());
					}
					if(panel.getVideos().size() != 0){
						panel.getVideos().iterator().next().setPanel(panel);
						session.save(panel.getVideos().iterator().next());
						if(panel.getVideos().iterator().next().isLocal()){
							duplicateFile(profile.getConnectionName(), "video", panel.getVideos().iterator().next().getValue());
						}
					}
					if(panel.getWebs().size() != 0){
						panel.getWebs().iterator().next().setPanel(panel);
						session.save(panel.getWebs().iterator().next());
					}
				}
			}
			if(profile.getProfile() != null){
				session.save(profile.getProfile());
				
				profile.setProfile(profile.getProfile());

				for(LayoutProfile layoutProfile : profile.getProfile().getLayoutProfiles()){
					layoutProfile.setProfile(profile.getProfile());			
					session.save(layoutProfile.getLayout());
					layoutProfile.setLayout(layoutProfile.getLayout());
					session.save(layoutProfile);
					
					if(layoutProfile.getLayout().getBgImage() != null){
						duplicateFile(profile.getConnectionName(), "image", layoutProfile.getLayout().getBgImage());
					}

					for(Panel panel : layoutProfile.getLayout().getPanels()){
						panel.setLayout(layoutProfile.getLayout());
						session.save(panel);
						
						if(panel.getFeeds().size() != 0){
							panel.getFeeds().iterator().next().setPanel(panel);
							session.save(panel.getFeeds().iterator().next());
						}
						if(panel.getImages().size() != 0){
							panel.getImages().iterator().next().setPanel(panel);
							session.save(panel.getImages().iterator().next());
							if(panel.getImages().iterator().next().getUrl().substring(0, 7).equals("/image/")){
								duplicateFile(profile.getConnectionName(), "image", panel.getImages().iterator().next().getUrl().substring(7, panel.getImages().iterator().next().getUrl().length()));
							}
						}
						if(panel.getSlideShows().size() != 0){
							panel.getSlideShows().iterator().next().setPanel(panel);
							session.save(panel.getSlideShows().iterator().next());
							for(String s : panel.getSlideShows().iterator().next().getImages().split(";")){
								duplicateFile(profile.getConnectionName(), "image", s);
							}
						}
						if(panel.getTexts().size() != 0){
							panel.getTexts().iterator().next().setPanel(panel);
							session.save(panel.getTexts().iterator().next());
						}
						if(panel.getVideos().size() != 0){
							panel.getVideos().iterator().next().setPanel(panel);
							session.save(panel.getVideos().iterator().next());
							if(panel.getVideos().iterator().next().isLocal()){
								duplicateFile(profile.getConnectionName(), "video", panel.getVideos().iterator().next().getValue());
							}
						}
						if(panel.getWebs().size() != 0){
							panel.getWebs().iterator().next().setPanel(panel);
							session.save(panel.getWebs().iterator().next());
						}
					}
				}
			}
			
			session.save(profile);
			
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
	
	private static void duplicateFile(String ip, String type, String fileName){     
	     try {
	    	 HttpClient client = new DefaultHttpClient();
	    	 URI address = new URI("http", null, ip, 8082, "/"+type, null, null);
	    	 HttpPost post = new HttpPost(address);
	 	    
		     MultipartEntity entity = new MultipartEntity();
		     entity.addPart("file", new FileBody(new File("C:/"+type+"/"+fileName)));
		     post.setEntity(entity);
		     HttpResponse response = client.execute(post);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
}

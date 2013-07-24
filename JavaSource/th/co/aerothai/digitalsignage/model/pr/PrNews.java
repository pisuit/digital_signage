package th.co.aerothai.digitalsignage.model.pr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="pr_news")
public class PrNews {
	
	@Id
	@Column(name="news_id")
	private Long news_id;
	
	@Column(name="news_topic")
	private String news_topic;
	
	@Column(name="news_durationsfrom")
	private Date news_durationsfrom;
	
	@Column(name="news_durationsto")
	private Date news_durationsto;
	
	@Column(name="news_content")
	private String news_content;
	
	@Column(name="news_source")
	private String news_source;
	
	@Column(name="news_image")
	private String news_image;
	
	@Column(name="news_imagerename")
	private String news_imagerename;
	
	@Column(name="news_status")
	private int news_status;
	
	@Column(name="news_user")
	private String news_user;
	
	@Column(name="news_date")
	private Date news_date;

	public Long getNews_id() {
		return news_id;
	}

	public void setNews_id(Long news_id) {
		this.news_id = news_id;
	}

	public String getNews_topic() {
		return news_topic;
	}

	public void setNews_topic(String news_topic) {
		this.news_topic = news_topic;
	}

	public Date getNews_durationsfrom() {
		return news_durationsfrom;
	}

	public void setNews_durationsfrom(Date news_durationsfrom) {
		this.news_durationsfrom = news_durationsfrom;
	}

	public Date getNews_durationsto() {
		return news_durationsto;
	}

	public void setNews_durationsto(Date news_durationsto) {
		this.news_durationsto = news_durationsto;
	}

	public String getNews_content() {
		return news_content;
	}

	public void setNews_content(String news_content) {
		this.news_content = news_content;
	}

	public String getNews_source() {
		return news_source;
	}

	public void setNews_source(String news_source) {
		this.news_source = news_source;
	}

	public String getNews_image() {
		return news_image;
	}

	public void setNews_image(String news_image) {
		this.news_image = news_image;
	}

	public String getNews_imagerename() {
		return news_imagerename;
	}

	public void setNews_imagerename(String news_imagerename) {
		this.news_imagerename = news_imagerename;
	}

	public int getNews_status() {
		return news_status;
	}

	public void setNews_status(int news_status) {
		this.news_status = news_status;
	}

	public String getNews_user() {
		return news_user;
	}

	public void setNews_user(String news_user) {
		this.news_user = news_user;
	}

	public Date getNews_date() {
		return news_date;
	}

	public void setNews_date(Date news_date) {
		this.news_date = news_date;
	}
}

package th.co.aerothai.digitalsignage.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="PANEL")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Panel {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="PANELID")
	private String panelId;
	
	@Column(name="WIDTH")
	private String width = "300";
	
	@Column(name="HEIGHT")
	private String height = "300";
	
	@Column(name="XPOSITION")
	private String xPosition = "0";
	
	@Column(name="YPOSITION")
	private String yPosition = "0";
	
	@Column(name="ZPOSITION")
	private String zPosition = "0";
	
	@ManyToOne
	@JoinColumn(name="LAYOUT_ID", referencedColumnName="ID")
	private Layout layout;
	
	@OneToMany(mappedBy="panel")
	private Set<Image> images;
	
	@OneToMany(mappedBy="panel")
	private Set<Video> videos;
	
	@OneToMany(mappedBy="panel")
	private Set<Text> texts;
	
	@OneToMany(mappedBy="panel")
	private Set<Web> webs; 
	
	@OneToMany(mappedBy="panel")
	private Set<SlideShow> slideShows;
	
	@OneToMany(mappedBy="panel")
	private Set<Feed> feeds;
	
	@Column(name="ISPRNEWS")
	private boolean isPrNews;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPanelId() {
		return panelId;
	}
	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}
	public Set<Image> getImages() {
		return images;
	}
	public void setImages(Set<Image> images) {
		this.images = images;
	}
	public Set<Video> getVideos() {
		return videos;
	}
	public void setVideos(Set<Video> videos) {
		this.videos = videos;
	}
	public Set<Text> getTexts() {
		return texts;
	}
	public void setTexts(Set<Text> texts) {
		this.texts = texts;
	}
	public Set<Web> getWebs() {
		return webs;
	}
	public void setWebs(Set<Web> webs) {
		this.webs = webs;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getxPosition() {
		return xPosition;
	}
	public void setxPosition(String xPosition) {
		this.xPosition = xPosition;
	}
	public String getyPosition() {
		return yPosition;
	}
	public void setyPosition(String yPosition) {
		this.yPosition = yPosition;
	}
	public String getzPosition() {
		return zPosition;
	}
	public void setzPosition(String zPosition) {
		this.zPosition = zPosition;
	}
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	public Set<SlideShow> getSlideShows() {
		return slideShows;
	}
	public void setSlideShows(Set<SlideShow> slideShows) {
		this.slideShows = slideShows;
	}
	public Set<Feed> getFeeds() {
		return feeds;
	}
	public void setFeeds(Set<Feed> feeds) {
		this.feeds = feeds;
	}
	public boolean isPrNews() {
		return isPrNews;
	}
	public void setPrNews(boolean isPrNews) {
		this.isPrNews = isPrNews;
	}
}

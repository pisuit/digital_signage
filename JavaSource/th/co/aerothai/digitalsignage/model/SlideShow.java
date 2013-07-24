package th.co.aerothai.digitalsignage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SLIDESHOW")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class SlideShow {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="IMAGES")
	private String images;
	
	@Column(name="TIMING")
	private int interval;
	
	@ManyToOne
	@JoinColumn(name="PANEL_ID", referencedColumnName="ID")
	private Panel panel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}
}

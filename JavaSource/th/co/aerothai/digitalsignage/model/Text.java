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
@Table(name="TEXT")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Text {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="VALUE")
	private String value;
	
	@Column(name="ISSCROLL")
	private boolean isScrolling = false;
	
	@Column(name="SCROLLDIRECTION")
	private String scrollingDirection = "left";
	
	@Column(name="SCROLLSPEED")
	private int scrollingSpeed = 5;
	
	@ManyToOne
	@JoinColumn(name="PANEL_ID", referencedColumnName="ID")
	private Panel panel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	public boolean isScrolling() {
		return isScrolling;
	}

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	public String getScrollingDirection() {
		return scrollingDirection;
	}

	public void setScrollingDirection(String scrollingDirection) {
		this.scrollingDirection = scrollingDirection;
	}

	public int getScrollingSpeed() {
		return scrollingSpeed;
	}

	public void setScrollingSpeed(int scrollingSpeed) {
		this.scrollingSpeed = scrollingSpeed;
	}
}

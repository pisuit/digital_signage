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
@Table(name="VIDEO")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Video {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="VALUE")
	private String value;
	
	@Column(name="ISLOCAL")
	private boolean isLocal = true;
	
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

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
}

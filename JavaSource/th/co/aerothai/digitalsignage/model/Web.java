package th.co.aerothai.digitalsignage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="WEB")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Web implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="VALUE")
	private String value;
	
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
}

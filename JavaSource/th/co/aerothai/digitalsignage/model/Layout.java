package th.co.aerothai.digitalsignage.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="LAYOUT")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Layout implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="MODIFIED_TIME")
	private Date modifiedTime;
	
	@Column(name="WIDTH")
	private String width;
	
	@Column(name="HEIGHT")
	private String height;
	
	@Column(name="BGCOLOR")
	private String bgColor;
	
	@Column(name="BGIMAGE")
	private String bgImage;
	
	@OneToMany(mappedBy="layout")
	private Set<Panel> panels;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	public Set<Panel> getPanels() {
		return panels;
	}
	public void setPanels(Set<Panel> panels) {
		this.panels = panels;
	}
	public String getBgImage() {
		return bgImage;
	}
	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}
}

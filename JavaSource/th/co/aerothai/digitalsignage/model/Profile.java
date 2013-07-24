package th.co.aerothai.digitalsignage.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PROFILE")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Profile implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="MODIFIEDTIME")
	private Date modifiedTime;
	
	@OneToMany(mappedBy="profile")
	private List<LayoutProfile> layoutProfiles;

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

	public List<LayoutProfile> getLayoutProfiles() {
		return layoutProfiles;
	}

	public void setLayoutProfiles(List<LayoutProfile> layoutProfiles) {
		this.layoutProfiles = layoutProfiles;
	}
}

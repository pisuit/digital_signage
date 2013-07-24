package th.co.aerothai.digitalsignage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name="CLIENTPROFILE")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class ClientProfile {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="CLIENT_NAME")
	private String clientName;
	
	@ManyToOne
	@JoinColumn(name="LAYOUT_ID", referencedColumnName="ID")
	@NotFound(action=NotFoundAction.IGNORE)
	private Layout layout;
	
	@ManyToOne
	@JoinColumn(name="PROFILE_ID", referencedColumnName="ID")
	@NotFound(action=NotFoundAction.IGNORE)
	private Profile profile;
	
	@Column(name="IS_LOCAL_HOST")
	private boolean isLocalHost = false;
	
	@Column(name="CONNECTION_NAME")
	private String connectionName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public boolean isLocalHost() {
		return isLocalHost;
	}
	public void setLocalHost(boolean isLocalHost) {
		this.isLocalHost = isLocalHost;
	}
	public String getConnectionName() {
		return connectionName;
	}
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
}

package th.co.aerothai.digitalsignage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user")
@GenericGenerator(strategy="th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class User implements Serializable{
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
}

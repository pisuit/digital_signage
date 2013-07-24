package th.co.aerothai.digitalsignage.model.pr;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="acti_organization ")
public class ActiOrganization implements Serializable{
	
	@Id
	@Column(name="act_id")
	private Long act_id;
	
	@Column(name="sequence")
	private Integer sequence;
	
	@Column(name="act_date")
	private Date act_date;
	
	@Column(name="act_start")
	private Date act_start;
	
	@Column(name="act_end")
	private Date act_end;

	public Long getAct_id() {
		return act_id;
	}

	public void setAct_id(Long act_id) {
		this.act_id = act_id;
	}

	public Date getAct_date() {
		return act_date;
	}

	public void setAct_date(Date act_date) {
		this.act_date = act_date;
	}

	public Date getAct_start() {
		return act_start;
	}

	public void setAct_start(Date act_start) {
		this.act_start = act_start;
	}

	public Date getAct_end() {
		return act_end;
	}

	public void setAct_end(Date act_end) {
		this.act_end = act_end;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}

package th.co.aerothai.digitalsignage.model.pr;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="acti_dateselect")
public class ActiDateSelect implements Serializable{
	
	@Id
	@Column(name="act_dsID")
	private Long act_dsID;
	
	@Column(name="act_date")
	private Date act_date;
	
	@Column(name="createBy")
	private Integer createBy;
	
	@Column(name="createDate")
	private Date createDate;
	
	@Column(name="act_prscstatus")
	private String act_prscstatus;
	
	@Column(name="delStatus")
	private String delStatus;
	
	@Column(name="delBy")
	private Integer delBy;

	public Long getAct_dsID() {
		return act_dsID;
	}

	public void setAct_dsID(Long act_dsID) {
		this.act_dsID = act_dsID;
	}

	public Date getAct_date() {
		return act_date;
	}

	public void setAct_date(Date act_date) {
		this.act_date = act_date;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAct_prscstatus() {
		return act_prscstatus;
	}

	public void setAct_prscstatus(String act_prscstatus) {
		this.act_prscstatus = act_prscstatus;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getDelBy() {
		return delBy;
	}

	public void setDelBy(Integer delBy) {
		this.delBy = delBy;
	}

}

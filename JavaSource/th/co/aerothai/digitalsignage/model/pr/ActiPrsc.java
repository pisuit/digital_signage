package th.co.aerothai.digitalsignage.model.pr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.internal.Nullable;


@Entity
@Table(name="acti_prsc ")
public class ActiPrsc {
	
	@Id
	@Column(name="act_prscID")
	private Long act_prscID;
	
	@ManyToOne
	@JoinColumn(name="act_dsID", referencedColumnName="act_dsID")
	private ActiDateSelect actiDateSelect;
	
	@ManyToOne
	@JoinColumn(name="act_id", referencedColumnName="act_id")
	private ActiOrganization actiOrganization;
	
	@ManyToOne
	@JoinColumn(name="pr_id", referencedColumnName="pr_id")
	private ActiPr actiPr;
	
	@Column(name="act_prsctype")
	private String act_prsctype;
	
	@Column(name="act_detail")
	private String act_detail;
	
	@Column(name="act_ordshow")
	private Integer act_ordshow;
	
	@Column(name="pr_content")
	private String pr_content;
	
	@Column(name="pr_ordshow")
	private Integer pr_ordshow;
	
	@Column(name="createBy")
	private String createBy;
	
	@Column(name="createDate")
	private Date createDate;
	
	@Column(name="modifyBy")
	private String modifyBy;

	public Long getAct_prscID() {
		return act_prscID;
	}

	public void setAct_prscID(Long act_prscID) {
		this.act_prscID = act_prscID;
	}

	public ActiDateSelect getActiDateSelect() {
		return actiDateSelect;
	}

	public void setActiDateSelect(ActiDateSelect actiDateSelect) {
		this.actiDateSelect = actiDateSelect;
	}

	public ActiOrganization getActiOrganization() {
		return actiOrganization;
	}

	public void setActiOrganization(ActiOrganization actiOrganization) {
		this.actiOrganization = actiOrganization;
	}

	public String getAct_prsctype() {
		return act_prsctype;
	}

	public void setAct_prsctype(String act_prsctype) {
		this.act_prsctype = act_prsctype;
	}

	public String getAct_detail() {
		return act_detail;
	}

	public void setAct_detail(String act_detail) {
		this.act_detail = act_detail;
	}

	public Integer getAct_ordshow() {
		return act_ordshow;
	}

	public void setAct_ordshow(Integer act_ordshow) {
		this.act_ordshow = act_ordshow;
	}

	public String getPr_content() {
		return pr_content;
	}

	public void setPr_content(String pr_content) {
		this.pr_content = pr_content;
	}

	public Integer getPr_ordshow() {
		return pr_ordshow;
	}

	public void setPr_ordshow(Integer pr_ordshow) {
		this.pr_ordshow = pr_ordshow;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public ActiPr getActiPr() {
		return actiPr;
	}

	public void setActiPr(ActiPr actiPr) {
		this.actiPr = actiPr;
	}

}

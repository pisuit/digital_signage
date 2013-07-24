package th.co.aerothai.digitalsignage.model.pr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="acti_pr")
public class ActiPr {
	
	@Id
	@Column(name="pr_id")
	private Long pr_id;
	
	@Column(name="pr_content")
	private String pr_content;
	
	@Column(name="pr_date")
	private Date pr_date;
	
	@Column(name="pr_enddate")
	private Date pr_enddate;
	
	@Column(name="contact_name")
	private String contact_name;
	
	@Column(name="contact_address")
	private String contact_address;
	
	@Column(name="contact_tel")
	private String contact_tel;
	
	@Column(name="pr_own")
	private Integer pr_own;
	
	@Column(name="pr_creater")
	private Integer pr_creater;
	
	@Column(name="pr_remark")
	private String pr_remark;
	
	@Column(name="pr_select")
	private Integer pr_select;

	public Long getPr_id() {
		return pr_id;
	}

	public void setPr_id(Long pr_id) {
		this.pr_id = pr_id;
	}

	public String getPr_content() {
		return pr_content;
	}

	public void setPr_content(String pr_content) {
		this.pr_content = pr_content;
	}

	public Date getPr_date() {
		return pr_date;
	}

	public void setPr_date(Date pr_date) {
		this.pr_date = pr_date;
	}

	public Date getPr_enddate() {
		return pr_enddate;
	}

	public void setPr_enddate(Date pr_enddate) {
		this.pr_enddate = pr_enddate;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_address() {
		return contact_address;
	}

	public void setContact_address(String contact_address) {
		this.contact_address = contact_address;
	}

	public String getContact_tel() {
		return contact_tel;
	}

	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}

	public Integer getPr_own() {
		return pr_own;
	}

	public void setPr_own(Integer pr_own) {
		this.pr_own = pr_own;
	}

	public Integer getPr_creater() {
		return pr_creater;
	}

	public void setPr_creater(Integer pr_creater) {
		this.pr_creater = pr_creater;
	}

	public String getPr_remark() {
		return pr_remark;
	}

	public void setPr_remark(String pr_remark) {
		this.pr_remark = pr_remark;
	}

	public Integer getPr_select() {
		return pr_select;
	}

	public void setPr_select(Integer pr_select) {
		this.pr_select = pr_select;
	}
	
}

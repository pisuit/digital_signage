package th.co.aerothai.digitalsignage.manager;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import th.co.aerothai.digitalsignage.dao.UserDao;

@ManagedBean(name="login")
@SessionScoped
public class LoginManager {
	private String username;
	private String password;
	
	public String login(){
		if(username.equals("admin") && password.equals("admin")){
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", "admin");
			return "profile?faces-redirect=true";
		} else if(UserDao.findUsers(username, password) != null){
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", UserDao.findUsers(username, password).getUsername());
			return "profile?faces-redirect=true";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", "PrimeFaces makes no mistakes"));
			return null;
		}
	}
	
	public String logout(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login?faces-redirect=true";
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

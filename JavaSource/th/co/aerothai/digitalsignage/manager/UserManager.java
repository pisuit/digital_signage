package th.co.aerothai.digitalsignage.manager;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import th.co.aerothai.digitalsignage.dao.UserDao;
import th.co.aerothai.digitalsignage.model.User;

@ManagedBean(name="userManager")
@ViewScoped
public class UserManager {
	private List<User> userList = new ArrayList<User>();
	private User selectedUser = new User();
	
	public UserManager(){
		createUserList();
	}
	
	private void createUserList(){
		if(userList != null) userList.clear();
		
		userList = UserDao.getUsers();
	}
	
	public void saveUser(){
		UserDao.saveUser(selectedUser);
		createUserList();
		selectedUser = new User();
	}
	
	public void reset(){
		selectedUser = new User();
	}
	
	public void deleteUser(){
		UserDao.deleteUser(selectedUser);
		createUserList();
		selectedUser = new User();
	}
	
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public User getSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
}

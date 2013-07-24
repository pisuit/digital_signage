package th.co.aerothai.digitalsignage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;

import th.co.aerothai.digitalsignage.dao.ServiceDao;
import th.co.aerothai.digitalsignage.model.ClientProfile;
import th.co.aerothai.digitalsignage.model.Layout;
import th.co.aerothai.digitalsignage.model.LayoutProfile;
import th.co.aerothai.digitalsignage.model.Profile;

@ManagedBean(name="profile")
@ViewScoped
public class ProfileManager implements Serializable{
	private List<Layout> layoutList = new ArrayList<Layout>();
	private Layout selectedLayout = new Layout();
	private List<ClientProfile> clientProfileList = new ArrayList<ClientProfile>();
	private ClientProfile selectedClientProfile = new ClientProfile();
	private ClientProfile editClientProfile = new ClientProfile();
	private List<SelectItem> layoutSelectItemList = new ArrayList<SelectItem>();
	private List<SelectItem> profileSelectItemList = new ArrayList<SelectItem>();
	private Long selectedLayoutID = Long.valueOf("-1");
	private List<Profile> profileList = new ArrayList<Profile>();
	private Profile selectedProfile = new Profile();
	private List<LayoutProfile> layoutProfileList = new ArrayList<LayoutProfile>();
	private LayoutProfile editLayoutProfile = new LayoutProfile();
	private Long selectedLayoutIDForLayPro = Long.valueOf("-1");
	private LayoutProfile selectedLayoutProfile = new LayoutProfile();
	private Long selectedProfileID = Long.valueOf("-1");
	
	public ProfileManager(){
		createLayoutList();
		createClientProfileList();
		createLayoutSelectItemList();
		createProfileList();
		createProfileSelectItemList();
	}
	
	public String consoleHandler(String command, String[] params){
		if(ServiceDao.executeTerminal(StringUtils.join(params, " ")) != null){
			String result = "";
			for(Object[] o : ServiceDao.executeTerminal(StringUtils.join(params, " "))){
				result = result + "<-->";
				for(int i=0;i<o.length;i++){
					result = result+o[i]+"|";
				}
			}
			return result;
		} else {
			return "error !!";
		}
	}
	
	public String editLayout(){
		LayoutManager layoutManager = (LayoutManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("layout");
		if(layoutManager != null) {
			layoutManager.setSelectedLayoutID(selectedLayout.getId());
			layoutManager.loadLayout();
		} else {
			LayoutManager manager = new LayoutManager();
			manager.setSelectedLayoutID(selectedLayout.getId());
			manager.loadLayout();
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("layout", manager);
		}
		MenuHandler menu = (MenuHandler) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("menu");
		menu.setActiveTab("0");
		
		return "layout?faces-redirect=true";
	}
	
	private void createProfileSelectItemList(){
		profileSelectItemList.clear();
		if(profileSelectItemList != null) profileSelectItemList.clear();
		profileSelectItemList.add(new SelectItem(Long.valueOf("-1"), "Select Profile"));
		
		for(Profile profile : ServiceDao.getProfileList()){
			profileSelectItemList.add(new SelectItem(profile.getId(), profile.getName()));
		}
	}
	
	private void createProfileList(){
		if(profileList != null)	profileList.clear();
		profileList.addAll(ServiceDao.getProfileList());
	}
	
	public void deleteLayout(){
		ServiceDao.deleteLayout(selectedLayout);
		createLayoutList();
		createLayoutSelectItemList();
		selectedLayout = new Layout();
	}
	
	public void deleteClientProfile(){
		ServiceDao.deleteClientProfile(editClientProfile);
		createClientProfileList();
		clearClientProfule();
	}
	
	public void clientProfileSelected(){
		editClientProfile = selectedClientProfile;
		if(selectedClientProfile.getLayout() != null){
			selectedLayoutID = selectedClientProfile.getLayout().getId();
		}
		if(selectedClientProfile.getProfile() != null){
			selectedProfileID = selectedClientProfile.getProfile().getId();
		}
		
	} 

	private void createLayoutList(){
		if(layoutList != null) layoutList.clear();
		layoutList.addAll(ServiceDao.getLayoutList());
	}
	
	private void createClientProfileList(){
		if(clientProfileList != null) clientProfileList.clear();
		clientProfileList.addAll(ServiceDao.getClientProfileList());
	}
	
	private void createLayoutSelectItemList(){
		layoutSelectItemList.clear();
		layoutSelectItemList.add(new SelectItem(Long.valueOf("-1"), "Select Layout"));
		for(Layout layout : ServiceDao.getLayoutList()){
			layoutSelectItemList.add(new SelectItem(layout.getId(), layout.getName()));
		}
	}
	
	public void saveLayoutProfile(){
		editLayoutProfile.setLayout(ServiceDao.getLayout(selectedLayoutIDForLayPro));
		layoutProfileList.add(editLayoutProfile);
//		for(LayoutProfile layoutProfile : layoutProfileList){
//			if(layoutProfile.getLayout().getName().equals(editLayoutProfile.getLayout().getName())){
//				layoutProfileList.remove(layoutProfile);
//				break;
//			}
//		}
		clearLayoutProfile();
	}
	
	public void clearLayoutProfile(){
		selectedLayoutProfile = new LayoutProfile();
		selectedLayoutIDForLayPro = Long.valueOf("-1");
		editLayoutProfile = new LayoutProfile();
	}
	
	public void saveProfile(){
		selectedProfile.setModifiedTime(new Date());
		ServiceDao.saveProfile(selectedProfile, layoutProfileList);
		createProfileSelectItemList();
		clearProfile();
		createProfileList();
	}
	
	public void deleteProfile(){
		ServiceDao.deleteProfile(selectedProfile);
		createProfileList();
		createProfileSelectItemList();
		clearProfile();
	}
	
	public void clearProfile(){
		selectedProfile = new Profile();
		layoutProfileList.clear();
		editLayoutProfile = new LayoutProfile();
		selectedLayoutProfile = new LayoutProfile();
	}
	
	public void deleteLayoutProfile(){
		layoutProfileList.remove(selectedLayoutProfile);
		clearLayoutProfile();
	}
	
	public void clearClientProfule(){
		selectedClientProfile = new ClientProfile();
		editClientProfile = new ClientProfile();
		selectedLayoutID = Long.valueOf("-1");
		selectedProfileID = Long.valueOf("-1");
	}
	
	public void profileSelected(){
		layoutProfileList.clear();
		layoutProfileList.addAll(selectedProfile.getLayoutProfiles());
	}
	
	public void layoutProfileSelected(){
		editLayoutProfile = selectedLayoutProfile;
		selectedLayoutIDForLayPro = selectedLayoutProfile.getLayout().getId();
	}
	
	public void test(){
		System.out.println("value changed");
	}
	
	public void saveClientProfile(){
		editClientProfile.setLayout(ServiceDao.getLayout(selectedLayoutID));
		editClientProfile.setProfile(ServiceDao.getProfile(selectedProfileID));
		ServiceDao.saveClientProfile(editClientProfile);
		createClientProfileList();
		clearClientProfule();
	}

	public List<ClientProfile> getClientProfileList() {
		return clientProfileList;
	}

	public void setClientProfileList(List<ClientProfile> clientProfileList) {
		this.clientProfileList = clientProfileList;
	}

	public ClientProfile getSelectedClientProfile() {
		return selectedClientProfile;
	}

	public void setSelectedClientProfile(ClientProfile selectedClientProfile) {
		this.selectedClientProfile = selectedClientProfile;
	}

	public ClientProfile getEditClientProfile() {
		return editClientProfile;
	}

	public void setEditClientProfile(ClientProfile editClientProfile) {
		this.editClientProfile = editClientProfile;
	}

	public List<Layout> getLayoutList() {
		return layoutList;
	}

	public void setLayoutList(List<Layout> layoutList) {
		this.layoutList = layoutList;
	}

	public Layout getSelectedLayout() {
		return selectedLayout;
	}

	public void setSelectedLayout(Layout selectedLayout) {
		this.selectedLayout = selectedLayout;
	}

	public List<SelectItem> getLayoutSelectItemList() {
		return layoutSelectItemList;
	}

	public void setLayoutSelectItemList(List<SelectItem> layoutSelectItemList) {
		this.layoutSelectItemList = layoutSelectItemList;
	}

	public Long getSelectedLayoutID() {
		return selectedLayoutID;
	}

	public void setSelectedLayoutID(Long selectedLayoutID) {
		this.selectedLayoutID = selectedLayoutID;
	}

	public List<Profile> getProfileList() {
		return profileList;
	}

	public void setProfileList(List<Profile> profileList) {
		this.profileList = profileList;
	}

	public Profile getSelectedProfile() {
		return selectedProfile;
	}

	public void setSelectedProfile(Profile selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

	public List<LayoutProfile> getLayoutProfileList() {
		return layoutProfileList;
	}

	public void setLayoutProfileList(List<LayoutProfile> layoutProfileList) {
		this.layoutProfileList = layoutProfileList;
	}

	public LayoutProfile getEditLayoutProfile() {
		return editLayoutProfile;
	}

	public void setEditLayoutProfile(LayoutProfile editLayoutProfile) {
		this.editLayoutProfile = editLayoutProfile;
	}

	public Long getSelectedLayoutIDForLayPro() {
		return selectedLayoutIDForLayPro;
	}

	public void setSelectedLayoutIDForLayPro(Long selectedLayoutIDForLayPro) {
		this.selectedLayoutIDForLayPro = selectedLayoutIDForLayPro;
	}

	public LayoutProfile getSelectedLayoutProfile() {
		return selectedLayoutProfile;
	}

	public void setSelectedLayoutProfile(LayoutProfile selectedLayoutProfile) {
		this.selectedLayoutProfile = selectedLayoutProfile;
	}

	public List<SelectItem> getProfileSelectItemList() {
		return profileSelectItemList;
	}

	public void setProfileSelectItemList(List<SelectItem> profileSelectItemList) {
		this.profileSelectItemList = profileSelectItemList;
	}

	public Long getSelectedProfileID() {
		return selectedProfileID;
	}

	public void setSelectedProfileID(Long selectedProfileID) {
		this.selectedProfileID = selectedProfileID;
	}

}

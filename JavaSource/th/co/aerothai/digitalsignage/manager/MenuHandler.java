package th.co.aerothai.digitalsignage.manager;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="menu")
@SessionScoped
public class MenuHandler implements Serializable{
	private String activeTab = "1";
	
	public String changeTab(String index){
		activeTab = index;
		if(index.equals("0")) {
			LayoutManager manager = (LayoutManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("layout");
			if(manager!= null) manager.initLayout();
			
			return "layout?faces-redirect=true";
		}
		if(index.equals("1")) return "profile?faces-redirect=true";
//		if(index.equals("2")) {
//			PreviewManager preview = (PreviewManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("preview");
//			LayoutManager layout = (LayoutManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("layout");
//			if(preview != null) preview.init();
//			if(layout != null) layout.copyComponentMap();
//			
//			return "preview?faces-redirect=true";
//		}
		if(index.equals("2")) return "file?faces-redirect=true";
		if(index.equals("3")) return "user?faces-redirect=true";
		
		return "";
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}
}

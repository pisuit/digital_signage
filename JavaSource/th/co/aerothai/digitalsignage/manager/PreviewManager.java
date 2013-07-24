package th.co.aerothai.digitalsignage.manager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.BeanUtils;

import th.co.aerothai.digitalsignage.customcomponent.CCHtmlOutputText;
import th.co.aerothai.digitalsignage.customcomponent.CCPanel;

@ManagedBean(name="preview")
@SessionScoped
public class PreviewManager implements Serializable{
	private String bgColor = "FFFFFF";
	private String width = "1280";
	private String height = "1024";
	private String bgImage = "";
	private HtmlPanelGroup panelGroup = new HtmlPanelGroup();
	
	public PreviewManager(){
		init();
	}
	
	public void init(){
		panelGroup.getChildren().clear();
		LayoutManager layout = (LayoutManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("layout");
		buildLayout(layout);
	}
	
	private void buildLayout(LayoutManager manager){
		bgColor = manager.getBgColor();
		width = manager.getWidth();
		height = manager.getHeight();
//		bgImage = manager.getBgImageName();
		
		for(UIComponent p : manager.getComponentMap().values()){
			if(p.getClass().equals(CCPanel.class)){		
				panelGroup.getChildren().add(p);
			}	
		}
	}
	
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getBgImage() {
		return bgImage;
	}
	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}
	public HtmlPanelGroup getPanelGroup() {
		return panelGroup;
	}
	public void setPanelGroup(HtmlPanelGroup panelGroup) {
		this.panelGroup = panelGroup;
	}
}

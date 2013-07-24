package th.co.aerothai.digitalsignage.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.behavior.ClientBehaviorContext.Parameter;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.primefaces.component.feedreader.FeedReader;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.separator.Separator;
import org.primefaces.context.RequestContext;

import com.sun.faces.facelets.component.UIRepeat;
import com.sun.webui.jsf.component.IFrame;

import th.co.aerothai.digitalsignage.customcomponent.CCHtmlOutputText;
import th.co.aerothai.digitalsignage.customcomponent.CCImageSwitch;
import th.co.aerothai.digitalsignage.customcomponent.CCMarquee;
import th.co.aerothai.digitalsignage.customcomponent.CCMedia;
import th.co.aerothai.digitalsignage.customcomponent.CCPanel;
import th.co.aerothai.digitalsignage.dao.PRDao;
import th.co.aerothai.digitalsignage.dao.ServiceDao;
import th.co.aerothai.digitalsignage.model.ClientProfile;
import th.co.aerothai.digitalsignage.model.LayoutProfile;
import th.co.aerothai.digitalsignage.model.Panel;
import th.co.aerothai.digitalsignage.model.Layout;
import th.co.aerothai.digitalsignage.model.Profile;
import th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator;

@ManagedBean(name="display")
@SessionScoped
public class DisplayManager implements Serializable{
	private HtmlPanelGroup panelGroup = new HtmlPanelGroup();
	private String width;
	private String height;
	private String bgColor;
	private String displayName;
//	private Long currentLayoutID;
	private Date currentModifyTime;
//	private Long currentProfileID;
	private Date currentProfileModifytime;
	private Layout nextLayout;
	private int layoutTiming = 0;
	private boolean layoutProfileDisableed = true;
	List<LayoutProfile> layoutProfiles;
	private int currentLayoutOrder = 0;
	private int maxLayoutOrder = 0;
	private String bgImage;
	private List<String> updatedPanel = new ArrayList<String>();
	private String updatedPanelAsString;
	private boolean isFeedPollRendered = false;
	
	public DisplayManager(){
		FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        displayName = paramMap.get("display");

        init();         
	}
	
	public void init(){
		updatedPanel.clear();
		isFeedPollRendered = false;
		ClientProfile clientProfile = ServiceDao.getClientProfileByDisplay(displayName);
		Layout layout = clientProfile.getLayout();
		
		if(clientProfile.isLocalHost() && !FacesContext.getCurrentInstance().getExternalContext().getRequestServerName().contains("localhost")){
			ServiceDao.duplicateDisplayData(clientProfile);
		}
		
		if(layout != null){
//			currentLayoutID = layout.getId();
			layoutProfileDisableed = true;
			currentModifyTime = layout.getModifiedTime();
			
			width = layout.getWidth();
			height = layout.getHeight();
			bgColor = layout.getBgColor();
			bgImage = layout.getBgImage();
			
//			panelGroup.setStyle("width:"+width+"px;height:"+height+"px;background-color:#"+bgColor+";background-image:url('image/"+bgImage+"');");
	
			for(Panel p : layout.getPanels()){
				CCPanel panel = new CCPanel();
				panel.setId(p.getPanelId());
				panel.setStyle("position:absolute;width:"+p.getWidth()+"px;height:"+p.getHeight()+"px;left:"+p.getxPosition()+"px;top:"+p.getyPosition()+"px;z-index:"+p.getzPosition()+";");
				
				if(p.getImages().size() != 0){
					GraphicImage graphicImage = new GraphicImage();
					graphicImage.setUrl(p.getImages().iterator().next().getUrl());
					graphicImage.setStyle("width:100%;height:100%");
					panel.getChildren().add(graphicImage);
				}
				if(p.getWebs().size() != 0){
					IFrame frame = new IFrame();
					frame.setUrl(p.getWebs().iterator().next().getValue());
					frame.setStyle("width:100%;height:100%");
					frame.setScrolling("no");
					panel.getChildren().add(frame);
				}
				if(p.getTexts().size() != 0){
					HtmlOutputText outputText = new HtmlOutputText();
					
					if(p.getTexts().iterator().next().isScrolling()){
						String marquee = "<marquee style=\"height:100%;white-space:nowrap\" behavior=\"scroll\" direction=\""+p.getTexts().iterator().next().getScrollingDirection()+"\" scrollamount=\""+p.getTexts().iterator().next().getScrollingSpeed()+"\">";
						outputText.setValue(marquee+p.getTexts().iterator().next().getValue()+"</marquee>");
					} else {
						outputText.setValue(p.getTexts().iterator().next().getValue());
					}
			
					outputText.setEscape(false);
					panel.getChildren().add(outputText);
				}
				if(p.isPrNews()){
					UIRepeat repeat = new UIRepeat();
					repeat.setValue("value");
					
					for(String c :PRDao.getPrNews()){
						GraphicImage graphicImage = new GraphicImage();
						graphicImage.setValue("http://home.aerothai.co.th/pr_news/UploadImage/"+c);
						graphicImage.setStyle("width:100%;height:100%");
						repeat.getChildren().add(graphicImage);												
					}		
					
					CCImageSwitch imageSwitch = new CCImageSwitch();
					imageSwitch.setId("slide"+new HibernateCurrentTimeIDGenerator().gen().toString());
					imageSwitch.setEffect("fade");
					imageSwitch.setStyle("width:100%;height:100%");
					imageSwitch.setInterval(10);
					imageSwitch.setSlideshowSpeed(10*1000);
					imageSwitch.getChildren().add(repeat);
					imageSwitch.setImageList(PRDao.getPrNews());
					
					panel.getChildren().add(imageSwitch);
				}
				if(p.getSlideShows().size() != 0){
					UIRepeat repeat = new UIRepeat();
					repeat.setValue("value");
					
					for(String c : StringUtils.split(p.getSlideShows().iterator().next().getImages(), ';')){
						GraphicImage graphicImage = new GraphicImage();
						graphicImage.setValue("image/"+c);
						graphicImage.setStyle("width:100%;height:100%");
						repeat.getChildren().add(graphicImage);
					}
											
					CCImageSwitch imageSwitch = new CCImageSwitch();
					imageSwitch.setId("slide"+new HibernateCurrentTimeIDGenerator().gen().toString());
					imageSwitch.setEffect("fade");
					imageSwitch.setStyle("width:100%;height:100%");
					imageSwitch.setInterval(p.getSlideShows().iterator().next().getInterval());
					imageSwitch.setSlideshowSpeed(p.getSlideShows().iterator().next().getInterval()*1000);
					imageSwitch.getChildren().add(repeat);
					imageSwitch.setImageList(Arrays.asList(StringUtils.split(p.getSlideShows().iterator().next().getImages(), ',')));
					
					panel.getChildren().add(imageSwitch);
				}
				if(p.getVideos().size() != 0){
					if(p.getVideos().iterator().next().isLocal()){
						CCMedia media = new CCMedia();
						media.setValue(getServerURL()+"/video/"+p.getVideos().iterator().next().getValue());
						media.setVideoCode(p.getVideos().iterator().next().getValue());
						media.setLocal(true);
						media.setStyle("width:100%;height:100%;");
						
						UIParameter parameter = new UIParameter();
						parameter.setName("wmode");
						parameter.setValue("transparent");
						media.getChildren().add(parameter);
						
						parameter = new UIParameter();
						parameter.setName("loop");
						parameter.setValue("true");
						media.getChildren().add(parameter);
						
						parameter = new UIParameter();
						parameter.setName("controller");
						parameter.setValue("false");
						media.getChildren().add(parameter);	
						
						parameter = new UIParameter();
						parameter.setName("scale");
						parameter.setValue("tofit");
						media.getChildren().add(parameter);	
						
						parameter = new UIParameter();
						parameter.setName("kioskmode");
						parameter.setValue("true");
						media.getChildren().add(parameter);
						
						panel.getChildren().add(media);
					} else {
						CCMedia media = new CCMedia();
						media.setVideoCode(p.getVideos().iterator().next().getValue());
						media.setValue("http://www.youtube.com/apiplayer?video_id="+p.getVideos().iterator().next().getValue()+"&version=3&autoplay=1&loop=1&hd=1&playlist="+p.getVideos().iterator().next().getValue());
						media.setPlayer("flash");
						media.setLocal(false);
						media.setStyle("width:100%;height:100%");
						
						UIParameter parameter = new UIParameter();
						parameter.setName("wmode");
						parameter.setValue("opaque");
						media.getChildren().add(parameter);
						
						panel.getChildren().add(media);
					}	
				}
				if(p.getFeeds().size() != 0){
					FeedReader reader = new FeedReader();
					CCHtmlOutputText text = new CCHtmlOutputText();
					HtmlPanelGrid grid = new HtmlPanelGrid();
					HtmlPanelGroup group = new HtmlPanelGroup();
					Separator separator = new Separator();
					CCMarquee marquee = new CCMarquee();
					
					group.setLayout("block");
					group.setStyle("margin-top: 5px; background-color: #ADDFFF; border-radius: 5px; width:100%");
					
					grid.setColumns(1);
					grid.setCellpadding("3");
					grid.setStyle("width:100%");
					
					reader.setValue(p.getFeeds().iterator().next().getUrl());
					reader.setVar("feed");
						
					text.setValueExpression("value", createValueExpression("#{feed.title}", String.class));
					text.setStyle("font-weight:bold");
					group.getChildren().add(text);
					grid.getChildren().add(group);
					
					text = new CCHtmlOutputText();	
					group = new HtmlPanelGroup();
					text.setValueExpression("value", createValueExpression("#{feed.description.value}", String.class));
					group.setLayout("block");
					group.setStyle("background-color: #E3E4FA; border-radius: 5px; width:100%");
					group.getChildren().add(text);
					grid.getChildren().add(group);
					
					grid.getChildren().add(separator);
					
					reader.getChildren().add(grid);
					marquee.getChildren().add(reader);
					marquee.setSpeed(p.getFeeds().iterator().next().getScrollSpeed());
					
					panel.setStyleClass("feed");
					panel.getChildren().add(marquee);
					updatedPanel.add(":displayform:"+panel.getId());
					isFeedPollRendered = true;
				}
				panelGroup.getChildren().add(panel);
			}
		} else {
			layoutProfileDisableed = false;
			Profile profile = ServiceDao.getClientProfileByDisplay(displayName).getProfile();
			layoutProfiles = ServiceDao.getLayoutProfileListForProfile(profile);
			
//			currentProfileID = profile.getId();
			currentProfileModifytime = profile.getModifiedTime();
			currentLayoutOrder = 0;
			maxLayoutOrder = layoutProfiles.size()-1;
			nextLayout = layoutProfiles.get(0).getLayout();
			layoutTiming = layoutProfiles.get(0).getTiming();
			layoutProfileDisableed = false;
			buildNextLayout();
		}
	}
	
	public String getServerURL(){	
		URL newURL = null;
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			URL url = new URL(request.getRequestURL().toString());
			newURL = new URL(url.getProtocol(), url.getHost(), 8082, "");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newURL.toString();
	}
	
	public void buildNextLayout(){
		updatedPanel.clear();
		isFeedPollRendered = false;
		panelGroup.getChildren().clear();
		layoutTiming = layoutProfiles.get(currentLayoutOrder).getTiming();
	
		width = nextLayout.getWidth();
		height = nextLayout.getHeight();
		bgColor = nextLayout.getBgColor();
		bgImage = nextLayout.getBgImage();
		
		for(Panel p : nextLayout.getPanels()){
			CCPanel panel = new CCPanel();
			panel.setId(p.getPanelId());
			panel.setStyle("position:absolute;width:"+p.getWidth()+"px;height:"+p.getHeight()+"px;left:"+p.getxPosition()+"px;top:"+p.getyPosition()+"px;z-index:"+p.getzPosition()+";");
			
			if(p.getImages().size() != 0){
				GraphicImage graphicImage = new GraphicImage();
				graphicImage.setUrl(p.getImages().iterator().next().getUrl());
				graphicImage.setStyle("width:100%;height:100%");
				panel.getChildren().add(graphicImage);
			}
			if(p.getWebs().size() != 0){
				IFrame frame = new IFrame();
				frame.setUrl(p.getWebs().iterator().next().getValue());
				frame.setStyle("width:100%;height:100%");
				frame.setScrolling("no");
				panel.getChildren().add(frame);
			}
			if(p.getTexts().size() != 0){
				HtmlOutputText outputText = new HtmlOutputText();
				
				if(p.getTexts().iterator().next().isScrolling()){
					String marquee = "<marquee style=\"height:100%;white-space:nowrap\" behavior=\"scroll\" direction=\""+p.getTexts().iterator().next().getScrollingDirection()+"\" scrollamount=\""+p.getTexts().iterator().next().getScrollingSpeed()+"\">";
					outputText.setValue(marquee+p.getTexts().iterator().next().getValue()+"</marquee>");
				} else {
					outputText.setValue(p.getTexts().iterator().next().getValue());
				}
		
				outputText.setEscape(false);
				panel.getChildren().add(outputText);
			}
			if(p.getVideos().size() != 0){
				if(p.getVideos().iterator().next().isLocal()){
					CCMedia media = new CCMedia();
					media.setValue(getServerURL()+"/video/"+p.getVideos().iterator().next().getValue());
					media.setVideoCode(p.getVideos().iterator().next().getValue());
					media.setLocal(true);
					media.setStyle("width:100%;height:100%;");
					
					UIParameter parameter = new UIParameter();
					parameter.setName("wmode");
					parameter.setValue("transparent");
					media.getChildren().add(parameter);
					
					parameter = new UIParameter();
					parameter.setName("loop");
					parameter.setValue("true");
					media.getChildren().add(parameter);
					
					parameter = new UIParameter();
					parameter.setName("controller");
					parameter.setValue("false");
					media.getChildren().add(parameter);	
					
					parameter = new UIParameter();
					parameter.setName("scale");
					parameter.setValue("tofit");
					media.getChildren().add(parameter);	
					
					parameter = new UIParameter();
					parameter.setName("kioskmode");
					parameter.setValue("true");
					media.getChildren().add(parameter);
					
					panel.getChildren().add(media);
				} else {
					CCMedia media = new CCMedia();
					media.setVideoCode(p.getVideos().iterator().next().getValue());
					media.setValue("http://www.youtube.com/apiplayer?video_id="+p.getVideos().iterator().next().getValue()+"&version=3&autoplay=1&loop=1&hd=1&playlist="+p.getVideos().iterator().next().getValue());
					media.setPlayer("flash");
					media.setLocal(false);
					media.setStyle("width:100%;height:100%");
					
					UIParameter parameter = new UIParameter();
					parameter.setName("wmode");
					parameter.setValue("opaque");
					media.getChildren().add(parameter);
					
					panel.getChildren().add(media);
				}		
			}
			if(p.isPrNews()){
				UIRepeat repeat = new UIRepeat();
				repeat.setValue("value");
				
				for(String c :PRDao.getPrNews()){
					GraphicImage graphicImage = new GraphicImage();
					graphicImage.setValue("http://home.aerothai.co.th/pr_news/UploadImage/"+c);
					graphicImage.setStyle("width:100%;height:100%");
					repeat.getChildren().add(graphicImage);												
				}		
				
				CCImageSwitch imageSwitch = new CCImageSwitch();
				imageSwitch.setId("slide"+new HibernateCurrentTimeIDGenerator().gen().toString());
				imageSwitch.setEffect("fade");
				imageSwitch.setStyle("width:100%;height:100%");
				imageSwitch.setInterval(10);
				imageSwitch.setSlideshowSpeed(10*1000);
				imageSwitch.getChildren().add(repeat);
				imageSwitch.setImageList(PRDao.getPrNews());
				
				panel.getChildren().add(imageSwitch);
			}
			if(p.getSlideShows().size() != 0){
				UIRepeat repeat = new UIRepeat();
				repeat.setValue("value");
				
				for(String c : StringUtils.split(p.getSlideShows().iterator().next().getImages(), ';')){
					GraphicImage graphicImage = new GraphicImage();
					graphicImage.setValue("image/"+c);
					graphicImage.setStyle("width:100%;height:100%");
					repeat.getChildren().add(graphicImage);
				}
										
				CCImageSwitch imageSwitch = new CCImageSwitch();
				imageSwitch.setId("slide"+new HibernateCurrentTimeIDGenerator().gen().toString());
				imageSwitch.setEffect("fade");
				imageSwitch.setStyle("width:100%;height:100%");
				imageSwitch.setInterval(p.getSlideShows().iterator().next().getInterval());
				imageSwitch.setSlideshowSpeed(p.getSlideShows().iterator().next().getInterval()*1000);
				imageSwitch.getChildren().add(repeat);
				imageSwitch.setImageList(Arrays.asList(StringUtils.split(p.getSlideShows().iterator().next().getImages(), ',')));
				
				panel.getChildren().add(imageSwitch);
			}
			if(p.getFeeds().size() != 0){
				FeedReader reader = new FeedReader();
				CCHtmlOutputText text = new CCHtmlOutputText();
				HtmlPanelGrid grid = new HtmlPanelGrid();
				HtmlPanelGroup group = new HtmlPanelGroup();
				Separator separator = new Separator();
				CCMarquee marquee = new CCMarquee();
				
				group.setLayout("block");
				group.setStyle("margin-top: 5px; background-color: #ADDFFF; border-radius: 5px; width:100%");
				
				grid.setColumns(1);
				grid.setCellpadding("3");
				grid.setStyle("width:100%");
				
				reader.setValue(p.getFeeds().iterator().next().getUrl());
				reader.setVar("feed");
					
				text.setValueExpression("value", createValueExpression("#{feed.title}", String.class));
				text.setStyle("font-weight:bold");
				group.getChildren().add(text);
				grid.getChildren().add(group);
				
				text = new CCHtmlOutputText();	
				group = new HtmlPanelGroup();
				text.setValueExpression("value", createValueExpression("#{feed.description.value}", String.class));
				group.setLayout("block");
				group.setStyle("background-color: #E3E4FA; border-radius: 5px; width:100%");
				group.getChildren().add(text);
				grid.getChildren().add(group);
				
				grid.getChildren().add(separator);
				
				reader.getChildren().add(grid);
				marquee.getChildren().add(reader);
				marquee.setSpeed(p.getFeeds().iterator().next().getScrollSpeed());
				
				panel.setStyleClass("feed");
				panel.getChildren().add(marquee);
				updatedPanel.add(":displayform:"+panel.getId());
				isFeedPollRendered = true;
			}
			panelGroup.getChildren().add(panel);
		}
		
		if(currentLayoutOrder == maxLayoutOrder){
			currentLayoutOrder = 0;
		} else {
			currentLayoutOrder++;
		}
		
		nextLayout = layoutProfiles.get(currentLayoutOrder).getLayout();
		
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.execute("update()");
	}
	
	public void ping(){
		try{
			String response;
	        String result = "";
			URL url = new URL("http://172.16.91.137:8081/digital_signage/JSONPServlet?aaa");
	        URLConnection uc = url.openConnection();
	        uc.setReadTimeout(2000);
	        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

	        while ((response = in.readLine()) != null) {
	            result += response;
	        }
	        
	        in.close();
	        
	        if(result.equals("aaa") && FacesContext.getCurrentInstance().getExternalContext().getRequestServerName().equals("localhost")){
	        	FacesContext.getCurrentInstance().getExternalContext().redirect("http://172.16.91.137:8081/digital_signage/display.jsf?display="+displayName);
	        	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("display");
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	public void disconnect(){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("display");
	}
	
	public void poll(){
		ClientProfile client = ServiceDao.getClientProfileByDisplay(displayName);
		if(client.getLayout() != null){
			layoutProfileDisableed = true;
			currentProfileModifytime = null;
			if(!client.getLayout().getModifiedTime().equals(currentModifyTime)){
				panelGroup.getChildren().clear();
				init();	
					
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.execute("update()");
				
				currentModifyTime = client.getLayout().getModifiedTime();
			}
		} else {
			layoutProfileDisableed = false;
			currentModifyTime = null;
			if(!client.getProfile().getModifiedTime().equals(currentProfileModifytime)){
				panelGroup.getChildren().clear();
				init();
					
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.execute("update()");
					
				currentProfileModifytime = client.getProfile().getModifiedTime();
			}
		}
		
	}
	
	public void updateParent(){
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("displayform");
	}
	
	public ValueExpression createValueExpression(String exp, Class c){
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = context.getELContext();
		Application app = context.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory(); 
		ValueExpression valueExp = elFactory.createValueExpression(elContext, exp, c); 		
		return valueExp;
	}
	
	public HtmlPanelGroup getPanelGroup() {
		return panelGroup;
	}
	public void setPanelGroup(HtmlPanelGroup panelGroup) {
		this.panelGroup = panelGroup;
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
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getLayoutTiming() {
		return layoutTiming;
	}

	public void setLayoutTiming(int layoutTiming) {
		this.layoutTiming = layoutTiming;
	}

	public boolean isLayoutProfileDisableed() {
		return layoutProfileDisableed;
	}

	public void setLayoutProfileDisableed(boolean layoutProfileDisableed) {
		this.layoutProfileDisableed = layoutProfileDisableed;
	}

	public Date getCurrentProfileModifytime() {
		return currentProfileModifytime;
	}

	public void setCurrentProfileModifytime(Date currentProfileModifytime) {
		this.currentProfileModifytime = currentProfileModifytime;
	}

	public String getBgImage() {
		return bgImage;
	}

	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}

	public List<String> getUpdatedPanel() {
		return updatedPanel;
	}

	public void setUpdatedPanel(List<String> updatedPanel) {
		this.updatedPanel = updatedPanel;
	}

	public String getUpdatedPanelAsString() {
		if(updatedPanel.size() != 0){		
			return StringUtils.join(updatedPanel, ',');
		} else {
			return "";
		}
	}

	public boolean isFeedPollRendered() {
		return isFeedPollRendered;
	}

	public void setFeedPollRendered(boolean isFeedPollRendered) {
		this.isFeedPollRendered = isFeedPollRendered;
	}

}

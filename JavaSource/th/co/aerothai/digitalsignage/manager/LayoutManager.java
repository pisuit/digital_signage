package th.co.aerothai.digitalsignage.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.contextmenu.ContextMenu;
import org.primefaces.component.dnd.Draggable;
import org.primefaces.component.feedreader.FeedReader;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.imageswitch.ImageSwitch;
import org.primefaces.component.media.Media;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.resizable.Resizable;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;

import com.sun.faces.facelets.component.UIRepeat;
import com.sun.jmx.remote.util.Service;
import com.sun.webui.jsf.component.IFrame;


import th.co.aerothai.digitalsignage.customcomponent.CCHtmlOutputText;
import th.co.aerothai.digitalsignage.customcomponent.CCImageSwitch;
import th.co.aerothai.digitalsignage.customcomponent.CCMarquee;
import th.co.aerothai.digitalsignage.customcomponent.CCMedia;
import th.co.aerothai.digitalsignage.customcomponent.CCPanel;
import th.co.aerothai.digitalsignage.customcomponent.CCVideo;
import th.co.aerothai.digitalsignage.customobject.ImageObject;
import th.co.aerothai.digitalsignage.dao.PRDao;
import th.co.aerothai.digitalsignage.dao.ServiceDao;
import th.co.aerothai.digitalsignage.model.Feed;
import th.co.aerothai.digitalsignage.model.Image;
import th.co.aerothai.digitalsignage.model.Panel;
import th.co.aerothai.digitalsignage.model.Layout;
import th.co.aerothai.digitalsignage.model.SlideShow;
import th.co.aerothai.digitalsignage.model.Text;
import th.co.aerothai.digitalsignage.model.Video;
import th.co.aerothai.digitalsignage.model.Web;
import th.co.aerothai.digitalsignage.utils.HibernateCurrentTimeIDGenerator;

@ManagedBean(name="layout")
@SessionScoped

public class LayoutManager implements Serializable{
	private HtmlPanelGroup panelGroup = new HtmlPanelGroup();
	private HashMap<String, UIComponent> componentMap = new HashMap<String, UIComponent>();
	private HashMap<String, String> clientIDMap = new HashMap<String, String>();
	private String imageURL;
	private String videoURL;
	private CCPanel currentPanel = new CCPanel();
	private String bgColor = "FFFFFF";
	private String width = "1280";
	private String height = "1024";
	private String text;
	private String layoutName;
	private Long selectedLayoutID = null; 
	private List<SelectItem> layoutSelectItemList = new ArrayList<SelectItem>();
	private List<SelectItem> videoSelectItemList = new ArrayList<SelectItem>();
	private List<ImageObject> imageSelectItemList = new ArrayList<ImageObject>();
	private boolean isTextScroll = false;
	private String selectedImage = "default";
	private String selectedVideo = "default";
	private String scrollDirection = "left";
	private int scrollSpeed = 5;
	private String webURL;
	private String panelHeight;
	private String panelWidth;
	private String panelTop;
	private String panelLeft;
	private ImageObject selectedBGImage;
	private ImageObject selectedLocalImage;
	private DualListModel<String> imageDualList;
	private int slideShowInterval = 3;
	private String feedUrl;
	
	public LayoutManager(){		
		createImageSelectItemList();
		createVideoSelectItemList();
		
		List<String> source = new ArrayList<String>();
		List<String> target = new ArrayList<String>();
		
		for(File f : new File("C:\\image\\").listFiles()){
			source.add(f.getName());
		}
		
		imageDualList = new DualListModel<String>(source, target);
	}
	
	public void disconnect(){
//		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("layout");
	}
	
	public void createImageSelectItemList(){
		if(imageSelectItemList != null) imageSelectItemList.clear();
		
		for(File f : new File("C:\\image\\").listFiles()){
			imageSelectItemList.add(new ImageObject(f.getName()));
		}
	}
	
	public void createVideoSelectItemList(){
		if(videoSelectItemList != null) videoSelectItemList.clear();
		
		videoSelectItemList.add(new SelectItem("default", "Select One"));
		
		File file = new File("C:\\video\\");
		for(File f : file.listFiles()){
			videoSelectItemList.add(new SelectItem(f.getName(), f.getName()));
		}
	}
	
	public void addRegion(){	
		if(panelGroup.getChildCount() != 0) {
			savePanelgroupProperty();
		}
		
		CCPanel panel = new CCPanel();	
		Draggable draggable = new Draggable();
		Resizable resizable = new Resizable();	
//		AjaxBehavior ajaxBehavior = new AjaxBehavior();
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem;
		
		String id = new HibernateCurrentTimeIDGenerator().gen().toString();
//		ajaxBehavior.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(createMethodExpression("#{layout.panelClosedListener}"), createMethodExpression("#{layout.panelClosedListener}")));
		
		panel.setId("panel"+id);	
//		panel.setStyle("width:300px; height:300px; position:absolute; z-index:"+counter+";"); 
		panel.setStyle("position:absolute;width:"+panel.getWidth()+"px;height:"+panel.getHeight()+"px;"); 
//		panel.setHeader("panel"+id);
//		panel.setClosable(true);
		panel.setStyleClass("stackclass");
//		panel.addClientBehavior("close", ajaxBehavior);
		
		draggable.setId("drag"+panel.getId());
		draggable.setFor(panel.getId());
		draggable.setContainment("parent");
		draggable.setSnap(true);
		draggable.setSnapMode("outer");
		draggable.setSnapTolerance(5);
		draggable.setStack(".stackclass");
		
		resizable.setId("resize"+panel.getId());
		resizable.setFor(panel.getId());
		resizable.setContainment(true);
		resizable.setHandles("s,e,w,n,se,sw,ne,nw");
		
		menuItem = new MenuItem();
		menuItem.setId("text"+panel.getId());
		menuItem.setValue("Add Text");
		menuItem.setIcon("ui-icon-pencil");
		menuItem.setOncomplete("addTextDialog.show()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));
		menuItem.setUpdate("addtextdialog");
		contextMenu.getChildren().add(menuItem);
		
		menuItem = new MenuItem();
		menuItem.setId("image"+panel.getId());
		menuItem.setValue("Add Link Image");
		menuItem.setIcon("ui-icon-image");
		menuItem.setOncomplete("addImageDialog.show()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));
		menuItem.setUpdate("addlinkimagedialog");
		contextMenu.getChildren().add(menuItem);
		
		menuItem = new MenuItem();
		menuItem.setId("imagelocal"+panel.getId());
		menuItem.setValue("Add Local Image");
		menuItem.setIcon("ui-icon-image");
		menuItem.setOncomplete("addImageDialog2.show()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
		menuItem.setUpdate("addlocalimagedialog");
		contextMenu.getChildren().add(menuItem);
		
//		Submenu submenu = new Submenu();
//		submenu.setLabel("Add Video");
//		submenu.setIcon("ui-icon-video");
		
		menuItem = new MenuItem();
		menuItem.setId("youtube"+panel.getId());
		menuItem.setValue("Add Youtube Video");
		menuItem.setIcon("ui-icon-video");
		menuItem.setOncomplete("addVideoDialog.show()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
		menuItem.setUpdate("addyoutubevideodialog");
		contextMenu.getChildren().add(menuItem);
		
		menuItem = new MenuItem();
		menuItem.setId("video"+panel.getId());
		menuItem.setValue("Add Local Video");
		menuItem.setIcon("ui-icon-video");
		menuItem.setOncomplete("addVideoDialog2.show()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));
		menuItem.setUpdate("addlocalvideodialog");
		contextMenu.getChildren().add(menuItem);
		
//		submenu.getChildren().add(menuItem);		
//		contextMenu.getChildren().add(submenu);
		
		menuItem = new MenuItem();
		menuItem.setId("web"+panel.getId());
		menuItem.setValue("Add Web Page");
		menuItem.setIcon("ui-icon-extlink");
		menuItem.setOncomplete("addWebDialog.show()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
		menuItem.setUpdate("addwebdialog");
		contextMenu.getChildren().add(menuItem);
		
		menuItem = new MenuItem();
		menuItem.setId("slide"+panel.getId());
		menuItem.setValue("Add Slide Show");
		menuItem.setIcon("ui-icon-transfer-e-w");
		menuItem.setOncomplete("addSlideShowDialog.show()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
		menuItem.setUpdate("addslideshowdialog");
		contextMenu.getChildren().add(menuItem);
		
		menuItem = new MenuItem();
		menuItem.setId("feed"+panel.getId());
		menuItem.setValue("Add Feed");
		menuItem.setIcon("ui-icon-signal-diag");
		menuItem.setOncomplete("addFeedDialog.show()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
		menuItem.setUpdate("addfeeddialog");
		contextMenu.getChildren().add(menuItem);
		
		menuItem = new MenuItem();
		menuItem.setId("prnews"+panel.getId());
		menuItem.setValue("Add PR News");
		menuItem.setIcon("ui-icon-notice");
		menuItem.setOncomplete("update()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
		contextMenu.getChildren().add(menuItem);
		
//		menuItem = new MenuItem();
//		menuItem.setId("prop"+panel.getId());
//		menuItem.setValue("Properties");
//		menuItem.setIcon("ui-icon-gear");
//		menuItem.setOncomplete("addPropertiesDialog.show()");
//		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
//		menuItem.setUpdate("addpropdialog");
//		contextMenu.getChildren().add(menuItem);
			
		menuItem = new MenuItem();
		menuItem.setId("delete"+panel.getId());
		menuItem.setValue("Delete");
		menuItem.setIcon("ui-icon-close");
		menuItem.setOncomplete("update()");
		menuItem.addActionListener(createMethodExpressionActionListener("#{layout.deletePanelListener}"));		
		contextMenu.getChildren().add(menuItem);
		
		contextMenu.setId("context"+panel.getId());
		contextMenu.setFor(panel.getId());
		panelGroup.getChildren().add(panel);
		panelGroup.getChildren().add(draggable);
		panelGroup.getChildren().add(resizable);
		panelGroup.getChildren().add(contextMenu);

		componentMap.put(panel.getClientId(), panel);
		componentMap.put(draggable.getId(), draggable);
		componentMap.put(resizable.getId(), resizable);
		componentMap.put(contextMenu.getId(), contextMenu);
		clientIDMap.put(panel.getId(), panel.getClientId());
	}
	
	public void panelListener(ActionEvent event){	
		String clientID = clientIDMap.get(((ContextMenu)event.getComponent().getParent()).getFor());
		currentPanel = (CCPanel)componentMap.get(clientID);
	
		panelWidth = currentPanel.getWidth();
		panelHeight = currentPanel.getHeight();
		panelTop = currentPanel.getyPosition();
		panelLeft = currentPanel.getxPosition();
		
		if(event.getComponent().getId().contains("prnews")){
			addPrNews();
		} else {
			if(event.getComponent().getId().contains("imagelocal")){
				createImageSelectItemList();
			}
			if(event.getComponent().getId().contains("video")){
				createVideoSelectItemList();
			}
			
			for(UIComponent component : currentPanel.getChildren()){
				if(component.getClass().equals(CCHtmlOutputText.class)){
					text = ((CCHtmlOutputText)component).getText();
					isTextScroll = ((CCHtmlOutputText)component).isScrolling();
					scrollDirection = ((CCHtmlOutputText)component).getScrollingDirection();
					scrollSpeed = ((CCHtmlOutputText)component).getScrollingSpeed();
				}
				if(component.getClass().equals(GraphicImage.class)){
					if(((GraphicImage)component).getUrl().substring(0, 7).equals("/image/")){
						selectedLocalImage = new ImageObject(((GraphicImage)component).getUrl().substring(7, ((GraphicImage)component).getUrl().length()));
					} else {
						imageURL = ((GraphicImage)component).getUrl();
					}
				}
				if(component.getClass().equals(CCMedia.class)){
					if(((CCMedia)component).isLocal()){
						selectedVideo = ((CCMedia)component).getVideoCode();
					} else {
						videoURL = ((CCMedia)component).getVideoCode();
					}			
				}
				if(component.getClass().equals(IFrame.class)){
					webURL = ((IFrame)component).getUrl();
				}
				if(component.getClass().equals(CCImageSwitch.class)){
					imageDualList.getSource().clear();
					imageDualList.getTarget().clear();
					slideShowInterval = ((CCImageSwitch)component).getInterval();
					
					imageDualList.setTarget(((CCImageSwitch)component).getImageList());		
					for(File f : new File("C:\\image\\").listFiles()){
						if(!((CCImageSwitch)component).getImageList().contains(f.getName())){
							imageDualList.getSource().add(f.getName());
						}
					}
				}
				if(component.getClass().equals(CCMarquee.class)){
					for(UIComponent comp : component.getChildren()){
						feedUrl = ((FeedReader)comp).getValue();
					}
				}
			}
		}
	}
	
	public void savePanelgroupProperty(){
		saveState();		
//		initLayout();
	}
	
	public void createLayoutSelectItemList(){
		if(layoutSelectItemList != null) layoutSelectItemList.clear();
		List<Layout> layouts = ServiceDao.getLayoutList();
		
		for(Layout layout : layouts){
			layoutSelectItemList.add(new SelectItem(layout.getId(), layout.getName()));
		}
	}
	
	public void loadLayout(){
		componentMap.clear();
		clientIDMap.clear();
		panelGroup.getChildren().clear();
		
		Layout layout = ServiceDao.getLayout(selectedLayoutID);
		
		width = layout.getWidth();
		height = layout.getHeight();
		bgColor = layout.getBgColor();
		selectedBGImage = new ImageObject(layout.getBgImage());
				
		for(Panel p : layout.getPanels()){
			CCPanel panel = new CCPanel();
			panel.setId(p.getPanelId());
			panel.setWidth(p.getWidth());
			panel.setHeight(p.getHeight());
			panel.setxPosition(p.getxPosition());
			panel.setyPosition(Integer.toString(Integer.parseInt(p.getyPosition())+103));
			panel.setzPosition(p.getzPosition());
			panel.setPrNews(p.isPrNews());
			panel.setStyle("position:absolute;width:"+p.getWidth()+"px;height:"+p.getHeight()+"px;left:"+p.getxPosition()+"px;top:"+Integer.toString(Integer.parseInt(p.getyPosition())+103)+"px;z-index:"+p.getzPosition()+";");
			panel.setStyleClass("stackclass");
			
			if(p.getImages().size() != 0){
				GraphicImage graphicImage = new GraphicImage();
				graphicImage.setUrl(p.getImages().iterator().next().getUrl());
				graphicImage.setStyle("width:100%;height:100%");
				panel.getChildren().add(graphicImage);
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
			if(p.getTexts().size() != 0){
				CCHtmlOutputText outputText = new CCHtmlOutputText();
				
				if(p.getTexts().iterator().next().isScrolling()){
					outputText.setScrolling(p.getTexts().iterator().next().isScrolling());
					outputText.setScrollingDirection(p.getTexts().iterator().next().getScrollingDirection());
					outputText.setScrollingSpeed(p.getTexts().iterator().next().getScrollingSpeed());
					outputText.setText(p.getTexts().iterator().next().getValue());
					String marquee = "<marquee style=\"height:100%;white-space:nowrap\" behavior=\"scroll\" direction=\""+p.getTexts().iterator().next().getScrollingDirection()+"\" scrollamount=\""+p.getTexts().iterator().next().getScrollingSpeed()+"\">";
					outputText.setValue(marquee+p.getTexts().iterator().next().getValue()+"</marquee>");
				} else {
					outputText.setValue(p.getTexts().iterator().next().getValue());
					outputText.setText(p.getTexts().iterator().next().getValue());
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
			if(p.getWebs().size() != 0){
				IFrame frame = new IFrame();
				frame.setUrl(p.getWebs().iterator().next().getValue());
				frame.setStyle("width:100%;height:100%");
				frame.setScrolling("no");
				
				panel.getChildren().add(frame);
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
					
				panel.getChildren().add(marquee);
			}
			
			Draggable draggable = new Draggable();
			Resizable resizable = new Resizable();	
			ContextMenu contextMenu = new ContextMenu();
			MenuItem menuItem;
			
			draggable.setId("drag"+panel.getId());
			draggable.setFor(panel.getId());
			draggable.setContainment("parent");
			draggable.setSnap(true);
			draggable.setSnapMode("outer");
			draggable.setSnapTolerance(5);
			draggable.setStack(".stackclass");
			
			resizable.setId("resize"+panel.getId());
			resizable.setFor(panel.getId());
			resizable.setContainment(true);
			resizable.setHandles("s,e,w,n,se,sw,ne,nw");
			
			menuItem = new MenuItem();
			menuItem.setId("text"+panel.getId());
			menuItem.setValue("Add Text");
			menuItem.setIcon("ui-icon-pencil");
			menuItem.setOncomplete("addTextDialog.show()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));
			menuItem.setUpdate("addtextdialog");
			contextMenu.getChildren().add(menuItem);
			
			menuItem = new MenuItem();
			menuItem.setId("image"+panel.getId());
			menuItem.setValue("Add Link Image");
			menuItem.setIcon("ui-icon-image");
			menuItem.setOncomplete("addImageDialog.show()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));
			menuItem.setUpdate("addlinkimagedialog");
			contextMenu.getChildren().add(menuItem);
			
			menuItem = new MenuItem();
			menuItem.setId("imagelocal"+panel.getId());
			menuItem.setValue("Add Local Image");
			menuItem.setIcon("ui-icon-image");
			menuItem.setOncomplete("addImageDialog2.show()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
			menuItem.setUpdate("addlocalimagedialog");
			contextMenu.getChildren().add(menuItem);
			
//			Submenu submenu = new Submenu();
//			submenu.setLabel("Add Video");
//			submenu.setIcon("ui-icon-video");
			
			menuItem = new MenuItem();
			menuItem.setId("youtube"+panel.getId());
			menuItem.setValue("Add Youtube Video");
			menuItem.setIcon("ui-icon-video");
			menuItem.setOncomplete("addVideoDialog.show()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
			menuItem.setUpdate("addyoutubevideodialog");
			contextMenu.getChildren().add(menuItem);
			
			menuItem = new MenuItem();
			menuItem.setId("video"+panel.getId());
			menuItem.setValue("Add Local Video");
			menuItem.setIcon("ui-icon-video");
			menuItem.setOncomplete("addVideoDialog2.show()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));
			menuItem.setUpdate("addlocalvideodialog");
			contextMenu.getChildren().add(menuItem);
			
//			submenu.getChildren().add(menuItem);		
//			contextMenu.getChildren().add(submenu);
			
			menuItem = new MenuItem();
			menuItem.setId("web"+panel.getId());
			menuItem.setValue("Add Web Page");
			menuItem.setIcon("ui-icon-extlink");
			menuItem.setOncomplete("addWebDialog.show()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
			menuItem.setUpdate("addwebdialog");
			contextMenu.getChildren().add(menuItem);
			
			menuItem = new MenuItem();
			menuItem.setId("slide"+panel.getId());
			menuItem.setValue("Add Slide Show");
			menuItem.setIcon("ui-icon-transfer-e-w");
			menuItem.setOncomplete("addSlideShowDialog.show()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
			menuItem.setUpdate("addslideshowdialog");
			contextMenu.getChildren().add(menuItem);
			
			menuItem = new MenuItem();
			menuItem.setId("feed"+panel.getId());
			menuItem.setValue("Add Feed");
			menuItem.setIcon("ui-icon-signal-diag");
			menuItem.setOncomplete("addFeedDialog.show()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
			menuItem.setUpdate("addfeeddialog");
			contextMenu.getChildren().add(menuItem);
			
			menuItem = new MenuItem();
			menuItem.setId("prnews"+panel.getId());
			menuItem.setValue("Add PR News");
			menuItem.setIcon("ui-icon-notice");
			menuItem.setOncomplete("update()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.panelListener}"));	
			contextMenu.getChildren().add(menuItem);
			
			menuItem = new MenuItem();
			menuItem.setId("delete"+panel.getId());
			menuItem.setValue("Delete");
			menuItem.setIcon("ui-icon-close");
			menuItem.setOncomplete("update()");
			menuItem.addActionListener(createMethodExpressionActionListener("#{layout.deletePanelListener}"));		
			contextMenu.getChildren().add(menuItem);
			
			contextMenu.setId("context"+panel.getId());
			contextMenu.setFor(panel.getId());
				
			panelGroup.getChildren().add(panel);
			panelGroup.getChildren().add(draggable);
			panelGroup.getChildren().add(resizable);
			panelGroup.getChildren().add(contextMenu);
			
			componentMap.put(panel.getClientId(), panel);
			componentMap.put(draggable.getId(), draggable);
			componentMap.put(resizable.getId(), resizable);
			componentMap.put(contextMenu.getId(), contextMenu);
			clientIDMap.put(panel.getId(), panel.getClientId());
		}
	}
	
	public void addPanelProperties(){
		currentPanel.setWidth(panelWidth);
		currentPanel.setHeight(panelHeight);
		currentPanel.setyPosition(panelTop);
		currentPanel.setxPosition(panelLeft);
		currentPanel.setStyle("position:absolute;width:"+currentPanel.getWidth()+"px;height:"+currentPanel.getHeight()+"px;left:"+currentPanel.getxPosition()+"px;top:"+currentPanel.getyPosition()+"px;z-index:"+currentPanel.getzPosition()+";");
			
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addText(){
		CCHtmlOutputText outputText = new CCHtmlOutputText();
		
		if(isTextScroll){
			String marquee = "<marquee style=\"height:100%\" behavior=\"scroll\" direction=\""+scrollDirection+"\" scrollamount=\""+scrollSpeed+"\">";
			String value = marquee+text+"</marquee>";
			outputText.setScrolling(true);
			outputText.setScrollingDirection(scrollDirection);
			outputText.setScrollingSpeed(scrollSpeed);
			outputText.setValue(value);
		} else {
			outputText.setValue(text);
		}
		outputText.setText(text);
		outputText.setEscape(false);
				
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(outputText);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addLinkImage(){	
		GraphicImage graphicImage = new GraphicImage();
		graphicImage.setUrl(imageURL);
		graphicImage.setStyle("width:100%;height:100%");
		
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(graphicImage);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addLocalImage(){	
		GraphicImage graphicImage = new GraphicImage();
		graphicImage.setUrl("/image/"+selectedLocalImage.getImageName());
		graphicImage.setStyle("width:100%;height:100%");
		
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(graphicImage);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addSlideShow(){	
		UIRepeat repeat = new UIRepeat();
		repeat.setValue("value");
		
		for(String c : imageDualList.getTarget()){
			GraphicImage graphicImage = new GraphicImage();
			graphicImage.setValue("image/"+c);
			graphicImage.setStyle("width:100%;height:100%");
			repeat.getChildren().add(graphicImage);
		}
					
		CCImageSwitch imageSwitch = new CCImageSwitch();
		imageSwitch.setId("slide"+new HibernateCurrentTimeIDGenerator().gen().toString());
		imageSwitch.setEffect("fade");
		imageSwitch.setStyle("width:100%;height:100%");
		imageSwitch.setInterval(slideShowInterval);
		imageSwitch.setSlideshowSpeed(slideShowInterval*1000);
		imageSwitch.getChildren().add(repeat);
		imageSwitch.setImageList(imageDualList.getTarget());
		
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(imageSwitch);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addPrNews(){
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
		
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(imageSwitch);
		currentPanel.setPrNews(true);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addYoutubeVideo(){	
		CCMedia media = new CCMedia();
		media.setPlayer("flash");
		media.setValue("http://www.youtube.com/apiplayer?video_id="+videoURL+"&version=3&autoplay=1&loop=1&hd=1&playlist="+videoURL);
		media.setVideoCode(videoURL);
		media.setLocal(false);
		media.setStyle("width:100%;height:100%;");
		
		UIParameter parameter = new UIParameter();
		parameter.setName("wmode");
		parameter.setValue("opaque");
		media.getChildren().add(parameter);
		
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(media);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addLocalVideo(){			
		CCMedia media = new CCMedia();
		media.setValue(getServerURL()+"/video/"+selectedVideo);
		media.setVideoCode(selectedVideo);
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
		
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(media);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addWeb(){
		IFrame frame = new IFrame();
		frame.setUrl(webURL);
		frame.setStyle("width:100%;height:100%;");
		frame.setScrolling("no");
		
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(frame);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void addFeed(){
		FeedReader reader = new FeedReader();
		CCHtmlOutputText text = new CCHtmlOutputText();
		HtmlPanelGrid grid = new HtmlPanelGrid();
		HtmlPanelGroup group = new HtmlPanelGroup();
		Separator separator = new Separator();
		CCMarquee marquee = new CCMarquee();
				
		grid.setColumns(1);
		grid.setCellpadding("3");
		grid.setStyle("width:100%");
		
		reader.setValue(feedUrl);
		reader.setVar("feed");
				
		text.setValueExpression("value", createValueExpression("#{feed.title}", String.class));
		text.setStyle("font-weight:bold");
		group.setLayout("block");
		group.setStyle("margin-top: 5px; background-color: #ADDFFF; border-radius: 5px; width:100%");
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
			
		currentPanel.getChildren().clear();
		currentPanel.getChildren().add(marquee);
		componentMap.put(currentPanel.getClientId(), currentPanel);
		
		savePanelgroupProperty();
	}
	
	public void updateParent(){
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("mainform:panelgroup");
	}
	
	public void clear(){	
		panelGroup.getChildren().clear();
		componentMap.clear();
		clientIDMap.clear();
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
	
	public void initLayout(){
		panelGroup.getChildren().clear();
		panelGroup.getChildren().addAll(componentMap.values());
	}
	
	public void saveState(){
		RequestContext context = RequestContext.getCurrentInstance();
		
		for(UIComponent component : panelGroup.getChildren()){
			if(component.getClass().equals(CCPanel.class)){
				context.execute("getComponentState('"+component.getClientId()+"')");
			}
		}
	}
	
	public void saveLayout(){
		savePanelgroupProperty();
	
		Layout layout = new Layout();
		
		Layout existedlayout = ServiceDao.getLayout(layoutName);
		
		if(existedlayout != null){
			layout = existedlayout;
			ServiceDao.updateLayout(existedlayout);
		} 
		
		layout.setBgColor(bgColor);
		layout.setHeight(height);
		layout.setWidth(width);
		layout.setModifiedTime(new Date());
		layout.setName(layoutName);
		if(selectedBGImage != null){
			layout.setBgImage(selectedBGImage.getImageName());
		} else {
			layout.setBgImage(null);
		}
		Layout thisLayout = ServiceDao.saveLayout(layout);
		
		for(UIComponent component : componentMap.values()){
			if(component.getClass().equals(CCPanel.class)){
				Panel panel = new Panel();
				panel.setPanelId(((CCPanel)component).getId());
				panel.setWidth(((CCPanel)component).getWidth());
				panel.setHeight(((CCPanel)component).getHeight());
				panel.setxPosition(((CCPanel)component).getxPosition());
				panel.setyPosition(Integer.toString(Integer.parseInt(((CCPanel)component).getyPosition())-103));
				panel.setzPosition(((CCPanel)component).getzPosition());
				panel.setPrNews(((CCPanel)component).isPrNews());
				panel.setLayout(thisLayout);

				Panel thisPanel = ServiceDao.savePanel(panel);
				
				for(UIComponent component2 : component.getChildren()){
					if(component2.getClass().equals(GraphicImage.class)){
						Image image = new Image();
						image.setUrl(((GraphicImage)component2).getUrl());
						image.setPanel(thisPanel);
						ServiceDao.saveImage(image);
					}
					if(component2.getClass().equals(CCMedia.class)){
						Video video = new Video();
						video.setValue(((CCMedia)component2).getVideoCode());
						video.setLocal(((CCMedia)component2).isLocal());
						video.setPanel(thisPanel);
						ServiceDao.saveVideo(video);
					}
					if(component2.getClass().equals(IFrame.class)){
						Web web = new Web();
						web.setValue(((IFrame)component2).getUrl());
						web.setPanel(thisPanel);
						ServiceDao.saveWeb(web);
					}
					if(component2.getClass().equals(CCImageSwitch.class)){
						if(!((CCPanel)component).isPrNews()){
							SlideShow slideShow = new SlideShow();
							slideShow.setImages(StringUtils.join(((CCImageSwitch)component2).getImageList(), ';'));
							slideShow.setInterval(((CCImageSwitch)component2).getInterval());
							slideShow.setPanel(thisPanel);
							ServiceDao.saveSlideShow(slideShow);
						}
					}
					if(component2.getClass().equals(CCHtmlOutputText.class)){
						Text text = new Text();
						text.setValue(((CCHtmlOutputText)component2).getText());
						text.setPanel(thisPanel);
						if(((CCHtmlOutputText)component2).isScrolling()){
							text.setScrolling(true);
							text.setScrollingDirection(((CCHtmlOutputText)component2).getScrollingDirection());
							text.setScrollingSpeed(((CCHtmlOutputText)component2).getScrollingSpeed());
						}
						ServiceDao.saveText(text);
					}
					if(component2.getClass().equals(CCMarquee.class)){
						for(UIComponent component3 : component2.getChildren()){
							Feed feed = new Feed();
							feed.setUrl(((FeedReader)component3).getValue());
							feed.setScrollSpeed(((CCMarquee)component2).getSpeed());
							feed.setPanel(thisPanel);
							ServiceDao.saveFeed(feed);
						}
					}
				}
			}
		}	
	}
	
	public void fromRemoteCommand(){	
		FacesContext facesContext = FacesContext.getCurrentInstance();
//		System.out.println("===========================================");
//		System.out.println("ID: "+facesContext.getExternalContext().getRequestParameterMap().get("id"));
//		System.out.println("X: "+facesContext.getExternalContext().getRequestParameterMap().get("x"));
//		System.out.println("Y: "+facesContext.getExternalContext().getRequestParameterMap().get("y"));
//		System.out.println("Z: "+facesContext.getExternalContext().getRequestParameterMap().get("z"));
//		System.out.println("WIDTH: "+facesContext.getExternalContext().getRequestParameterMap().get("width"));
//		System.out.println("HEIGHT: "+facesContext.getExternalContext().getRequestParameterMap().get("height"));	
		
		String id = facesContext.getExternalContext().getRequestParameterMap().get("id");
		String x = facesContext.getExternalContext().getRequestParameterMap().get("x");
		String y = facesContext.getExternalContext().getRequestParameterMap().get("y");
		String z = facesContext.getExternalContext().getRequestParameterMap().get("z");
		String width = facesContext.getExternalContext().getRequestParameterMap().get("width");
		String height = facesContext.getExternalContext().getRequestParameterMap().get("height");
		
//		String style = "width:"+width+"px;height:"+height+"px;left:"+x+"px;top:"+y+"px;position:absolute;z-index:"+z+";";
		
		CCPanel panel = (CCPanel)componentMap.get(id);
		panel.setWidth(width);
		panel.setHeight(height);
		panel.setxPosition(x);
		panel.setyPosition(y);
		panel.setzPosition(z);
		panel.setStyle("position:absolute;width:"+panel.getWidth()+"px;height:"+panel.getHeight()+"px;left:"+panel.getxPosition()+"px;top:"+panel.getyPosition()+"px;z-index:"+panel.getzPosition()+";");
		componentMap.put(panel.getClientId(), panel);
		clientIDMap.put(panel.getId(), panel.getClientId());
	}
	
//	public void panelClosedListener(CloseEvent event){
//		event.getComponent().getChildren().clear();
//		panelGroup.getChildren().remove(getUIComponent("drag"+event.getComponent().getId()));
//		panelGroup.getChildren().remove(getUIComponent("resize"+event.getComponent().getId()));
//		panelGroup.getChildren().remove(getUIComponent("context"+event.getComponent().getId()));
//		panelGroup.getChildren().remove(event.getComponent());
//		
//		componentMap.remove("drag"+event.getComponent().getId());
//		componentMap.remove("resize"+event.getComponent().getId());
//		componentMap.remove("context"+event.getComponent().getId());
//		componentMap.remove(event.getComponent().getClientId());
//		clientIDMap.remove(event.getComponent().getId());
//	}
	
	public void deletePanelListener(ActionEvent event){	
		String clientID = clientIDMap.get(((ContextMenu)event.getComponent().getParent()).getFor());
		CCPanel panel = (CCPanel)componentMap.get(clientID);
		
		panel.getChildren().clear();
		panelGroup.getChildren().remove(getUIComponent("drag"+panel.getId()));
		panelGroup.getChildren().remove(getUIComponent("resize"+panel.getId()));
		panelGroup.getChildren().remove(getUIComponent("context"+panel.getId()));
		panelGroup.getChildren().remove(panel);
		
		componentMap.remove("drag"+panel.getId());
		componentMap.remove("resize"+panel.getId());
		componentMap.remove("context"+panel.getId());
		componentMap.remove(panel.getClientId());
		clientIDMap.remove(panel.getId());
		
		savePanelgroupProperty();
	}
	
	public MethodExpression createMethodExpression(String exp){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		MethodExpression methodExpression = expressionFactory.createMethodExpression(facesContext.getELContext(), exp, String.class, new Class[0]);
		return methodExpression;
	}
	
	public MethodExpressionActionListener createMethodExpressionActionListener(String exp){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		MethodExpressionActionListener actionListener = new MethodExpressionActionListener(expressionFactory.createMethodExpression(facesContext.getELContext(), exp, null, new Class[] {ActionEvent.class}));
		return actionListener;
	}
	
	public ValueExpression createValueExpression(String exp, Class c){
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = context.getELContext();
		Application app = context.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory(); 
		ValueExpression valueExp = elFactory.createValueExpression(elContext, exp, c); 		
		return valueExp;
	}
	
	public UIComponent getUIComponent(String id){
		UIComponent uiComponent = null;
		for(UIComponent component : panelGroup.getChildren()){
			if(component.getId().equals(id)) uiComponent = component;
		}
		return uiComponent;
	}	
	
	
	
	
	
	public HtmlPanelGroup getPanelGroup() {
		return panelGroup;
	}

	public void setPanelGroup(HtmlPanelGroup panelGroup) {
		this.panelGroup = panelGroup;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public HashMap<String, UIComponent> getComponentMap() {
		return componentMap;
	}

	public void setComponentMap(HashMap<String, UIComponent> componentMap) {
		this.componentMap = componentMap;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}
	
	public boolean isTextScroll() {
		return isTextScroll;
	}

	public void setTextScroll(boolean isTextScroll) {
		this.isTextScroll = isTextScroll;
	}

	public String getScrollDirection() {
		return scrollDirection;
	}

	public void setScrollDirection(String scrollDirection) {
		this.scrollDirection = scrollDirection;
	}

	public int getScrollSpeed() {
		return scrollSpeed;
	}

	public void setScrollSpeed(int scrollSpeed) {
		this.scrollSpeed = scrollSpeed;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public HashMap<String, String> getClientIDMap() {
		return clientIDMap;
	}

	public void setClientIDMap(HashMap<String, String> clientIDMap) {
		this.clientIDMap = clientIDMap;
	}

	public String getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(String selectedImage) {
		this.selectedImage = selectedImage;
	}

	public CCPanel getCurrentPanel() {
		return currentPanel;
	}

	public void setCurrentPanel(CCPanel currentPanel) {
		this.currentPanel = currentPanel;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public Long getSelectedLayoutID() {
		return selectedLayoutID;
	}

	public void setSelectedLayoutID(Long selectedLayoutID) {
		this.selectedLayoutID = selectedLayoutID;
	}

	public List<SelectItem> getLayoutSelectItemList() {
		return layoutSelectItemList;
	}

	public void setLayoutSelectItemList(List<SelectItem> layoutSelectItemList) {
		this.layoutSelectItemList = layoutSelectItemList;
	}

	public List<SelectItem> getVideoSelectItemList() {
		return videoSelectItemList;
	}

	public void setVideoSelectItemList(List<SelectItem> videoSelectItemList) {
		this.videoSelectItemList = videoSelectItemList;
	}

	public String getSelectedVideo() {
		return selectedVideo;
	}

	public void setSelectedVideo(String selectedVideo) {
		this.selectedVideo = selectedVideo;
	}

	public String getPanelHeight() {
		return panelHeight;
	}

	public void setPanelHeight(String panelHeight) {
		this.panelHeight = panelHeight;
	}

	public String getPanelWidth() {
		return panelWidth;
	}

	public void setPanelWidth(String panelWidth) {
		this.panelWidth = panelWidth;
	}

	public String getPanelTop() {
		return panelTop;
	}

	public void setPanelTop(String panelTop) {
		this.panelTop = panelTop;
	}

	public String getPanelLeft() {
		return panelLeft;
	}

	public void setPanelLeft(String panelLeft) {
		this.panelLeft = panelLeft;
	}

	public DualListModel<String> getImageDualList() {
		return imageDualList;
	}

	public void setImageDualList(DualListModel<String> imageDualList) {
		this.imageDualList = imageDualList;
	}

	public int getSlideShowInterval() {
		return slideShowInterval;
	}

	public void setSlideShowInterval(int slideShowInterval) {
		this.slideShowInterval = slideShowInterval;
	}

	public ImageObject getSelectedBGImage() {
		return selectedBGImage;
	}

	public void setSelectedBGImage(ImageObject selectedBGImage) {
		this.selectedBGImage = selectedBGImage;
	}

	public List<ImageObject> getImageSelectItemList() {
		return imageSelectItemList;
	}

	public void setImageSelectItemList(List<ImageObject> imageSelectItemList) {
		this.imageSelectItemList = imageSelectItemList;
	}

	public ImageObject getSelectedLocalImage() {
		return selectedLocalImage;
	}

	public void setSelectedLocalImage(ImageObject selectedLocalImage) {
		this.selectedLocalImage = selectedLocalImage;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

}

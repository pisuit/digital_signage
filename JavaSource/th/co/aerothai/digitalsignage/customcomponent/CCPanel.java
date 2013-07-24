package th.co.aerothai.digitalsignage.customcomponent;

import java.io.Serializable;

import org.primefaces.component.panel.Panel;

public class CCPanel extends Panel implements Serializable{
	private String width = "300";
	private String height = "300";
	private String xPosition = "0";
	private String yPosition = "0";
	private String zPosition = "0";
	private boolean isPrNews = false;
	
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
	public String getxPosition() {
		return xPosition;
	}
	public void setxPosition(String xPosition) {
		this.xPosition = xPosition;
	}
	public String getyPosition() {
		return yPosition;
	}
	public void setyPosition(String yPosition) {
		this.yPosition = yPosition;
	}
	public String getzPosition() {
		return zPosition;
	}
	public void setzPosition(String zPosition) {
		this.zPosition = zPosition;
	}
	public boolean isPrNews() {
		return isPrNews;
	}
	public void setPrNews(boolean isPrNews) {
		this.isPrNews = isPrNews;
	}
}

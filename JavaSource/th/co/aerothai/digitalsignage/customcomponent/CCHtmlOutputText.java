package th.co.aerothai.digitalsignage.customcomponent;

import java.io.Serializable;

import javax.faces.component.html.HtmlOutputText;

public class CCHtmlOutputText extends HtmlOutputText implements Serializable{
	private String scrollingDirection = "left";
	private int scrollingSpeed = 5;
	private boolean isScrolling = false;
	private String text;
	
	public String getScrollingDirection() {
		return scrollingDirection;
	}
	public void setScrollingDirection(String scrollingDirection) {
		this.scrollingDirection = scrollingDirection;
	}
	public int getScrollingSpeed() {
		return scrollingSpeed;
	}
	public void setScrollingSpeed(int scrollingSpeed) {
		this.scrollingSpeed = scrollingSpeed;
	}
	public boolean isScrolling() {
		return isScrolling;
	}
	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}

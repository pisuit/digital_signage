package th.co.aerothai.digitalsignage.customcomponent;

import java.io.Serializable;

import javax.faces.component.html.HtmlOutputText;

public class CCVideo extends HtmlOutputText implements Serializable{
	private String videoName;

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
}

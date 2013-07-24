package th.co.aerothai.digitalsignage.customcomponent;

import java.io.Serializable;

import org.primefaces.component.media.Media;

public class CCMedia extends Media implements Serializable{
	private String videoCode;
	private boolean isLocal = true;

	public String getVideoCode() {
		return videoCode;
	}

	public void setVideoCode(String videoCode) {
		this.videoCode = videoCode;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
}

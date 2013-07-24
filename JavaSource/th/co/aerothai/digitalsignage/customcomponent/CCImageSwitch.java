package th.co.aerothai.digitalsignage.customcomponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.component.imageswitch.ImageSwitch;

public class CCImageSwitch extends ImageSwitch implements Serializable{
	private List<String> imageList = new ArrayList<String>();
	private int interval = 3;

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
}

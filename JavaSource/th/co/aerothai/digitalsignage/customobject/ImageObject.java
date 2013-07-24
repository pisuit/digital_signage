package th.co.aerothai.digitalsignage.customobject;

import java.io.Serializable;

public class ImageObject implements Serializable{
	private String imageName;
	
	public ImageObject(){
		
	}
	
	public ImageObject(String image){
		this.imageName = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String toString(){
		return imageName;
	}
	
	public boolean equals(Object obj){
		if(obj.toString().equals(imageName)){
			return true;
		} else {
			return false;
		}
	}
}

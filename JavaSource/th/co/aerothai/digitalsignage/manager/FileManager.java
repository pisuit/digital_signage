package th.co.aerothai.digitalsignage.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;

@ManagedBean(name="file")
@ViewScoped
public class FileManager implements Serializable{
	private List<String> imageList = new ArrayList<String>();
	private String selectedImage;
	private List<String> videoList = new ArrayList<String>();
	private String selectedVideo;
	
	public FileManager(){
		createImageList();
		createVideoList();
	}
	
	private void createImageList(){
		imageList.clear();
		for(File f : new File("C:\\image\\").listFiles()){
			imageList.add(f.getName());
		}
	}
	
	private void createVideoList(){
		videoList.clear();
		for(File f : new File("C:\\video\\").listFiles()){
			videoList.add(f.getName());
		}
	}
	
	public void deleteImage(){
		new File("C:\\image\\"+selectedImage).delete();
		createImageList();
		selectedImage = null;
	}
	
	public void deleteVideo(){
		new File("C:\\video\\"+selectedVideo).delete();
		createVideoList();
		selectedVideo = null;
	}
	
	public void videoUploadListener(FileUploadEvent event){
		try {
			BufferedInputStream bis = new BufferedInputStream(event.getFile().getInputstream());
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("C:\\video\\"+event.getFile().getFileName())));
			int data;
			while((data = bis.read()) != -1) bos.write(data);
			bis.close();
			bos.close();
			
			createVideoList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	public void imageUploadListener(FileUploadEvent event){
		try {
			BufferedInputStream bis = new BufferedInputStream(event.getFile().getInputstream());
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("C:\\image\\"+event.getFile().getFileName())));
			int data;
			while((data = bis.read()) != -1) bos.write(data);
			bis.close();
			bos.close();
			
			createImageList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
	public String getSelectedImage() {
		return selectedImage;
	}
	public void setSelectedImage(String selectedImage) {
		this.selectedImage = selectedImage;
	}

	public List<String> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<String> videoList) {
		this.videoList = videoList;
	}

	public String getSelectedVideo() {
		return selectedVideo;
	}

	public void setSelectedVideo(String selectedVideo) {
		this.selectedVideo = selectedVideo;
	}

}

package th.co.aerothai.digitalsignage.converter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import th.co.aerothai.digitalsignage.customobject.ImageObject;

@FacesConverter(value="imageconverter")
public class ImageConverter implements Converter{

	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		return new ImageObject(arg2);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		// TODO Auto-generated method stub
		return object.toString();
	}

}

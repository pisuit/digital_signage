package th.co.aerothai.digitalsignage.customcomponent;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class CCMarquee extends HtmlOutputText{
	private int speed = 2;
	private String direction = "up";
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		// TODO Auto-generated method stub
		ResponseWriter writer = context.getResponseWriter();
		for(UIComponent kid : getChildren()){
			writer.startElement("marquee", this);
			writer.writeAttribute("style", "height:100%;font-size:20px", null);
			writer.writeAttribute("scrollamount", speed, null);
			writer.writeAttribute("direction", direction, null);
			kid.encodeAll(context);
			writer.endElement("marquee");
		}
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}

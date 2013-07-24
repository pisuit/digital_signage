package th.co.aerothai.digitalsignage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import th.co.aerothai.digitalsignage.dao.PRDao;
import th.co.aerothai.digitalsignage.model.pr.ActiPrsc;

/**
 * Servlet implementation class RssFeeds
 */
@WebServlet(name="ActivityFeed", urlPatterns={"/activity_feed.xml"})
public class ActivityFeed extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActivityFeed() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/xml;charset=UTF-8");
		PrintWriter servlet = response.getWriter();
		
		try {	
			 Element rssRoot = new Element("rss");
			 rssRoot.setAttribute("version", "2.0");
			 
			 Document feed = new Document();
			 feed.setRootElement(rssRoot);
			 
			 Element channel = new Element("channel");
			 rssRoot.addContent(channel);
			 
			 channel.addContent(new Element("title").addContent("Title of the Feed"));
             channel.addContent(new Element("description"). addContent("Description of the entire feed"));
             channel.addContent(new Element ("link").addContent ("http://staff.aerothai.co.th"));
             
             addRssItems (channel);
             
             XMLOutputter output = new XMLOutputter();
             output.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
             output.output(feed, servlet);
		} finally {
            servlet.flush();
            servlet.close();
        }
	}
	
	void addRssItems(Element channel){
		List<ActiPrsc> list = PRDao.getActivity();
		 SimpleDateFormat f =  new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		 SimpleDateFormat time =  new SimpleDateFormat("HH:mm");
		
		 for(ActiPrsc prsc : list){
			 Element item = new Element("item");
			 item.addContent(new Element("title").addContent(time.format(prsc.getActiOrganization().getAct_start())));
			 item.addContent(new Element("description").addContent(prsc.getAct_detail()));
			 item.addContent(new Element("pubDate").addContent(f.format(prsc.getCreateDate())));
			 channel.addContent(item);
		 }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

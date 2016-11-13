package stats.bis.org;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class StatisticsExplorer {
	
	public static String baseURL = "http://stats.bis.org";
	public ArrayList<String> listOfBaseLinks = new ArrayList<>();
	
	public StatisticsExplorer(){
		try {
			//those links are not accessible via scraper/crawler due to JavaScript execution on page
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/LBS.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/CBS.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/SEC.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/DER.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/GLI.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/CRE.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/CTG.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/DSR.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/SPP.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/CPI.html");
			listOfBaseLinks.add("http://stats.bis.org/statx/toc/EER.html");

			//String source = ScrapeBIS.sendGet("http://stats.bis.org/statx/toc/LBS.html");
			//Document d = Jsoup.parse(source);
			//System.out.println(d.toString());
			//baseURL=d.select("body > div.toc-detail > ul:nth-child(4) > li:nth-child(2) > a").toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
	}
	
	public static ArrayList<String> getLinks(Document docc){

		ArrayList<String> list = new ArrayList<>();
		Elements elements = docc.select("#toc > ul");
		//System.out.println(elements);
		
		for (Element el : elements) {

			//Elements links = el.children();
			
			//for (Element element : links) {
				
				list.add(baseURL + el.attr("data-href").toString());
			//}
		}
		
		return list;
		
	}
}
		

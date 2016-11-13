package stats.bis.org;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeBIS {

	public static final String USER_AGENT = "Mozilla/5.0";
	public static String baseUrl = "http://stats.bis.org";

	public static String sendGet(String urlUrl) throws Exception {

		String url = urlUrl;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();

	}


	public static void main(String[] args) {

		StatisticsExplorer se = new StatisticsExplorer();
		ArrayList<String> li = makeLinks(se.listOfBaseLinks);
		downloadCSV(li);
//		for (String string : li) {
//			System.out.println(string);
//		}

	}

	private static void downloadCSV(ArrayList<String> makeLinks) {
		int i =0;
		for (String downloadLink : makeLinks) {
			try {
				URL website = new URL(downloadLink+"f=csv");
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				
				FileOutputStream fos = new FileOutputStream(fileName(downloadLink+"f=csv"));
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				i++;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.err.println("file not exist on :" + downloadLink+"f=csv");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	//http://stats.bis.org/statx/srs/table/A6?c=SV&p=
	public static String fileName(String s){
		String  pom = s.replaceAll("http://stats.bis.org/statx/srs/","");
				pom = pom.replaceAll("/"," ");
				pom = pom.replaceAll("\\?c=","-");
				pom = pom.replaceAll("\\?f=csv","");
				pom = pom.replaceAll("&f=csv","");		
		return pom+".csv";
	}


	public static ArrayList<String> makeLinks(ArrayList<String> listOfBaseLinks){

		ArrayList<String> linksToDownload = new ArrayList<>();

		for (String link : listOfBaseLinks) {

			String pageSource;
			try {
				pageSource = sendGet(link);
				Document doc =  Jsoup.parse(pageSource);

				Elements elements = doc.getElementsByTag("li");

				for (Element element : elements) {

					for (Element e : element.getElementsByTag("a")) {
						//	System.out.println("Link added to download list:" +e.attr("href")); 
						//linksToDownload.add(baseUrl+e.attr("href"));
						//those link have multiple new link,when opened.
						if (e.attr("href").contains("a5")||
								e.attr("href").contains("a6")||
								e.attr("href").contains("a7")||
								e.attr("href").contains("b4")||
								e.attr("href").contains("c3")	) {
							String pageSource2;
							try {
								pageSource2 = sendGet(baseUrl + e.attr("href"));
								Document doc2 =  Jsoup.parse(pageSource2);

								Elements elements2 = doc2.getElementsByTag("a");
								
								for (Element elem : elements2) {
										linksToDownload.add(baseUrl+ elem.attr("href").replaceAll("&p=", "")+"&"); 
									
								} 
							}catch (Exception e1) {
								//e1.printStackTrace();
							}

						}else{
							//System.out.println(baseUrl+ e.attr("href")+"?");
							linksToDownload.add(baseUrl+ e.attr("href")+"?"); 

						}
					}

				}
			} catch (Exception e) {

				//e.printStackTrace();
			}
		} 
		if (linksToDownload.removeAll(remoweBadLinks())) {
			return linksToDownload;
		}
		return linksToDownload;
	}

	public static ArrayList<String> remoweBadLinks(){
		ArrayList<String> list = new ArrayList<>();
		
			list.add("http://stats.bis.org/http://www.bis.org&");
			list.add("http://stats.bis.org/http://twitter.com/BIS_org&");
			list.add("http://stats.bis.org/http://www.youtube.com/user/bisbribiz&");
			list.add("http://stats.bis.org/http://www.bis.org/rss/index.htm&");
			list.add("http://stats.bis.org/http://www.bis.org/emailalert.htm&");
			list.add("http://stats.bis.org/http://www.bis.org/sitemap/index.htm&");
			list.add("http://stats.bis.org/http://www.bis.org/about/contact.htm&");
			list.add("http://stats.bis.org//statx/&");
			list.add("http://stats.bis.org//statx/help_table.html&");
			list.add("http://stats.bis.org//statx/toc/LBS.html&");
			list.add("http://stats.bis.org//statx/toc/SEC.html&");
			list.add("http://stats.bis.org//statx/toc/CBS.html&");
			list.add("http://stats.bis.org//statx/toc/DER.html&");
			list.add("http://stats.bis.org//statx/toc/GLI.html&");
			list.add("http://stats.bis.org//statx/toc/CRE.html&");
			list.add("http://stats.bis.org//statx/toc/CTG.html&");
			list.add("http://stats.bis.org//statx/toc/DSR.html&");
			list.add("http://stats.bis.org//statx/toc/SPP.html&");
			list.add("http://stats.bis.org//statx/toc/CPI.html&");
			list.add("http://stats.bis.org//statx/toc/EER.html&");
			list.add("stats.bis.orghttp");
			
			return list;
	}
}

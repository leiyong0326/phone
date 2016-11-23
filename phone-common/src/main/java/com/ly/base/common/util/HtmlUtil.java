package com.ly.base.common.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.ly.base.common.model.HtmlModel;
import com.ly.base.common.system.SystemConfig;

/**
 * Html文件生成器
 * 
 * @author LeiYong
 *
 */
public class HtmlUtil {
	
	
	/**
	 * 考虑如何生成一个页面,需要生成哪些元素,根据什么来生成
	 * 
	 * @return
	 */
	public static String generateHtml(HtmlModel html) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>" + html.getTitle() + "</title>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=no\" />");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div align=\"center\" id=\"contentId\">");
		sb.append(html.getContent());
		sb.append("</div></body></html>");
		return sb.toString();
	}

	
	public static void main(String[] args) {
		String content=HtmlUtil.getHTML("http://cy-carimg-files.b0.upaiyun.com//parts_detail_html/6f1c801469764959983.html");
		System.out.println(content);
	}
	
	 public static String getHTML(String url){
		 // 获取指定URL的网页，返回网页内容的字符串，然后将此字符串存到文件即可 
		 try { 
			 URL newUrl = new URL(url);
			 URLConnection connect = newUrl.openConnection();
			 connect.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			 DataInputStream dis = new DataInputStream(connect.getInputStream()); 
			 BufferedReader in = new BufferedReader(new InputStreamReader(dis,SystemConfig.CHARSET));
			 String html = ""; 
			 String readLine = null;
			 while((readLine = in.readLine()) != null){ 
				 html = html + readLine; 
			} in.close();
			return html; 
		}catch (MalformedURLException me){ 
			System.out.println("MalformedURLException" + me); 
		}catch (IOException ioe){ 
			System.out.println("ioeException" + ioe);
		} return null; 
	}
}

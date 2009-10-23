package ru.styskin.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
	
	private String url;
	private String title;
	
	public Downloader(String title, String url) {
		this.title = title;
		this.url = url;
	}
	
	public void run() {
		try {
			Pattern pattern = Pattern.compile("<h1.*<\\/h1>");			
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream(), "utf-8"));
			PrintWriter out = null;
			String s;
			boolean flag = false;
			
			while((s = in.readLine()) != null) {
				Matcher matcher = pattern.matcher(s);
				if(matcher.find()) {
					s = matcher.group();
					s = s.substring(s.indexOf('>') + 1);
					s = s.substring(0, s.indexOf("</"));
					s = s.substring(1, s.indexOf("»"));
					String fileName = title + "/" + s + ".txt";
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName))));
				}
				if(s.indexOf("descr_full") >= 0 && out != null) {
					flag = true;
				}
				if(flag) {
					if(s.indexOf("<br />") >= 0) {
						s = s.substring(s.indexOf("<br />") + "<br />".length());
					}
					out.println(s.trim());
					if (s.indexOf("</div>") >= 0) {
						break;						
					}					
				}
			}			
			in.close();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}

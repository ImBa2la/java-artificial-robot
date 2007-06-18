package ru.styskin.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
	
	private String url; 
	
	public Downloader(String url) {
		this.url = url;
	}
	
	public void run() {
		try {
			Pattern pattern = Pattern.compile("<h1>.*<\\/h1>");			
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			PrintWriter out = null;
			String s;
			boolean flag = false;
			
			while((s = in.readLine()) != null) {
				Matcher matcher = pattern.matcher(s);
				if(matcher.find()) {
					s = matcher.group();
					s = s.replace("<h1>", "").replace("</h1>", "").trim();
					out = new PrintWriter(new BufferedWriter(new FileWriter(s + ".txt")));
				}
				if(s.indexOf("<pre>") >= 0 && out != null) {
					flag = true;
				}
				if(flag) {
					if(s.indexOf("<pre>") >= 0) {
						s = s.substring(s.indexOf("<pre>") + "<pre>".length());
					}
					if(s.indexOf("</pre>") >= 0) {
						s = s.substring(0, s.indexOf("</pre>"));
						flag = false;
					}
					out.println(s.trim());					
				}
			}			
			in.close();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}

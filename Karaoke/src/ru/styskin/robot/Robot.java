package ru.styskin.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Robot implements Runnable {
	
	private String head;
	private String enterance; 
	
	public Robot(String head, String entarance) {
		this.head = head;
		this.enterance = entarance;		
	}
	
	public static void main(String[] args) {
		new Robot("http://www.karaoke.ru", "/base/362.htm").run();		
	}
	                            
	public void run() {
		
		Pattern pattern = Pattern.compile("<a href=\\/song\\/\\d{1,5}\\.htm");
		try {
			URL url = new URL(head + enterance);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String s;
			while((s = in.readLine()) != null) {
				Matcher matcher = pattern.matcher(s);
				if(matcher.find()) {
					s = matcher.group();
					s = s.substring(s.indexOf('=')+1);
					new Downloader(head + s).run();					
				}				
			}			
			in.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

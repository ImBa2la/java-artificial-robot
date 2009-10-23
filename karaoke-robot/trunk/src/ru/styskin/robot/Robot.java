package ru.styskin.robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Robot implements Runnable {
	
	private String head;
	private String enterance; 
	private String title;
	
	public Robot(String head, String entarance, String title) {
		this.head = head;
		this.enterance = entarance;
		this.title = title;
	}
	
	public static void main(String[] args) {
		new Robot("http://www.karaoke.ru", "/catalog/artist_songs/8881", "Би-2").run();		
	}
	                            
	public void run() {
		File dir = new File(title);
		dir.mkdir();		
		Pattern pattern = Pattern.compile("<a href=\"\\/catalog\\/song\\/\\d{1,5}\"");
		try {
			URL url = new URL(head + enterance);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String s;
			while((s = in.readLine()) != null) {
				Matcher matcher = pattern.matcher(s);
				if(matcher.find()) {
					s = matcher.group();
					s = s.substring(s.indexOf('=')+1);
					s = s.substring(1, s.length() - 1);
					new Downloader(title, head + s).run();					
				}				
			}			
			in.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

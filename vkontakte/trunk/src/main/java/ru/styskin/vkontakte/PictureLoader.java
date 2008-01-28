package ru.styskin.vkontakte;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import ru.styskin.utils.Triple;
import ru.styskin.vkontakte.dao.UserDao;

public class PictureLoader extends TimerTask {
	
	private static final Logger logger = Logger.getLogger(PictureLoader.class); 
	
	private String path = "pictures";
	private UserDao userDao;
	
	public void run() {
		Triple<Integer, String, String> path = userDao.next();
		if(path != null) {
			try {
				if(path.getC() != null) {
					URL url = new URL(path.getC());
					BufferedInputStream in = new BufferedInputStream(url.openConnection().getInputStream());
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.path + "/" + path.getB()));
					int c;
					while( (c = in.read()) != -1) {
						out.write(c);										
					}
					in.close();
					out.close();
				}
				userDao.setDone(path.getA());
			} catch (Throwable e) {
				logger.error("", e);
			}			
		}
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}

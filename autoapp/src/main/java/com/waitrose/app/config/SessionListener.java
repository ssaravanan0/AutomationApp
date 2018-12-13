package com.waitrose.app.config;




import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class SessionListener implements HttpSessionListener {

	private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

	
@Override
public void sessionCreated(HttpSessionEvent event) {
	logger.info("==== Session is created ====");
event.getSession().setMaxInactiveInterval(5*60);
}
@Override
public void sessionDestroyed(HttpSessionEvent event) {
	logger.info("==== Session is destroyed ====");

}
}
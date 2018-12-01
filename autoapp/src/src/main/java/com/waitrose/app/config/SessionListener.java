package com.waitrose.app.config;




import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
public class SessionListener implements HttpSessionListener {

long starttime ;
long endtime;
@Override
public void sessionCreated(HttpSessionEvent event) {
	starttime = System.nanoTime();
System.out.println("==== Session is created ====" + starttime);
//event.getSession().setMaxInactiveInterval(5*60);
event.getSession().setMaxInactiveInterval(5*60);

}
@Override
public void sessionDestroyed(HttpSessionEvent event) {
System.out.println("==== Session is destroyed ===="+ ((System.nanoTime() - starttime)/1000000) +" milli secs..");

}
}
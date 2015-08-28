package wx;

import java.net.URLEncoder;

//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.webapp.WebAppContext;

public class WxServerStarter {
	
	public static void main(String[] args)throws Exception {
	    /*
		try {
		    String file = WxServerStarter.class.getResource("").getFile();
	        int pos = file.indexOf("/target/");
	        String baseDir = file.substring(0, pos);
	        String classesDir = baseDir + "/target/classes/";
	        String appDir = baseDir + "/src/main/webapp/";
	        System.out.println("baseDir:" + baseDir + ", appPath:" + appDir + ", classesDir:" + classesDir);
	        
	        Server server = new Server(8080);
	        WebAppContext webapp = new WebAppContext();
	        webapp.setContextPath("/");
	        webapp.setResourceBase(appDir);
	        server.setHandler(webapp);
	        server.start();
	        System.out.println("JettyServer ready!");
	        server.join();
		} catch (Exception e) {
			System.out.println("JettyServer start error!");
			e.printStackTrace();
		}
		*/
	    System.out.println(URLEncoder.encode("100圆=4万个", "utf-8"));
	}

}

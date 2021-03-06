package com.fourture.mark;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import org.apache.turbine.util.BrowserDetector;

public class ParseUserAgent {

	private String browserName;
	private float browserVersion;
	private String platform;
	private String platformVersion;
	private String device;
	
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlatformVersion() {
		return platformVersion;
	}

	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	private boolean mobile;
	
	public ParseUserAgent( String userAgent)
	{
		
		try {
					
			// New method
			// Get an UserAgentStringParser and analyze the requesting client
			UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
			ReadableUserAgent agent = parser.parse( userAgent);		
			
			// Browser Detector crashes on some UserAgents, handle them here
			String browserVersionStr;
			
			//Chromeframe: 2 tyes of useragents known
			//User-Agent: Mozilla/5.0 (Windows NT 5.1; chromeframe/14.0.835.186) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.186 Safari/535.1
			//User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; chromeframe/14.0.835.163; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)
			if ( (userAgent.contains( "chromeframe")) && !(userAgent.contains( "MSIE"))) {
				browserName = "GCF";
				// get version only major and minor. Example 3.6.3 : we only need 3.6
				int i= userAgent.indexOf("chromeframe");
				browserVersionStr = userAgent.substring( i + 12, userAgent.length());
				//i= browserVersion.indexOf(",");
				browserVersionStr = browserVersionStr.substring( 0, browserVersionStr.indexOf("."));
				browserVersion = Float.valueOf( browserVersionStr);
				
				platform = "Windows";
				platformVersion = parsePlatformVersion( userAgent);
				device = parseDevice( userAgent);
				return;
			}
			//User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; chromeframe/14.0.835.163; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)
			if (userAgent.contains( "chromeframe")) {
				browserName = "IE GCF";
				// get version only major and minor. Example 3.6.3 : we only need 3.6
				int i= userAgent.indexOf("MSIE");
				browserVersionStr = userAgent.substring( i + 5, userAgent.length());
				//i= browserVersion.indexOf(",");
				browserVersionStr = browserVersionStr.substring( 0, 3);
				browserVersion = Float.valueOf( browserVersionStr);			
				platform = agent.getOperatingSystem().getFamilyName();
				platformVersion = agent.getOperatingSystem().getName();
				normalize();
				return;
			}
			
			//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240
 			if (userAgent.contains( "Edge")) {
 				browserName = "Edge";
 				int i= userAgent.indexOf("Edge");
 				browserVersionStr = userAgent.substring( i + 5, userAgent.length());
 				browserVersionStr = browserVersionStr.substring( 0, 3);
 				browserVersion = Float.valueOf( browserVersionStr);			
 				platform = agent.getOperatingSystem().getFamilyName();
 				platformVersion = agent.getOperatingSystem().getName();
 				normalize();
 				return;
 			}
			
			browserName = agent.getName();
			browserVersionStr = agent.getVersionNumber().getMajor();
			browserVersion = Float.valueOf( browserVersionStr);
			platform = agent.getOperatingSystem().getFamilyName();
			platformVersion = agent.getOperatingSystem().getName();
			
			//fix to be able to detect Windows 10
			if (platformVersion.equals("Windows"))
				if (userAgent.contains("Windows NT 10.0")) platformVersion = "10";
			
			//Actually, this will always return true with the new browser detector software
			if ( browserName != null ){ 
				normalize();
				
				return;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private void normalize() {
		if ( "Windows XP".equals( platformVersion)) platformVersion = "XP";
		if ( "Windows 7".equals( platformVersion)) platformVersion = "7";
		if ( "Windows 8".equals( platformVersion)) platformVersion = "8";
		if ( "Windows Vista".equals( platformVersion)) platformVersion = "Vista"; 
		
		if ( "IE".equals( browserName)) browserName = "MSIE";
	}
	
	private String parseDevice(String userAgent) {
		if (userAgent.contains("iPhone")) return "iPhone";
		if (userAgent.contains("iPad")) return "iPad";
		if (userAgent.contains("N900")) return "N900";
		return "Unknown";
	}

	public String parsePlatformVersion( String userAgent)
	{
	
		if (platform.equals("Windows"))
		{
			if ( userAgent.contains("Windows NT 6.1")) return "7";
			if ( userAgent.contains("Windows NT 6.0")) return "Vista";
			if ( userAgent.contains("Windows NT 5.2")) return "Server 2003;Windows XP x64 Edition";
			if ( userAgent.contains("Windows NT 5.1")) return "XP";
			if ( userAgent.contains("Windows NT 5.01")) return "2000, Service Pack 1 (SP1)";
			if ( userAgent.contains("Windows NT 5.0")) return "2000";
			if ( userAgent.contains("Windows NT 4.0")) return "NT 4.0";
			if ( userAgent.contains("Windows 98; Win 9x 4.90")) return "Millennium Edition (Windows Me)";
			if ( userAgent.contains("Windows 98")) return "98";
			if ( userAgent.contains("Windows 95")) return "95";
			if ( userAgent.contains("Windows CE")) return "CE";
			
		}
		if (platform.equals("Macintosh"))
		{
			if ( userAgent.contains("Mac OS X 10_0")) return "OS X 10.0";
			if ( userAgent.contains("Mac OS X 10_1")) return "OS X 10.1";
			if ( userAgent.contains("Mac OS X 10_2")) return "OS X 10.2";
			if ( userAgent.contains("Mac OS X 10_3")) return "OS X 10.3";
			if ( userAgent.contains("Mac OS X 10_4")) return "OS X 10.4";
			if ( userAgent.contains("Mac OS X 10_5")) return "OS X 10.5";
			if ( userAgent.contains("Mac OS X 10_6")) return "OS X 10.6";
			
		}
		return "Unknown";
	}
	
	public boolean isMobile() {
		return mobile;
	}

	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}

	
	public void validate( boolean check, String message)
	{
		if (! check)
			System.out.println("Error in: " + message);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String useragent = "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";
		ParseUserAgent p = new ParseUserAgent( useragent);
		
		// TODO Auto-generated method stub
		useragent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3,gzip(gfe)";
		p = new ParseUserAgent( useragent);
		if (! p.getBrowserName().equals("Firefox")) System.out.println("Error parsing:" + useragent);
		if ( p.getBrowserVersion() != 3F ) System.out.println("Error parsing:" + useragent);
		if ( !p.platformVersion.equals("XP")) System.out.println("Error parsing:" + useragent);
		//p.validate( p.device.equals("Unknown"), useragent);
		
		useragent ="Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";
		p = new ParseUserAgent( useragent);
		//if (! p.getBrowserName().equals("Firefox")) System.out.println("Error parsing:" + useragent);
		//if ( p.getBrowserVersion() != 3.5F ) System.out.println("Error parsing:" + useragent);
		//if ( p.isMobile() != true ) System.out.println("Error parsing:" + useragent);
		//p.validate( p.device.equals("N900"), useragent);
		
		
		useragent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.1.249.1064 Safari/532.5,gzip(gfe)";
		p = new ParseUserAgent( useragent);
		if (! p.getBrowserName().equals("Chrome")) System.out.println("Error parsing:" + useragent);
		if ( p.getBrowserVersion() != 4.1F ) System.out.println("Error parsing:" + useragent);
		if ( p.isMobile() != false ) System.out.println("Error parsing:" + useragent);
		
		useragent = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A537a Safari/419.3";
		p = new ParseUserAgent( useragent);
		if (! p.getBrowserName().equals("Safari")) System.out.println("Error parsing:" + useragent);
		if ( p.getBrowserVersion() != 3.0F ) System.out.println("Error parsing:" + useragent);
		if ( p.isMobile() != true ) System.out.println("Error parsing:" + useragent);
		p.validate( p.device.equals("iPhone"), useragent);
		
		useragent = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_1_3 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7E18 Safari/528.16,gzip(gfe)";
		p = new ParseUserAgent( useragent);
		if (! p.getBrowserName().equals("Safari")) System.out.println("Error parsing:" + useragent);
		if ( p.getBrowserVersion() != 4.0F ) System.out.println("Error parsing:" + useragent);
		if ( p.isMobile() != true ) System.out.println("Error parsing:" + useragent);
		p.validate( p.device.equals("iPhone"), useragent);
		
		useragent = "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; nl-nl) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B367 Safari/531.21.10,gzip(gfe)";
		p = new ParseUserAgent( useragent);
		if (! p.getBrowserName().equals("Safari")) System.out.println("Error parsing:" + useragent);
		if ( p.getBrowserVersion() != 4.0F ) System.out.println("Error parsing:" + useragent);
		if ( p.isMobile() != false ) System.out.println("Error parsing:" + useragent);
		p.validate( p.device.equals("iPad"), useragent);
		
		useragent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_3; nl-nl) AppleWebKit/531.22.7 (KHTML, like Gecko) Version/4.0.5 Safari/531.22.7,gzip(gfe)";
		p = new ParseUserAgent( useragent);
		if (! p.getBrowserName().equals("Safari")) System.out.println("Error parsing:" + useragent);
		if ( p.getBrowserVersion() != 4.0F ) System.out.println("Error parsing:" + useragent);
		if ( p.isMobile() != false ) System.out.println("Error parsing:" + useragent);
	
		useragent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1),gzip(gfe)";
		p = new ParseUserAgent( useragent);
		p.validate( p.platformVersion.equals("XP"), useragent);
		p.validate( p.browserVersion ==6, useragent);
		
		useragent = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; OfficeLiveConnector.1.4; OfficeLivePatch.1.3; MS-RTC LM 8; .NET4.0C; .NET4.0E; InfoPath....";
		p = new ParseUserAgent( useragent);
		p.validate( p.platformVersion.equals("7"), useragent);
		p.validate( p.browserVersion ==8, useragent);
		
		useragent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_3; en-US) AppleWebKit/533.4 (KHTML, like Gecko) Chrome/5.0.375.55 Safari/533.4,gzip(gfe)";
		p = new ParseUserAgent( useragent);
		p.validate( p.platformVersion.equals("OS X 10.6"), useragent);
		p.validate( p.browserVersion ==5.0, useragent);
		
		useragent = "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; chromeframe/14.0.835.163; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";
		p = new ParseUserAgent( useragent);
		if (! p.getBrowserName().equals("GCF")) System.out.println("Error parsing:" + useragent);
		if ( p.getBrowserVersion() != 14.0F ) System.out.println("Error parsing:" + useragent);
		if ( p.isMobile() != false ) System.out.println("Error parsing:" + useragent);
		
		useragent = "User-Agent: Mozilla/5.0 (Windows NT 5.1; chromeframe/14.0.835.186) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.186 Safari/535.1";
		p = new ParseUserAgent( useragent);
		if (! p.getBrowserName().equals("GCF")) System.out.println("Error parsing:" + useragent);
		if ( p.getBrowserVersion() != 14.0F ) System.out.println("Error parsing:" + useragent);
		if ( p.isMobile() != false ) System.out.println("Error parsing:" + useragent);
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public float getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(float browserVersion) {
		this.browserVersion = browserVersion;
	}


}

package wx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import wx.util.FileMonitor;
import wx.util.Util;

public class WxEnum {
	
	private final static Logger log = LoggerFactory.getLogger(WxEnum.class);
	
	public static enum Name {
		Gender,
		Area,
		Category,
		AdminArea,
		Roles
	}

	private static Map<String, Map<Integer, String>> items = new HashMap<String, Map<Integer, String>>();
	private static Map<String, Map<String, Integer>> extValueItems = new HashMap<String, Map<String, Integer>>();
	private static Map<String, String> textTemplets = new HashMap<String, String>();
	
	private static FileMonitor fileMonitor;
	static {
		try {
			String xml = Util.locateFile("constants.xml");
			if (xml != null) {
				fileMonitor = new FileMonitor(xml, 60000);
				fileMonitor.addListener(new FileMonitor.Listener() {
					
					@Override
					public void onFileMofity(String file, long ts) {
						new FileSystemXmlApplicationContext(file);
						log.info(" cfg update! textTemplets:" + textTemplets);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setTextTemplets(Map<String, String> textTemplets) {
		WxEnum.textTemplets = textTemplets;
	}

	public static String getTextTemplet(String key) {
		return textTemplets.get(key);
	}
	
	public static Map<Integer, String> getItems(Name name) {
		return getItems(name.toString());
	}
	
	public static Map<Integer, String> getItems(String name) {
		Map<Integer, String> map = items.get(name);
		if (map == null)
			map = Collections.emptyMap();
		return map;
	}
	
	public static String getValue(Name name, int key) {
		return getValue(name.toString(), key);
	}
	
	public static String getValue(String name, int key) {
		String v = getItems(name).get(key);
		return (v == null ? "" : v);
	}
	
	public static int getKey(Name name, String value) {
		return getKey(name.toString(), value);
	}
	
	public static int getKey(String name, String value) {
		return getKey(name, value, 0);
	}
	
	public static int getKey(Name name, String value, int defaultKey) {
		return getKey(name.toString(), value, defaultKey);
	}
	
	public static int getKey(String name, String value, int defaultKey) {
		if (value == null)
			return defaultKey;
		Map<Integer, String> map = getItems(name);
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			if (value.equals(entry.getValue()) || value.indexOf(entry.getValue()) >= 0)
				return entry.getKey();
		}
		
		Map<String, Integer> exts = extValueItems.get(name);
		if (exts != null) {
			for (Map.Entry<String, Integer> e : exts.entrySet()) {
				if (value.indexOf(e.getKey()) >= 0)
					return e.getValue();
			}
		}
		return defaultKey;
	}
	
	public void setItems(Map<String, Map<Integer, String>> items) {
		WxEnum.items = items;
		for (Map.Entry<String, Map<Integer, String>> entry : WxEnum.items.entrySet()) {
			String name = entry.getKey();
			for (Map.Entry<Integer, String> e : entry.getValue().entrySet()) {
				String[] ss = e.getValue().split(",");
				if (ss.length > 1) {
					e.setValue(ss[0]);
					
					for (int i = 1; i < ss.length; i++) {
						Map<String, Integer> exts = extValueItems.get(name);
						if (exts == null) {
							exts = new HashMap<String, Integer>();
							extValueItems.put(name, exts);
						}
						exts.put(ss[i], e.getKey());
					}
				}
			}
		}
	}

}

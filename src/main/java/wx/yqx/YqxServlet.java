package wx.yqx;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import wx.util.JsonBuilder;
import wx.util.JsonWrapper;
import wx.util.Util;

public class YqxServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected final static Logger log = LoggerFactory.getLogger(YqxServlet.class);

	protected WebApplicationContext ctx;
	protected JdbcTemplate jtYqx;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ctx = (WebApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		jtYqx = ctx.getBean("jtYqx", JdbcTemplate.class);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String c = request.getParameter("c");
		String a = request.getParameter("a");
		
		String ret = "{}";
		if ("upfile".equals(c)) {
			if ("systag".equals(a)) {
				ret = upfile_systag(request, response);
			}
		} else if ("scene".equals(c)) {
			if ("design".equals(a)) {
				ret = scene_design(request, response);
			} else if ("pageList".equals(a)) {
				ret = scene_pageList(request, response);
			} else if ("savepage".equals(a)) {
				ret = scene_savepage(request, response);
			}
		}

		response.getWriter().println(ret);
		response.getWriter().flush();
	}
	
	private String getPostStr(HttpServletRequest request) {
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			return sb.toString();
		} catch (Exception e) {
			
		}
		return null;
	}
	
	private String scene_savepage(HttpServletRequest request, HttpServletResponse response) {
		JsonWrapper content = new JsonWrapper(getPostStr(request));
		
		long userId = Util.obj2long(request.getSession().getAttribute(wx.util.Constants.SESSION_ATTR_USERID));
		long pageId = content.getLong("id");
		long sceneId = content.getLong("sceneId");
		
		String sql = "delete from cj_scenedata where pageid_bigint=? and sceneid_bigint=?";
		jtYqx.update(sql, pageId, sceneId);
		
		JsonWrapper elements = content.getNode("elements");
		for (int i = 0; i < elements.getArraySize(); i++) {
			JsonWrapper e = elements.getArrayNode(i);
			int type = e.getInt("type");
			if (type == 5 || type == 501 || type == 502 || type == 503) {
				sql = "insert into cj_scenedata(sceneid_bigint, pageid_bigint, elementid_int, elementtitle_varchar, elementtype_int, userid_int) values(?,?,?,?,?,?)";
				jtYqx.update(sql, sceneId, pageId, e.getLong("id"), e.getString("title"), e.getInt("type"), userId);
				
				String c = e.get("content");
				if (c.indexOf("eqs/link?id=") >= 0) {
					e.put("content", c.replace("eqs/link?id=", "?c=scene&a=link&id="));
				}
			}
		}
		sql = "update cj_scenepage set pagecurrentnum_int=?, properties_text=?, content_text=? where pageid_bigint=? and sceneid_bigint=? and userid_int=?";
		jtYqx.update(sql, content.getInt("num"), content.get("properties_text"), elements.toJson(), pageId, sceneId, userId);
		
		int musicType = 0;
		String musicUrl = content.get("scene.image.bgAudio.url");
		if (musicUrl != null) {
			musicType = Util.str2int(content.get("scene.image.bgAudio.type"));
		} else {
			musicUrl = "";
		}
		sql = "update cj_scene set musicurl_varchar=?,musictype_int=? where pageid_bigint=? and sceneid_bigint=? and userid_int=?";
		jtYqx.update(sql, musicUrl, musicType, pageId, sceneId, userId);
		
		JsonBuilder json = new JsonBuilder();
		json.append("success", true).append("code", 200).append("msg", "success");
		json.flip();
		return json.toString();
	}
	
	private String scene_pageList(HttpServletRequest request, HttpServletResponse response) {
		JsonBuilder json = new JsonBuilder();
		json.append("success", true).append("code", 200).append("msg", "success");

		json.appendNull("obj");
		json.appendNull("map");
		
		long sceneId = Util.str2long(request.getParameter("id"));
		String sql = "select * from scenepage where sceneid_bigint=? and myTypl_id=0 order by pagecurrentnum_int asc";
		List<Map<String, Object>> results = jtYqx.queryForList(sql, Arrays.asList(sceneId));
		
		List<JsonBuilder> list = new ArrayList<JsonBuilder>();
		for (Map<String, Object> m : results) {
			JsonBuilder item = new JsonBuilder();
			item.append("id", m.get("pageid_bigint"));
			item.append("sceneId", m.get("sceneid_bigint"));
			item.append("num", m.get("pagecurrentnum_int"));
			item.append("name", m.get("pagename_varchar"));
			item.appendNull("properties").appendNull("elements").appendNull("scene");
			item.flip();
			list.add(item);
		}
		json.append("list", list);
		
		json.flip();
		return json.toString();
	}
	
	private String scene_design(HttpServletRequest request, HttpServletResponse response) {
		JsonBuilder json = new JsonBuilder();
		json.append("success", true).append("code", 200).append("msg", "success");
		
		long pageid = Util.str2long(request.getParameter("id"));
		String sql = "select * from cj_scenepage where pageid_bigint=?";
		List<Map<String, Object>> scenePages = jtYqx.queryForList(sql, Arrays.asList(pageid));
		
		long sceneId = Util.obj2long(scenePages.get(0).get("sceneid_bigint"));
		sql = "select * from cj_scene where delete_int=0 and sceneid_bigint=?";
		List<Map<String, Object>> scenes = jtYqx.queryForList(sql, Arrays.asList(sceneId));
		
		JsonWrapper content = new JsonWrapper((String)scenePages.get(0).get("content_text"));
		for (int i = 0; i < content.getArraySize(); i++) {
			JsonWrapper n = content.getArrayNode(i);
			n.put("sceneId", sceneId);
			n.put("pageId", pageid);
		}
		
		JsonBuilder obj = new JsonBuilder();
		obj.append("id", pageid);
		obj.append("sceneId", sceneId);
		obj.append("num", scenes.get(0).get("pagecurrentnum_int"));
		obj.appendNull("name");
		obj.append("properties", scenes.get(0).get("properties_text"));
		obj.appendJsonValue("elements", content.toJson());
		
		JsonBuilder scene = new JsonBuilder();
		scene.append("id", sceneId);
		scene.append("name", scenes.get(0).get("scenename_varchar"));
		scene.append("createUser", scenes.get(0).get("userid_int"));
		scene.append("createTime", 1425998747000L);
		scene.append("type", scenes.get(0).get("scenetype_int"));
		scene.append("pageMode", scenes.get(0).get("movietype_int"));
		scene.append("image", scenes.get(0).get("movietype_int")); //??
		scene.append("isAdvancedUser", false);
		if (scenes.get(0).get("musicurl_varchar") != null && String.valueOf(scenes.get(0).get("musicurl_varchar")).length() > 0) {
			JsonBuilder bgAudio = new JsonBuilder();
			bgAudio.append("url", scenes.get(0).get("musicurl_varchar"));
			bgAudio.append("type", scenes.get(0).get("musictype_int"));
			bgAudio.flip();
			obj.append("bgAudio", bgAudio);
		}
		scene.flip();
		obj.append("scene", scene);
		
		obj.append("isTpl", 0).append("isPromotion", 0).append("status", 1).append("openLimit", 0).append("submitLimit", 0);
		obj.appendNull("startDate").appendNull("endDate").appendNull("accessCode").appendNull("thirdCode");
		obj.append("updateTime", 1426038857000L).append("publishTime", 1426038857000L).append("applyTemplate", 0).append("applyPromotion", 0);
		obj.appendNull("sourceId").append("code", scenes.get(0).get("scenecode_varchar")).append("description", scenes.get(0).get("desc_varchar"));
		obj.append("sort", 0).append("pageCount", 0).append("dataCount", 1).append("showCount", scenes.get(0).get("hitcount_int"));
		obj.appendNull("userLoginName").appendNull("userName");
		obj.flip();
		
		json.append("obj", obj);
		
		json.appendNull("map").appendNull("list");
		
		json.flip();
		return json.toString();
	}
	
	private String upfile_systag(HttpServletRequest request, HttpServletResponse response) {
		JsonBuilder json = new JsonBuilder();
		json.append("success", true).append("code", 200).append("msg", "success");

		json.appendNull("obj");
		json.appendNull("map");
		
		List<Object> args = new ArrayList<Object>();
		String sql = "select * from cj_tag where userid_int=0 ";
		int type = Util.str2int(request.getParameter("type"));
		if (type == 1 || type == 2 || type == 88) {
			sql += " and type=?";
			args.add(type);
		}
		int bizType = Util.str2int(request.getParameter("bizType"));
		if (bizType != 0) {
			sql += " and biztype_int=?";
			args.add(bizType);
		}
		sql += " order by tagid_int asc";
		List<Map<String, Object>> results = jtYqx.queryForList(sql, args);

		List<JsonBuilder> list = new ArrayList<JsonBuilder>();
		for (Map<String, Object> m : results) {
			JsonBuilder item = new JsonBuilder();
			item.append("id", m.get("tagid_int"));
			item.append("name", m.get("name_varchar"));
			item.append("createUser", 0);
			item.append("createTime", 1423122412000L);
			item.append("bizType", m.get("biztype_int"));
			item.flip();
			list.add(item);
		}
		json.append("list", list);
		json.flip();
		
		return json.toString();
	}
}

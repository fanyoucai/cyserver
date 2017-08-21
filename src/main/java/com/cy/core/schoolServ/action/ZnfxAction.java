package com.cy.core.schoolServ.action;

import com.cy.core.chatGroup.entity.ChatGroup;
import com.cy.core.chatGroup.service.ChatGroupService;
import com.cy.core.news.service.NewsService;
import com.cy.core.schoolServ.dao.SchoolServMapper;
import com.cy.core.schoolServ.entity.Fxjh;
import com.cy.core.schoolServ.service.SchoolServService;
import com.cy.base.action.AdminBaseAction;
import com.cy.base.entity.DataGrid;
import com.cy.base.entity.Message;
import com.cy.core.alumni.entity.Alumni;
import com.cy.core.alumni.service.AlumniService;
import com.cy.core.channel.entity.NewsChannel;
import com.cy.core.channel.service.NewsChannelService;
import com.cy.core.dict.entity.Dict;
import com.cy.core.news.entity.News;
import com.cy.core.news.entity.NewsType;
import com.cy.core.serviceNews.entity.ServiceNews;
import com.cy.core.serviceNews.service.ServiceNewsService;
import com.cy.core.serviceNewsType.entity.ServiceNewsType;
import com.cy.core.serviceNewsType.service.ServiceNewsTypeService;
import com.cy.core.userProfile.entity.UserProfile;
import com.cy.core.userProfile.service.UserProfileService;
import com.cy.mobileInterface.baseinfo.service.BaseInfoService;
import com.cy.system.Global;
import com.cy.util.Base64Utils;
import com.cy.util.RoleUtil;
import com.cy.util.WebUtil;
import com.cy.util.jms.JsonDateValueProcessor;
import com.cy.util.jms.MsgPushServer;
import com.cy.util.jms.PushedMessage;
import com.cy.util.jms.SingleNewsMessage;
import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Namespace("/mobile/serv")
@Action(value = "znfxAction", results = { @Result(name = "viewZnfx", location = "/page/admin/news/viewNews.jsp")})
public class ZnfxAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ZnfxAction.class);

	@Autowired
	private NewsService newsService;

	@Autowired
	private SchoolServService schoolServService;

	@Autowired
	private AlumniService alumniService;

	@Autowired
	private NewsChannelService newsChannelService;
	
	@Autowired
	private UserProfileService userProfileService;

	private News news;

	@Autowired
	private BaseInfoService baseInfoService;

	@Autowired
	private ServiceNewsService serviceNewsService ;

	@Autowired
	private ServiceNewsTypeService serviceNewsTypeService ;

	@Autowired
	private ChatGroupService chatGroupService;

	// @Autowired
	// private MicroService microService;

	private String isRmv;
	private int pageType;
	private String aultId;
	private String category;

	/** --新闻所在地-- **/
	private String cityName;
	
	
	
//	private String country;
	private String province;
	private String city;
	private String area;

	//------------------
	private String topic;
	private String status;
	private String number;
	private String time;
	private String classinfo;
	private String other;
	private Fxjh fxjh;

	private String serviceId ;
	//------------------



	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setClassinfo(String classinfo) {
		this.classinfo = classinfo;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getTopic() {
		return topic;
	}

	public String getNumber() {
		return number;
	}

	public String getTime() {
		return time;
	}

	public String getClassinfo() {
		return classinfo;
	}

	public String getOther() {
		return other;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIsRmv() {
		return isRmv;
	}

	public void setIsRmv(String isRmv) {
		this.isRmv = isRmv;
	}

	public void setPageType(int pageType) {
		this.pageType = pageType;
	}

	public int getPageType() {
		return pageType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void dataGrid() {
		dataGridNews();
	}

	public void dataGridx() {
		dataGridNews();
	}

	private void dataGridNews() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		if (news != null) {
			map.put("title", news.getTitle());
		}

		if (getUser().getFlag() == 1) {
			map.put("cityName", getUser().getAlumni().getRegion());
		}
		
//		if (getUser().getRole().getSystemAdmin() != 1) {
		if (RoleUtil.isNotSystemAdmin(getUser())) {
        	map.put("deptList", getUser().getDepts());
        }
		DataGrid<News> data = newsService.dataGrid(map);
//		List<News> newsList = data.getRows();
//		if(!WebUtil.isEmpty(newsList)){
//			for(News news:newsList){
//				if(news.getPCategory()==0){
//					news.setPCategory(news.getCategory());
//					news.setPCategoryName(news.getCategoryName());
//					news.setCategory(0L);
//					news.setCategoryName("");
//				}
//				if(news.getPCategoryWeb()==0){
//					news.setPCategoryWeb(news.getCategoryWeb());
//					news.setPCategoryWebName(news.getCategoryWebName());
//					news.setCategoryWeb(0L);
//					news.setCategoryWebName("");
//				}
//			}	
//		}
		super.writeJson(data);
	}

	public void savex() {
		saveNews();
	}

	public void save() {
		saveNews();
	}

	private void saveNews() {
		//手机新闻1级栏目类型
		long type1 = WebUtil.toLong(this.getRequest().getParameter("type1")) ;
		//手机新闻2级栏目类型
		long type2 = WebUtil.toLong(this.getRequest().getParameter("type2")) ;
		//网页新闻1级栏目类型
		long webType1 = WebUtil.toLong(this.getRequest().getParameter("webType1")) ;
		//网页新闻2级栏目类型
		long webType2 = WebUtil.toLong(this.getRequest().getParameter("webType2")) ;
		
		if(type1>0 && type2 ==0){
			news.setCategory(type1);
		}else if(type1>0 && type2 >0){
			news.setCategory(type2);
		}
		
		if(webType1>0 && webType2 ==0){
			news.setCategoryWeb(webType1);
		}else if(webType1>0 && webType2 >0){
			news.setCategoryWeb(webType2);
		}
		
		
		String region = "";
//		if (!WebUtil.isNull(country)) {
//			region += country;
//		}
		if (!WebUtil.isNull(province)) {
			region += province;
		}
		if (!WebUtil.isNull(city)) {
			region += " " + city;
		}
		if (!WebUtil.isNull(area)) {
			region += " " + area;
		}
		if (!WebUtil.isNull(region)) {
			news.setCityName(region);
		}
		
		
		Message message = new Message();
		try {
			if (getUser().getFlag() == 1) {
				news.setCityName(getUser().getAlumni().getRegion());
			}
			//news.setCreateTime(new Date());
			String newsUrl = news.getNewsUrl();
			List<String> ls = null;
			if (WebUtil.isEmpty(newsUrl)) {
				// 自动生成URL
				HttpServletRequest request = this.getRequest();
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
				String url = basePath + "mobile/news/newsAction!doNotNeedSessionAndSecurity_getMobNew.action?id=";
				newsService.insertNewsAndsetUrl(news, url,ls);
			} else {
				newsService.save(news,ls);
			}
			message.setMsg("新增成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("新增失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update() {
		updateNews();
	}

	public void updatex() {
		updateNews();
	}

	private void updateNews() {
	
		//手机新闻1级栏目类型
		long type1 = WebUtil.toLong(this.getRequest().getParameter("type1")) ;
		//手机新闻2级栏目类型
		long type2 = WebUtil.toLong(this.getRequest().getParameter("type2")) ;
		//网页新闻1级栏目类型
		long webType1 = WebUtil.toLong(this.getRequest().getParameter("webType1")) ;
		//网页新闻2级栏目类型
		long webType2 = WebUtil.toLong(this.getRequest().getParameter("webType2")) ;
		
		if(type1>0 && type2 ==0){
			news.setCategory(type1);
		}else if(type1>0 && type2 >0){
			news.setCategory(type2);
		}
		
		if(webType1>0 && webType2 ==0){
			news.setCategoryWeb(webType1);
		}else if(webType1>0 && webType2 >0){
			news.setCategoryWeb(webType2);
		}
		
		Message message = new Message();
		try {
			List<String> lst = null;
			newsService.update(news, lst);
			message.setMsg("修改成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("修改失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void deletex() {
		deleteNews();
	}

	public void delete() {
		deleteNews();
	}

	private void deleteNews() {
		Message message = new Message();
		try {
			newsService.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	/*
	 * 批量发送
	 */
	public void sendList() {
		sendNews();
	}

	public void sendListx() {
		sendNews();
	}

	private void sendNews() {
		Message message = new Message();
		try {
			String accessToken = "";
			List<Long> list = new ArrayList<Long>();
			if (ids != null && !"".equals(ids)) {
				String[] array = ids.split(",");
				for (String id : array) {
					list.add(Long.parseLong(id));
				}
			}
			int result = 0;
			result = sendList(list, accessToken);
			if (result > 0) {
				message.setMsg("批量发送成功");
				message.setSuccess(true);
			} else if (result == -1) {
				message.setMsg("批量发送失败,批量发送超过每月限制，每月只能群发4批，每批不超过10条消息!");
				message.setSuccess(false);
			} else {
				message.setMsg("批量发送失败");
				message.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("批量发送失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	/** --新闻推送-- **/
	public int sendList(List<Long> list, String accessToken) {
		Map<String, PushedMessage> map = new HashMap<String, PushedMessage>();
		Iterator<Long> iterator = list.iterator();
		while (iterator.hasNext()) {
			Long id = iterator.next();
			News news = newsService.selectById(id);
			// 新闻推送这里需要大图
			// 图片URL
			String pic = news.getPic();
			if (!WebUtil.isEmpty(pic)) {
				// 图片后缀
				String fileExt = pic.substring(pic.lastIndexOf("."));
				String temp = pic.substring(0, pic.lastIndexOf("."));
				if ("100".equals(news.getTopnews())) {
					// 该新闻为滚动新闻，需要调用大图
					news.setPic(temp + "_MAX" + fileExt);
				} else if (temp.indexOf("_MIN") == -1) {
					news.setPic(temp + "_MIN" + fileExt);
				}
			}
			addToPushedMessageMap(map, news);
		}

		MsgPushServer server = MsgPushServer.getInstance();
		for (Map.Entry<String, PushedMessage> entry : map.entrySet()) {
			server.sendPushedMessage(entry.getValue());
		}

		return 1;
	}

	public void addToPushedMessageMap(Map<String, PushedMessage> map, News news) {
		SingleNewsMessage smsg = new SingleNewsMessage();
		String channelName = news.getChannelName();

		smsg.setChannelName(channelName);
		smsg.setChannelId(channelName);
		// 设置图片url
		String URL3 = news.getPic() == null ? "" : news.getPic();
		String strURL3 = "";
		try {
			strURL3 = URLEncoder.encode(URL3, "utf-8").replace("*", "*").replace("~", "~").replace("+", " ");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		smsg.setIcon(strURL3);

		// 设置newsUrl
		String URL4 = news.getNewsUrl();
		String strURL4 = "";
		try {
			strURL4 = URLEncoder.encode(URL4, "utf-8").replace("*", "*").replace("~", "~").replace("+", " ");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		smsg.setNewsUrl(strURL4);
		smsg.setNid((int) news.getNewsId());
		smsg.setPMId(channelName);
		smsg.setTime(news.getCreateTime());
		smsg.setTitle(news.getTitle());
		smsg.setSummary(news.getIntroduction());
		// 复用，新闻类型
		smsg.setContent(news.getType());

		if (map.containsKey(channelName)) {
			map.get(channelName).getNewsList().add(smsg);
		} else {
			PushedMessage pushedMessage = new PushedMessage();
			pushedMessage.setChannelName(channelName);
			pushedMessage.setChannelId(channelName);

			Map<String, Object> channelMap = new HashMap<String, Object>();
			channelMap.put("channelName", channelName);
			List<NewsChannel> newsChannelList = newsChannelService.selectNewsChannelList(channelMap);
			String iconURL4 = null;
			if (newsChannelList != null && newsChannelList.size() != 0) {
				iconURL4 = newsChannelList.get(0).getChannelIcon();
			} else {
				logger.error("invalid channnel name");
				return;
			}
			String strIconURL4 = "";
			try {
				strIconURL4 = URLEncoder.encode(iconURL4, "utf-8").replace("*", "*").replace("~", "~").replace("+", " ");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			pushedMessage.setIcon(strIconURL4);

			pushedMessage.setNewsSummary(smsg.getSummary());
			pushedMessage.setPMId(channelName);
			pushedMessage.setTime(new Date());
			List<SingleNewsMessage> listSingleNews = new ArrayList<SingleNewsMessage>();
			listSingleNews.add(smsg);
			pushedMessage.setNewsList(listSingleNews);
			map.put(channelName, pushedMessage);
		}
	}

	/**
	 * 访问地址http://127.0.0.1/cy_v1/mobile/news/newsAction!doNotNeedSessionAndSecurity_getRegularNews.action
	 * 
	 * demo通过固定通道获取示例新闻
	 */
	public void doNotNeedSessionAndSecurity_getRegularNews() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("start", 0);
			map.put("rows", 5);
			List<String> channelNameList = new ArrayList<String>();
			if (Global.REGULAR_CHANNEL_1 != null && !Global.REGULAR_CHANNEL_1.equals("")) {
				channelNameList.add(Global.REGULAR_CHANNEL_1);
			}
			if (Global.REGULAR_CHANNEL_2 != null && !Global.REGULAR_CHANNEL_2.equals("")) {
				channelNameList.add(Global.REGULAR_CHANNEL_2);
			}
			if (Global.REGULAR_CHANNEL_3 != null && !Global.REGULAR_CHANNEL_3.equals("")) {
				channelNameList.add(Global.REGULAR_CHANNEL_3);
			}

			if (channelNameList.size() != 0) {
				map.put("channelNameList", channelNameList);
				List<News> list = newsService.selectNews(map);

				if (list.size() != 0) {
					Map<String, PushedMessage> pushedMeaageMap = new HashMap<String, PushedMessage>();
					Iterator<News> iterator = list.iterator();
					while (iterator.hasNext()) {
						News news = iterator.next();
						addToPushedMessageMap(pushedMeaageMap, news);
					}

					List<PushedMessage> pushedMessageList = new ArrayList<PushedMessage>();
					for (PushedMessage v : pushedMeaageMap.values()) {
//						List<SingleNewsMessage> newsList = v.getNewsList();
						// this.setNewsPic(newsList);
						pushedMessageList.add(v);
					}
					// 设置javabean中日期转换时的格式
					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));

					JSONArray jsonobj = JSONArray.fromObject(pushedMessageList, jsonConfig);
					super.writeJson(jsonobj);
				} else {
					Message message = new Message();
					message.setMsg("频道不存在或无新闻纪录!");
					message.setSuccess(false);
					super.writeJson(message);
				}
			}

		} catch (Exception e) {
			Message message = new Message();
			logger.error(e, e);
			message.setMsg("查询出错!");
			message.setSuccess(false);
			super.writeJson(message);
		}
	}

	/** --设置新闻的封面图是大图还是小图-- **/
	private void setNewsPic(List<SingleNewsMessage> newsList) {
		boolean isBreaking = false;
		for (int i = 0; i < newsList.size(); i++) {
			SingleNewsMessage news = newsList.get(i);
			// 如果isBreaking为true则调用大图
			isBreaking = news.isBreaking();
			if (isBreaking) {
				// 调用大图
				String pic = news.getIcon();
				if (!WebUtil.isEmpty(pic)) {
					// 图片后缀
					String fileExt = pic.substring(pic.lastIndexOf("."));
					String temp = pic.substring(0, pic.lastIndexOf("."));
					if (temp.indexOf("_MAX") != -1 || temp.indexOf("_MIN") != -1) {
						temp = pic.substring(0, pic.lastIndexOf(".") - 4);
					}
					news.setIcon(temp + "_MAX" + fileExt);
					break;
				}
			}
		}

		// 如果isBreaking仍然为false;则将第一个图设置为大图
		if (isBreaking == false) {
			SingleNewsMessage news = newsList.get(0);
			String pic = news.getIcon();
			if (!WebUtil.isEmpty(pic)) {
				// 图片后缀
				String fileExt = pic.substring(pic.lastIndexOf("."));
				String temp = pic.substring(0, pic.lastIndexOf("."));
				if (temp.indexOf("_MAX") != -1 || temp.indexOf("_MIN") != -1) {
					temp = pic.substring(0, pic.lastIndexOf(".") - 4);
				}
				news.setIcon(temp + "_MAX" + fileExt);
			}
		}
	}

	public String doNotNeedSessionAndSecurity_showContent() {
		try {
			news = newsService.selectById(id);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return "showContent";
	}

	// public void doNotNeedSecurity_initType()
	// {
	// @SuppressWarnings("unchecked")
	// Map<String, Object> map = (Map<String, Object>)
	// ServletActionContext.getServletContext().getAttribute("dictionaryInfoMap");
	// @SuppressWarnings("unchecked")
	// List<Dict> allList = (List<Dict>) map.get("dicts");
	// List<DictValue> list = new ArrayList<DictValue>();
	// for (Dict dict : allList)
	// {
	// if (dict.getDictKey().equals("信息类别") && dict.getDictValue() != null &&
	// !"".equals(dict.getDictValue()))
	// {
	// list = JSON.parseArray(dict.getDictValue(), DictValue.class);
	// break;
	// }
	// }
	// super.writeJson(list);
	// }

	public void doNotNeedSecurity_getAllCategorys() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> cateNameList = new ArrayList<String>();
		cateNameList.add("手机新闻类别");
		map.put("cateNameList", cateNameList);
		List<Dict> list = newsService.getAllCategorys(map);
		super.writeJson(list);

	}

	public void setMobTypeList() {
		setMobTypeListNews();
	}

	public void setMobTypeListx() {
		setMobTypeListNews();
	}

	private void setMobTypeListNews() {
		Message message = new Message();
		String controlStr = "";
		if (isRmv.equals("true")) {
			// 100 is for topnews of mobile
			controlStr = "100";
		} else {
			controlStr = null;
		}
		try {
			if(pageType==1) {
				newsService.setMobTypeList(ids, controlStr);
			} else if(pageType==2) {
				newsService.setWebTypeList(ids, controlStr);
			}
			
			message.setMsg("设置成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("设置失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}
	
//	public void setWebTypeList() {
//		setWebTypeListNews();
//	}
//
//	public void setWebTypeListx() {
//		setWebTypeListNews();
//	}
//	
//	private void setWebTypeListNews() {
//		Message message = new Message();
//		String controlStr = "";
//		if (isRmv.equals("true")) {
//			// 100 is for topnews of mobile
//			controlStr = "100";
//		} else {
//			controlStr = null;
//		}
//		try {
//			newsService.setWebTypeList(ids, controlStr);
//			message.setMsg("设置成功");
//			message.setSuccess(true);
//
//		} catch (Exception e) {
//			logger.error(e, e);
//			message.setMsg("设置失败");
//			message.setSuccess(false);
//		}
//		super.writeJson(message);
//	}

	public void doNotNeedSessionAndSecurity_listMobNews() {
		// News news = new News();
		JSONObject json = new JSONObject();
		if (news == null) {
			news = new News();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", news.getCategory());

		// 100 is for topnews of mobile
		map.put("topnews", "100");
		map.put("start", news.getCurrentRow());
		map.put("rows", news.getIncremental());
		map.put("cityName", cityName);
		json.put("list", newsService.listMobNews(map));

		super.writeJson(json);
	}

	public String doNotNeedSessionAndSecurity_initMobNews() {
		News news = new News();
		long locate = 0;
		try {
			locate = Long.parseLong(category);
		} catch (Exception e) {
			locate = 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", locate);
		news.setCategory(locate);
		// 100 is for topnews of mobile
		map.put("topnews", "100");
		map.put("start", news.getCurrentRow());
		map.put("rows", news.getIncremental());
		map.put("cityName", cityName);
		List<News> topnewslist = newsService.listMobTopNews(map);
		super.getRequest().setAttribute("news", news);
		super.getRequest().setAttribute("topnewslist", topnewslist);
		return "listMobNews";
	}

	
	

	public String doNotNeedSessionAndSecurity_getMobNew() {

		news = newsService.selectById(id);

		if (news.getContent() != null) {

			String tmpOneStr = "";

			StringBuffer bufConStr = new StringBuffer(news.getContent());

			int findImg = bufConStr.indexOf("<img");

			while (findImg != -1) {

				tmpOneStr = bufConStr.substring(findImg, bufConStr.indexOf(">", findImg) + 1);

				bufConStr.delete(findImg, bufConStr.indexOf(">", findImg) + 1);

				tmpOneStr = tmpOneStr.replaceAll("(?i)style[\\s]*=[\\s]*[\'\"][\\S]*[\\s]*[\'\"]", "");

				tmpOneStr = tmpOneStr.replaceAll("(?i)width[\\s]*=[\\s]*[\'\"][\\S]*[\\s]*[\'\"]", "");

				tmpOneStr = tmpOneStr.replaceAll("(?i)height[\\s]*=[\\s]*[\'\"][\\S]*[\\s]*[\'\"]", "");

				bufConStr.insert(findImg, tmpOneStr);

				findImg = bufConStr.indexOf("<img", findImg + 1);

			}

			news.setContent(bufConStr.toString());
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", news.getCategory());
		map.put("start", 0);
		map.put("rows", 3);
		map.put("butNew", news.getNewsId());
		List<News> newslist = newsService.listMobNews(map);
		super.getRequest().setAttribute("newslist", newslist);
		return "getMobNew";
	}

	public void doNotNeedSessionAndSecurity_getMobNewJson() {

		news = newsService.selectById(id);
		super.writeJson(news);
	}

	public void doNotNeedSessionAndSecurity_getMobNewRelatedJson() {

		news = newsService.selectById(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", news.getCategory());
		map.put("start", 0);
		map.put("rows", 3);
		map.put("butNew", news.getNewsId());
		List<News> newslist = newsService.listMobNews(map);
		super.writeJson(newslist);
	}

	public void doNotNeedSessionAndSecurity_initMobNewsJson() {
		News news = new News();
		long locate = 0;
		try {
			locate = Long.parseLong(category);
		} catch (Exception e) {
			locate = 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", locate);
		news.setCategory(locate);
		// 100 is for topnews of mobile
		map.put("topnews", "100");
		map.put("start", news.getCurrentRow());
		map.put("rows", news.getIncremental());
		List<News> topnewslist = newsService.listMobTopNews(map);
		super.writeJson(topnewslist);
	}
	
	
	public void doNotNeedSessionAndSecurity_getNewsTypeByParent(){		
		long parent_id = WebUtil.toLong(this.getRequest().getParameter("parent_id"));
		int origin = WebUtil.toInt(this.getRequest().getParameter("origin"));
		int type = WebUtil.toInt(this.getRequest().getParameter("type"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent_id",parent_id);
		map.put("origin", origin);
		map.put("type", type);
		if(origin == 2) { //地方校友会只取本地的栏目
			com.cy.core.user.entity.User user = (com.cy.core.user.entity.User)getSession().get("user");
			map.put("alumniId", user.getDeptId());
		}
		List<NewsType> list = this.newsService.selectTypeList(map);
		super.writeJson(list);
	}
	
	public void doNotNeedSessionAndSecurity_getWebNewsTypeByParent(){
		long parent_id = WebUtil.toLong(this.getRequest().getParameter("parent_id"));
		int origin = WebUtil.toInt(this.getRequest().getParameter("origin"));
		int type = WebUtil.toInt(this.getRequest().getParameter("type"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent_id",parent_id);
		map.put("origin", origin);
		map.put("type", type);
		if(origin == 2) { //地方校友会只取本地的栏目
			com.cy.core.user.entity.User user = (com.cy.core.user.entity.User)getSession().get("user");
			map.put("alumniId", user.getDeptId());
		}
		List<NewsType> list = this.newsService.selectWebTypeList(map);
		super.writeJson(list);
	}
	

	public String doNotNeedSecurity_initNewsUpdate() {
		String convert = this.getRequest().getParameter("convert");
		news = newsService.selectById(id);
		//查询手机新闻类型的1级栏目
		Map<String, Object> map = new HashMap<String, Object>();
		//这里只允许查询总会新闻类别
		map.put("origin", 1);
		map.put("parent_id", 0);
		map.put("type", 1);
		List<NewsType> list1 = this.newsService.selectTypeList(map);
		List<NewsType> listweb1 = this.newsService.selectWebTypeList(map);
		//子栏目集合
		map = new HashMap<String, Object>();
		long parent_id = -1;
		if(news.getPCategory()==0) {
			if(news.getCategory()!=0) {
				parent_id = news.getCategory();
			}
		} else {
			parent_id = news.getPCategory();
		}
		map.put("parent_id", parent_id);
		List<NewsType> list2 = new ArrayList<NewsType>();
		if(WebUtil.isNull(convert)) {
			list2 = this.newsService.selectTypeList(map);
		}		
		
		parent_id = -1;
		if(news.getPCategoryWeb()==0) {
			if(news.getCategoryWeb()!=0) {
				parent_id = news.getCategoryWeb();
			}
		} else {
			parent_id = news.getPCategoryWeb();
		}
		map.put("parent_id", parent_id);
		List<NewsType> listweb2 = new ArrayList<NewsType>();
		if(WebUtil.isNull(convert)) {
			listweb2 = this.newsService.selectWebTypeList(map);
		}		

		String createTime = WebUtil.formatDateByPattern(news.getCreateTime(), WebUtil.YMDHMS);
		
		this.getRequest().setAttribute("news", news);
		this.getRequest().setAttribute("pCategory", news.getPCategory()==0?news.getCategory():news.getPCategory());
		this.getRequest().setAttribute("list1", list1);
		this.getRequest().setAttribute("list2", list2);
		this.getRequest().setAttribute("pCategoryWeb", news.getPCategoryWeb()==0?news.getCategoryWeb():news.getPCategoryWeb());
		this.getRequest().setAttribute("listweb1", listweb1);
		this.getRequest().setAttribute("listweb2", listweb2);
		this.getRequest().setAttribute("convert", WebUtil.isNull(convert)?"":convert);
		this.getRequest().setAttribute("createTime", createTime);
		return "initNewsUpdate";
	}
	
	/**--查询所有一级栏目返还给手机端--**/
	public void doNotNeedSessionAndSecurity_getAllLinkOfCategorys() {
		String basePath = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath();
		String URL = basePath + "/mobile/news/newsList.jsp?category=";
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("parent_id", 0);
		//是否上导航（0：不上导航， 1：上导航）
		queryMap.put("isNavigation", 1);
		List<NewsType> list = this.newsService.selectTypeList(queryMap);
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		//组织数据
		if(!WebUtil.isEmpty(list)){
			for(NewsType type:list){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("dictId", type.getId());
				map.put("dictTypeId", "0");
				map.put("dictName", type.getName());
				if(type.getType()==1){
					map.put("dictUrl", URL+type.getId());
				}else if(type.getType()==2){
					map.put("dictUrl", type.getUrl());
				}else if(type.getType()==3){
					
					map.put("dictUrl", basePath + "/mobile/news/newsShow.jsp?category="+type.getId());
				}else{
					map.put("dictUrl", "#");
				}
				
				map.put("dictValue", "0");		
				listMap.add(map);
			}
		}
		super.writeJson(listMap);
	}
	
	
	/**---根据mobile类别查询新闻列表--**/
	public void doNotNeedSessionAndSecurity_getNewsListByMobileType_new(){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = this.getRequest();
		String category = (String)request.getParameter("category");
		//判断栏目
		String topnews = request.getParameter("topnews");

		map.put("serviceId", serviceId);
		//判断是否查询幻灯片新闻
		if (WebUtil.isNull(topnews) && !"100".equals(topnews)) {
			map.put("channel", category);
		} else if ("100".equals(topnews)) {
			//仅仅查询topnews
			map.put("topnews", topnews);
			map.put("channel", category);
		}

		int start = WebUtil.toInt(request.getParameter("start"));
		int rows = WebUtil.toInt(request.getParameter("rows"));
//			JSONObject json = new JSONObject();
		Map<String,Object> resultMap = Maps.newHashMap() ;
		if (start >= 0 && rows > 0) {
			map.put("start", start);
			map.put("rows", rows);
			resultMap.put("start", start);
			resultMap.put("rows", rows);
		} else {
			map.put("isNotLimit","1") ;
		}

		List<ServiceNews> list = serviceNewsService.findList(map);
		String accountNum = request.getParameter("u");
		Long count = serviceNewsService.findCount(map);

		resultMap.put("newsList", list);
		resultMap.put("countNews", count);
		super.writeJson(resultMap);
	}
	/**---根据mobile类别查询新闻列表--**/
	public void doNotNeedSessionAndSecurity_getNewsListByMobileType(){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = this.getRequest();
		long category = WebUtil.toLong(request.getParameter("category"));
		//判断栏目
		//NewsType type = this.newsService.selectTypeById(category+"");
		List<NewsType> leveList = this.newsService.selectLeveList(category+"");
		String topnews = request.getParameter("topnews");

		//判断是否查询幻灯片新闻
		if(WebUtil.isNull(topnews)&&!"100".equals(topnews)){
			map.put("category", category);
		}else if("100".equals(topnews)){
			//仅仅查询topnews
			map.put("topnews", topnews);
			map.put("category", category);
			/*if(WebUtil.isEmpty(leveList)){
				map.put("topnews", topnews);
				map.put("category", category);
			}else{
				map.put("topnews", topnews);
				map.put("parent_id", category);
			}*/
		}

		int start =  WebUtil.toInt(request.getParameter("start"));
		int rows = WebUtil.toInt(request.getParameter("rows"));
		JSONObject json = new JSONObject();
		if(start>=0 && rows>0){
			map.put("start", start);
			map.put("rows", rows);
			json.put("start", start);
			json.put("rows", rows);
		}
		List<News> list = newsService.listMobNews(map);
		String accountNum = request.getParameter("u");
		int count = newsService.listMobNewsCount(map);

		doNewsVisibility(accountNum, list);
		json.put("newsList", list);
		json.put("countNews", count);
		super.writeJson(json);
	}
	
	private void doNewsVisibility(String accountNum, List<News> list) {
		if (list == null || list.size() <= 0) {
			return;
		}

		String uAlumni = "";
		String uProfession = "";
		if (accountNum != null && accountNum.trim().length() > 0) {
			UserProfile u = userProfileService.selectById(accountNum);
			uAlumni = String.valueOf(u.getAlumni_id());
			uProfession = u.getProfession();
		}

		for (int i = 0; i < list.size(); i++) {
			News news = list.get(i);
			String alumniid = news.getAlumniid();
			String profession = news.getProfession();

			if (StringUtils.isBlank(alumniid)
					&& StringUtils.isBlank(profession)) {
				continue;
			}

			// 无权限
			if (StringUtils.isNotBlank(alumniid)
					&& StringUtils.isBlank(uAlumni)) {
				list.remove(i);
			}

			if (StringUtils.isNotBlank(profession)
					&& StringUtils.isBlank(uProfession)) {
				list.remove(i);
			}

			boolean check1 = false;
			if (StringUtils.isNotBlank(alumniid)
					&& StringUtils.isNotBlank(uAlumni)) {

				String[] alumniidArray = alumniid.split(",");
				if (alumniidArray != null && alumniidArray.length > 0) {
					for (int k = 0; k < alumniidArray.length; k++) {
						String alumniid_K = alumniidArray[k];
						if (uAlumni.trim().equalsIgnoreCase(alumniid_K.trim())) {
							check1 = true;
							break;
						}
					}
				}

			}

			if (StringUtils.isNotBlank(profession)
					&& StringUtils.isNotBlank(uProfession)) {

				String[] professionArray = profession.split(",");
				if (professionArray != null && professionArray.length > 0) {
					for (int k = 0; k < professionArray.length; k++) {
						String profession_K = professionArray[k];
						if (uProfession.trim().equalsIgnoreCase(
								profession_K.trim())) {
							check1 = true;
							break;
						}
					}
				}
			}

			if (!check1) {
				list.remove(i);
			}
		}

	}
	
	/**--查询手机新闻类别,返回json数据，提供给前端页面--**/
	public void doNotNeedSessionAndSecurity_getMobileNewsType_new(){

		HttpServletRequest request = this.getRequest();
		String accountNum = request.getParameter("u");

//		List<NewsType> list = this.newsService.selectMobileNewsType(category);
		Map<String,Object> map = Maps.newHashMap() ;
		map.put("serviceId",serviceId) ;
		List<ServiceNewsType> list = this.serviceNewsTypeService.query(map) ;
		if(list != null && !list.isEmpty()) {
			ServiceNewsType type = list.get(0);
		}
		// 自动生成URL
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		for(ServiceNewsType t:list){
			String json_news_url = basePath + "mobile/serv/znfxAction!doNotNeedSessionAndSecurity_getNewsListByMobileType_new.action?category="+t.getId()+"&serviceId="+serviceId;
			t.setJson_news_url(json_news_url);
		}
		ServiceNewsType newsType = new ServiceNewsType();
		newsType.setJson_news_url(basePath + "mobile/serv/znfxAction!doNotNeedSessionAndSecurity_getFxjhPageData.action?category=0&u="+accountNum);
		newsType.setName("返校计划");
		list.add(newsType);
		map.clear();
		map.put("leveList",list) ;
		super.writeJson(map);

	}
	/**--查询手机新闻类别,返回json数据，提供给前端页面--**/
	public void doNotNeedSessionAndSecurity_getMobileNewsType(){

		HttpServletRequest request = this.getRequest();
		String accountNum = request.getParameter("u");
		long category = WebUtil.toLong(request.getParameter("category"));
		List<NewsType> list = this.newsService.selectMobileNewsType(category);
		if(category==0){
			super.writeJson(list);
		}else{
			NewsType type = list.get(0);
			// 自动生成URL
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
			for(NewsType t:type.getLeveList()){
				String json_news_url = basePath + "mobile/serv/znfxAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action?category="+t.getId();
				t.setJson_news_url(json_news_url);
			}
			NewsType newsType = new NewsType();
			newsType.setJson_news_url(basePath + "mobile/serv/znfxAction!doNotNeedSessionAndSecurity_getFxjhPageData.action?category=0&u="+accountNum);
			newsType.setName("返校计划");
			type.getLeveList().add(newsType);
			super.writeJson(type);
		}
	}

	public void doNotNeedSessionAndSecurity_getFxjhPageData(){
		HttpServletRequest request = this.getRequest();
		String accountNum = request.getParameter("u");
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

		Message message = new Message();
		String content = "{\"accountNum\":\""+accountNum+"\"}";
		baseInfoService.getClasses(message, content);
		logger.error("##############doNotNeedSessionAndSecurity_getFxjhPageData##########################>"+message);
		boolean msgRs = message.isSuccess();
		String classStr ="";
		if(msgRs) {
			List<Map<String, Object>> pathList = (List<Map<String, Object>>) message.getObj();
			if(pathList!=null && pathList.size()==1){
				classStr = "<input type=\"text\" id=\"classinfo\" name=\"classinfo\" maxlength=\"60\" value=\""+(String)pathList.get(0).get("strClass")+"\">";
			}
			else if(pathList!=null && pathList.size()>1){
				classStr = "<select id=\"classinfo\" name=\"classinfo\" style=\"margin-left: 95px;\">";

				for(int i=0;i<pathList.size();i++){
					Map<String, Object> mi = pathList.get(i);
					String classnamei = (String)mi.get("strClass");
					classStr += "<option value=\""+classnamei+"\">"+classnamei+"</option>";
				}
				classStr += "</select>";
			}
			else{
				classStr = "<input type=\"text\" id=\"classinfo\" name=\"classinfo\" maxlength=\"60\">";
			}
		}else{
			classStr = "<input type=\"text\" id=\"classinfo\" name=\"classinfo\" maxlength=\"60\">";
		}


		NewsType newsType = new NewsType();
		newsType.setJson_news_url(basePath + "mobile/serv/znfxAction!doNotNeedSessionAndSecurity_getFxjhPageData.action?category=0&u="+accountNum);
		newsType.setName("返校计划");
		newsType.setDeptName(classStr);

		super.writeJson(newsType);
	}


	/**--查询手机新闻详细，返回新闻详细,提供给前端页面--**/
	public void doNotNeedSessionAndSecurity_getMobileNews(){
		HttpServletRequest request = this.getRequest();
		long newsId = WebUtil.toLong(request.getParameter("newsId"));
		News news = this.newsService.selectById(newsId);
		super.writeJson(news);
	}
	
	
	
	
	/**--地方新闻的初始化更新--**/
	public String doNotNeedSecurity_initNewsUpdatex() {
		news = newsService.selectById(id);
		//查询手机新闻类型的1级栏目
		Map<String, Object> map = new HashMap<String, Object>();
		//这里只允许查询地方新闻类别
		map.put("origin", 2);
		map.put("parent_id", 0);
		map.put("type", 1);
		com.cy.core.user.entity.User user = (com.cy.core.user.entity.User)getSession().get("user");
		map.put("alumniId", user.getDeptId());
		List<NewsType> list1 = this.newsService.selectTypeList(map);
		List<NewsType> listweb1 = this.newsService.selectWebTypeList(map);
		
		//子栏目集合
		map = new HashMap<String, Object>();
		long parent_id = -1;
		if(news.getPCategory()==0) {
			if(news.getCategory()!=0) {
				parent_id = news.getCategory();
			}
		} else {
			parent_id = news.getPCategory();
		}
		map.put("parent_id", parent_id);
		List<NewsType> list2 = this.newsService.selectTypeList(map);
		
		parent_id = -1;
		if(news.getPCategoryWeb()==0) {
			if(news.getCategoryWeb()!=0) {
				parent_id = news.getCategoryWeb();
			}
		} else {
			parent_id = news.getPCategoryWeb();
		}
		map.put("parent_id", parent_id);
		List<NewsType> listweb2 = this.newsService.selectWebTypeList(map);
		
		String createTime = WebUtil.formatDateByPattern(news.getCreateTime(), WebUtil.YMDHMS);
		
		this.getRequest().setAttribute("news", news);
		this.getRequest().setAttribute("pCategory", news.getPCategory()==0?news.getCategory():news.getPCategory());
		this.getRequest().setAttribute("list1", list1);
		this.getRequest().setAttribute("list2", list2);	
		this.getRequest().setAttribute("pCategoryWeb", news.getPCategoryWeb()==0?news.getCategoryWeb():news.getPCategoryWeb());
		this.getRequest().setAttribute("listweb1", listweb1);
		this.getRequest().setAttribute("listweb2", listweb2);
		this.getRequest().setAttribute("createTime", createTime);
		return "initNewsUpdatex";
	}
	
	public String getById() {
		news = newsService.selectById(id);
		news.setAlumniidStr(getAlumniContentStr(news.getAlumniid()));
		String createTime = WebUtil.formatDateByPattern(news.getCreateTime(), WebUtil.YMDHMS);			
		this.getRequest().setAttribute("createTime", createTime);
		return "viewNews";
	}
	
	private String getAlumniContentStr(String alumniids){
		String rs = "";
		if(StringUtils.isNotBlank(alumniids)){
			String[] alumniArray = alumniids.split(",");
			
			List<Alumni> as = alumniService.selectAll( "地" );
			
			
			for(int i = 0;i<alumniArray.length;i++){
				String alumni = alumniArray[i];
				if(alumni!=null){alumni=alumni.trim();}
				for(int j = 0;j < as.size(); j++){
					Alumni a = as.get(j);
					String alumniId = String.valueOf(a.getAlumniId());
					if(alumniId.trim().equals(alumni)){
						rs += a.getAlumniName();
						rs += ",";
						break;
					}
				}
			}
		}
		if(rs!=null && rs.length()>0 && rs.lastIndexOf(",")==rs.length()-1){
			rs = rs.substring(0, rs.length()-1);
		}
		return rs;
	}

	public String getByIdx() {
		news = newsService.selectById(id);
		String createTime = WebUtil.formatDateByPattern(news.getCreateTime(), WebUtil.YMDHMS);			
		this.getRequest().setAttribute("createTime", createTime);
		return "viewNewsx";
	}
	/**
	 *创建返校计划
	 *
	 */

	public void doNotNeedSessionAndSecurity_addFxjh() {
		HttpServletRequest request = this.getRequest();
		String accountNum = request.getParameter("accountNum");
		System.out.println("############################accountNum###"+accountNum);
		System.out.println("###############################"+topic);
		System.out.println("###############################"+number);
		System.out.println("###############################"+time);
		System.out.println("###############################"+classinfo);
		System.out.println("###############################"+other);

		Message message = new Message();
		JSONObject json = new JSONObject();
		if (news == null) {
			news = new News();
		}

		Fxjh fxjh = new Fxjh();
		fxjh.setTopic(topic);
		fxjh.setNumber(number);
		fxjh.setTime(time);
		fxjh.setClassinfo(classinfo);
		fxjh.setUserId(accountNum);
		fxjh.setOther(other);
		boolean rs = schoolServService.saveFxjh(fxjh);

		Map<String, Object> map = new HashMap<String, Object>();
		message.setMsg("提交成功");
		message.setSuccess(true);
		message.setReturnId(String.valueOf(123));
		super.writeJson(message);
		return;
	}
	//*******************************返校计划**************************************//
	//得到列表并且分页
	public void dataGraid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		map.put("topic",topic);
		map.put("status",status);
        /*if(association != null && !association.equals("")){
            map.put("accountId",association.getAccountId());
        }*/
		DataGrid<Fxjh> data = schoolServService.dataGridFxjh(map);
		super.writeJson(data);
	}

	/**
	 *获取返校计划列表
	 *
	 */
	public void getFxjhList()
	{
		Map<String, Object> map = new HashMap<>();
		List<Fxjh> fxjhList = schoolServService.getFxjhList(map);

		super.writeJson(fxjhList);
	}

	public void saveFxjh(){
		//富文本是否转换 Lixun 2017.5.5
		if( Global.IS_RICH_TEXT_CONVERT == 1 ) {
			String content = Base64Utils.getFromBase64(fxjh.getContent().replaceAll("</?[^>]+>", ""));
			fxjh.setContent(content);
		}
		Message message = new Message();
		//生成签到码
		if(fxjh.getNeedSignIn().equals("1")) {
			fxjh.setSignInCode(generateSignInCode());
		}
		try{
			schoolServService.saveFxjh(fxjh);
			// 创建群组
			ChatGroup group = new ChatGroup();
			group.setName(fxjh.getTopic());
			group.setIntroduction("后台创建返校计划群");
			chatGroupService.insert(group);
			String groupId = group.getId() ;
			//更新返校计划群字段
			Map<String, String> map = new HashMap<>();
			map.put("id",fxjh.getId());
			map.put("groupId",groupId);
			schoolServService.updateWebFxjhGroupId(map);
			message.init(true,"新增成功",null);
		}catch (Exception e){
			logger.error(e, e);
			message.init(false,"新增失败", null);
		}
		super.writeJson(message);
	}

	private String generateSignInCode() {
		Random r = new Random();
		int x = r.nextInt(9999);
		String code = String.format("%04d", x);
		return code;
	}

	public void updateFxjh(){
		Message message = new Message();
		try{
			schoolServService.updateFxjh(fxjh);
			message.init(true,"修改成功",null);
		}catch (Exception e){
			logger.error(e, e);
			message.init(false,"修改失败", null);
		}
		super.writeJson(message);
	}

	public void deleteFxjh(){
		Message message = new Message();
		try{
			schoolServService.deleteFxjh(ids);
			message.init(true, "删除成功", null);
		}catch(Exception e){
			logger.error(e, e);
			message.init(false, "删除失败", null);
		}
		super.writeJson(message);
	}
	/**
	 *返校计划详情
	 *
	 */
	public void getFxjhById()
	{
		Fxjh tmp = schoolServService.selectByFxjhId(fxjh.getId());
		super.writeJson(tmp);
	}

	/**
	 *审核计划
	 *
	 */

	public void audit() {
		Message message = new Message();
		try {
			schoolServService.updateFxjh(fxjh);
			message.setMsg("审核成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("审核失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void doNotNeedSessionAndSecurity_getSignPeople(){
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("backSchoolId", fxjh.getId() );
		super.writeJson(schoolServService.getBackSchoolSign(map));
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public void addToWebpageDB() {
		Message message = newsService.addTowebpageDB();
		super.writeJson(message);
	}

	public static void main(String[] args) {
		String pic = "http%3A%2F%2F122.205.9.115%3A8088%2Fimage%2F20150619%2F20150619120402_841_MIN.jpg";
		String fileExt = pic.substring(pic.lastIndexOf("."));
		String temp = pic.substring(0, pic.lastIndexOf(".") - 4);
		System.out.println(fileExt);
		System.out.println(temp);
	}

	public String getAultId() {
		return aultId;
	}

	public void setAultId(String aultId) {
		this.aultId = aultId;
	}


	public Fxjh getFxjh() {
		return fxjh;
	}

	public void setFxjh(Fxjh fxjh) {
		this.fxjh = fxjh;
	}
}

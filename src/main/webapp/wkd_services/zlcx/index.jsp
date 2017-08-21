<%@ page language="java" import="java.util.*,com.cy.core.news.entity.*,com.cy.core.majormng.service.*,com.cy.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.apache.poi.util.SystemOutLogger" %>
<% News news = (News)request.getAttribute("news");
String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  String category = request.getParameter("category");
    org.springframework.context.ApplicationContext ac = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application);
    com.cy.core.news.service.NewsService newsService=(com.cy.core.news.service.NewsService)ac.getBean("newsService");
    List<com.cy.core.news.entity.NewsType> rsType = newsService.selectLeveList("0");
    if(rsType!=null && rsType.size() > 0){
        for(int i = 0; i < rsType.size(); i++){
            com.cy.core.news.entity.NewsType typei = rsType.get(i);
            String namei = typei.getName();
            if(namei!=null&&namei.trim().equalsIgnoreCase("资料查询")){
                long id = typei.getId();
                category = String.valueOf(id);
            }
        }
    }
    //----
String accountNum = request.getParameter("accountNum");
if(accountNum==null){accountNum="%20";}
%>


<!DOCTYPE html>
<html>
<head>
    <title>资料查询</title>
    <meta name="Description" content=""/>
    <meta name="Keywords" content="窗友"/>
    <meta name="author" content="Rainly"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="<%=path %>/mobile/css/cy_core.css">
    <link rel="stylesheet" href="<%=path %>/mobile/css/font-awesome.min.css">
    <link rel="stylesheet" href="<%=path %>/mobile/css/news.css">
</head>
<body>
<section class="ui-container">
    <div class="wrapper">
        <div class="inner">
            <div class="ui-slider">
            </div>
        </div>
    </div>
</section>
<script src="<%=path %>/mobile/js/zepto.js"></script>
<script src="<%=path %>/mobile/js/cy_core.js"></script>
<script src="<%=path %>/mobile/js/dropload.min.js"></script>
<script src="<%=path %>/mobile/js/template_news.js"></script>
<script src="<%=path %>/mobile/js/B.js"></script>
<script>
	var start = 0;
	var rows = 10;
	var category = 0;
    var accountNum = '<%= accountNum %>';
    var jsonStr = {
        "title":"资料查询",
        "btn1":{
            "imgname":"icon_Back@2x.png",
            "imgversion":"20170214",
            "imgbase64":"iVBORw0KGgoAAAANSUhEUgAAABoAAAAsCAYAAAB7aah+AAAAAXNSR0IArs4c6QAAAN1JREFUWAntlkEOgkAMRRkTVl7EA3gPD+D53MjGa7lyr4vxF2gghkIHqnHxmzQdMp3+6YOkVFWw5ZxP8Cu8Di49lOtFnohiN3i8GIpKJyqCZWuxYig5JdJrdZ3thsbXrUQEJxt4PCa90kIn0tF2dBRR3K5IXC5MmkRcSsIVicuFSZOIS0l4owy+M3xuaF1SSi9vQTMP76aGy4CyTP4DZIpuNxSiGDHaBPiB2GwKdoixAJadSow2m4Kdf8N4x4X2Bfe3U2c6e2DvaJ9csTMhFi+i9xqJfU/kQ+ygzz+Pb1Kohi6oSMRTAAAAAElFTkSuQmCC",
            "name":"返回",
            "action":"fallback"
        }
    }
    function menuConfig() {
        window.webkit.messageHandlers.AppModel.postMessage({body: jsonStr});
        return jsonStr;

    }
    function jsonConfig() {
        var str = JSON.stringify(jsonStr);
        window.stub.jsMethod(str);
        return jsonStr;

    }

    function fallback() {

        window.location.href = B.serverUrl + "/mobile/services/index_body.html?accountNum=" + accountNum;

    }

    Zepto(function ($) {
        //判断栏目是否有子栏目
        $.ajax({
            type: 'GET',
            //url: "json/news_type_2.json",
            url: "<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getMobileNewsType.action?category=<%=category %>",
            dataType: 'json',
            beforeSend: function(xhr, settings) {

            },
            success: function(data){
                //console.log(data.leveList.length);
                if (data && data.leveList) {
                    if(data.leveList.length==0){
                        category = data.id;
                        var params = "?category="+category+"&start="+start+"&rows="+rows+"&u=<%=accountNum%>";
                        var htmlTpl = '<ul class="ui-list ui-border-tb"></ul><div class="more" style="display: none;"><a href="javascript:;" alt='+category+'>点击查看更多</a></div><div class="ui-loading-wrap" style="display:none"><i class="ui-loading"></i><p>加载中</p></div>';
                        $('.inner').append(htmlTpl);
                        //getPageData("json/news_list.json",".ui-list","onload",newsList,null,"showMore");
                        getPageData("<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action"+params,".ui-list","onload",newsList,null,"showMore");
                        sigleCategory();
                    }else if(data.leveList.length == 1){
                        category = data.leveList[0].id;
                        var params = "?category="+category+"&start="+start+"&rows="+rows+"&u=<%=accountNum%>";
                        var htmlTpl = '<ul class="ui-list ui-border-tb"></ul><div class="more" style="display: none;"><a href="javascript:;" alt='+category+'>点击查看更多</a></div><div class="ui-loading-wrap" style="display:none"><i class="ui-loading"></i><p>加载中</p></div>';
                        $('.inner').append(htmlTpl);
                        //getPageData("json/news_list.json",".ui-list","onload",newsList,null,"showMore");
                        getPageData("<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action"+params,".ui-list","onload",newsList,null,"showMore");
                        sigleCategory();
                    } else {
                    console.log("<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getMobileNewsType.action?category=<%=category %>");
                        var htmlTpl = '<div class="ui-tab"></div>';
                        $('.inner').append(htmlTpl);
                        getPageData("<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getMobileNewsType.action?category=<%=category %>", ".ui-tab", "", tabNav, null, "tabNav");
                        multiCategory();
                    }
                }
                else{
                    $('.inner').append("暂无数据");
                }
            },
            error: function(xhr, type){
                //console.log('Ajax error!');
            },
            complete: function(xhr, type){

            }
        });
        function sigleCategory(){
            var dropload = $('.inner').dropload({
                domUp : {
                    domClass   : 'dropload-up',
                    domRefresh : '<div class="dropload-refresh"><i class="fa fa-long-arrow-down"></i>下拉刷新</div>',
                    domUpdate  : '<div class="dropload-update"><i class="fa fa-long-arrow-up"></i> 释放更新</div>',
                    domLoad    : '<div class="dropload-load"><div class="ui-loading-wrap"><i class="ui-loading"></i><p>加载中</p></div></div>'
                },
                loadUpFn : function(me){
                	var temUrl = "<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action"+"?category="+category+"&start=0&rows=10"+"&u=<%=accountNum%>";
                    //alert(temUrl);
                    getPageData("<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action?category=<%=category %>&topnews=100"+"&u=<%=accountNum%>",".ui-slider","update",sliderNews,me,"slider");
                    getPageData(temUrl,".ui-list","update",newsList,me);
                }
            });
        }

        function multiCategory(){
            var dropload = $('.inner').dropload({
                domUp : {
                    domClass   : 'dropload-up',
                    domRefresh : '<div class="dropload-refresh"><i class="fa fa-long-arrow-down"></i>下拉刷新</div>',
                    domUpdate  : '<div class="dropload-update"><i class="fa fa-long-arrow-up"></i> 释放更新</div>',
                    domLoad    : '<div class="dropload-load"><div class="ui-loading-wrap"><i class="ui-loading"></i><p>加载中</p></div></div>'
                },
                loadUpFn : function(me){
                    //me.resetload();
                    var _temUrl = $('.ui-tab-nav .current').data('href')+"&start=0&rows=10";
                    console.log(_temUrl);
                    getPageData("<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action?category=<%=category %>&topnews=100"+"&u=<%=accountNum%>",".ui-slider","update",sliderNews,me,"slider");
                    getPageData(_temUrl, ".ui-tab-content .current .ui-list", "update", newsList, me, "showMore");
                }
            });
            //dropload.loadUpFn();
        }



        //查看更多
        $(document).on('tap','.more',function(){
            var target = $(this);
            start = $('.ui-list > li').length;
            //var temUrl = target.children('a').attr('alt')+"?category="+category+"&start="+start+"&rows="+rows;
            var temUrl = "";
            if(target.children('a').attr('alt')>0){
            	temUrl = "<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action"+"?category="+target.children('a').attr('alt')+"&start="+start+"&rows="+rows+"&u=<%=accountNum%>";
            }else{
            	temUrl = $('.ui-tab-nav .current').data("href")+"&start="+start+"&rows="+rows+"&u=<%=accountNum%>";
            }
            console.log(temUrl);
            $.ajax({
                type: 'GET',
                url: temUrl,
                dataType: 'json',
                beforeSend: function(xhr, settings) {
                    target.hide();
                    target.next().show();
                    //console.log(target.next())
                },
                success: function(data){
                    if(data && data.newsList) {
                        if(data.newsList.length == 0){
                            $('.more >a').text("没有更多了");
                            return;
                        }
                        var result=$.tpl(newsList,data);
                        $(target.prev()).append(result);
                    }
                    else{
                        $('.more >a').text("没有更多了");
                    }
                },
                error: function(xhr, type){
                    //alert('Ajax error!');
                },
                complete: function(xhr, type){
                    target.show();
                    target.next().hide();
                }
            });
        });

 	$(document).on('tap','.ui-list > li',function(){
        if($(this).data('href')){
            location.href= $(this).data('href') + "&sourceFlag=zlcx";
        }
    });
    $(document).on('tap','.ui-slider-content > li',function(){
        if($(this).data('href')){
            location.href= $(this).data('href') + "&sourceFlag=zlcx";
        }
    });
        //初始加载,图片新闻<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getMobileNewsType.action?category=<%=category %>
        //getPageData("json/news_list_slider.json", ".ui-slider", "", sliderNews, null, "slider");
        //getPageData("json/news_type.json", ".ui-tab", "", tabNav, null, "tabNav");
        
        getPageData("<%=path%>/mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action?category=<%=category %>&topnews=100"+"&u=<%=accountNum%>", ".ui-slider", "", sliderNews, null, "slider");
    });
</script>
</body>
</html>
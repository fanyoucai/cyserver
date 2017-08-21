<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.cy.util.WebUtil"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
long check = WebUtil.toLong(request.getParameter("check")) ;
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	  <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=j5bQGIAXNd5rrnfu83is90HhX1n3xvMd"></script>
	<jsp:include page="../../../inc.jsp"></jsp:include>

	  <style>
		  .ta001 th{
			  width: 107px;
		  }
		  .ta001 td{
			  height: 33px;
		  }
		  #bdMap{
			  width: 500px;
			  height: 400px;
		  }
		  #locationDescSpan{
			  width: 500px;
			  text-align: center;
		  }

	  </style>
	<script type="text/javascript">
		var map;
		var editor;
		KindEditor.ready(function(K) {
			editor =K.create('#description',{
				 fontSizeTable:['9px', '10px', '11px', '12px', '13px', '14px', '15px', '16px', '17px', '18px', '19px', '20px', '22px', '24px', '28px', '32px'],
				 uploadJson : '${pageContext.request.contextPath}/fileUpload/fileUploadAction!doNotNeedSecurity_fileUpload.action',
				 readonlyMode : true,
				 afterChange:function(){
						this.sync();
					}
			});
		});


		var poiIcon = new BMap.Icon("${pageContext.request.contextPath}/images/star.png", new BMap.Size(23, 25));
		var clickIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
			offset: new BMap.Size(10, 25), // 指定定位位置
			imageOffset: new BMap.Size(0, 0 - 10 * 25) // 设置图片偏移
		});

		var preMarker ;

		/**
		 * 创建百度地图实例
		 * @param longitude
		 * @param latitude
		 */
		function createMap(div,longitude,latitude ,hasVal ,needClick) {// 创建Map实例
			map= new BMap.Map(div, {enableMapClick:false});
			// 初始化地图,设置中心点坐标和地图级别
			var point = new BMap.Point(longitude,latitude);
			map.centerAndZoom(point,12);
			// 创建标注对象并添加到地图
			var poiMarker ;
			if(hasVal == 1) {
				poiMarker = new BMap.Marker(point,{icon: clickIcon});
				preMarker = poiMarker ;
			} else {
				poiMarker = new BMap.Marker(point,{icon: poiIcon});
			}
			map.addOverlay(poiMarker);


			var geoc = new BMap.Geocoder();
			//添加地图类型控件
//			map.addControl(new BMap.MapTypeControl());
			//开启鼠标滚轮缩放
			map.enableScrollWheelZoom(true);
			//添加默认缩放平移控件
			map.addControl(new BMap.NavigationControl());
			var localSearch = new BMap.LocalSearch(map);
			//允许自动调节窗体大小
			localSearch.enableAutoViewport();

			if(needClick == 1) {
				//点击地图上某一点获取这个地点的经纬度坐标和详细的地理位置（省、市、区、街道名称、街道门牌号）
				//并将这个地址的经纬度坐标以及详细的地址记录下来之后赋值给数据库里的字段，点击保存的时候，将数据保存到数据库中。
				map.addEventListener("click", function (e) {
					if (e.overlay != poiMarker) {
						map.removeOverlay(e.overlay);
					}
					var marker = new BMap.Marker(e.point, {icon: clickIcon});
					map.removeOverlay(preMarker);
					map.addOverlay(marker);
					preMarker = marker;

					var pt = e.point;
					$('#longitude').val(e.point.lng);
					$('#latitude').val(e.point.lat);
					geoc.getLocation(pt, function (rs) {
						var addComp = rs.addressComponents;
						var str = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
						$('#locationDesc').val(str);
						$('#locationDescSpan').html(str);
					});
				});
			}
		}
		$(function () {
			var id = '${param.enterpriseId}';
			$.ajax({
				url: '${pageContext.request.contextPath}/cloudEnterprise/cloudEnterpriseAction!getById.action',
				data: { 'enterpriseId' : id },
				dataType: 'json',
				success: function (result) {

					if (result && result.id != undefined) {

						console.log(result);
						$('#name').text(result.name?result.name:"");
						$('#mainBusiness').text(result.mainBusiness?result.mainBusiness:"");
						$('#website').text(result.website?result.website:"");
						$('#recruitEmail').text(result.recruitEmail?result.recruitEmail:"");
						$('#serviceArea').text(result.serviceArea?result.serviceArea:"");
						$('#linkman').text(result.linkman?result.linkman:"");
						$('#contactNumber').text(result.contactNumber?result.contactNumber:"");
						$('#slogan').text(result.slogan?result.slogan:"");
						$('#city').text(result.city?result.city:"");
						$('#longitude').text(result.longitude?result.longitude:"");
						$('#latitude').text(result.latitude?result.latitude:"");
						$('#locationDesc').text(result.locationDesc?result.locationDesc:"");
						$('#summary').text(result.summary?result.summary:"");
						$('#type').text(result.type?result.type:"");
						$('#financingStage').text(result.financingStage?result.financingStage:"");
						$('#address').text(result.address);
						$('#citySpan').text(result.city);
						$('#locationDescSpan').text(result.locationDesc);

						var longitude = result.longitude;
						var latitude = result.latitude;
						var hasVal = 0 ;
						if((longitude != null && longitude != "") && (latitude != null && latitude != "") ) {
							hasVal = 1;
						} else {
							longitude = 116.331398;
							latitude = 39.897445 ;
						}

						createMap("allmap",longitude,latitude,hasVal,0) ;

//						editor.html(result.description);

						if(result.posterPic){
							$('#posterPic').append('<img src="'+ result.posterPic +'" width="200px" style="margin:10px" height="80px"/>');
						}
						if(result.logo){
							$('#logoPic').append('<img src="'+ result.logo +'" width="80px" style="margin:10px" height="80px"/>');
						}


					}
				},

				complete: function () {

					parent.$.messager.progress('close');

				}
			});
		});


		$(document).ready(function () {
			$('#city').combobox({
				onChange: function (n, o) {
					alert(1);
					var search = new BMap.LocalSearch("中国", {
						onSearchComplete: function(result){
							if (search.getStatus() == BMAP_STATUS_SUCCESS){
								var res = result.getPoi(0);
								var point = res.point;
								map.centerAndZoom(point, 12);
							}
						},renderOptions: {  //结果呈现设置，
							map: map,
							autoViewport: true,
							selectFirstResult: true
						}
					});
					var city1 = $("#city").combobox('getValue');
					if(city1 && city1 != '' && city1 != undefined  && city1 != '北京市' && city1 != '北京' ){
						search.search(city1);
					}else{
						search.search("北京");
					}
				}
			});
		});


	</script>
</head>
  
  <body>
  <form method="post" id="viewAssociationForm" style="position: relative">
	  <fieldset>
		  <legend>
			  企业信息
		  </legend>
		  <div style="width:500px;height:390px;border:2px solid gray;position: absolute; top:160px; left:130px;margin-top:10px;margin-left: 10px;" id="allmap"></div>
		  <table class="ta001">

			  <tr>
				  <th>
					  企业名称
				  </th>
				  <td colspan="3" id="name" >
					  <input name="cloudEnterprise.id"  id="enterpriseId" type="hidden" value="${param.enterpriseId}">

				  </td>
			  </tr>

			  <tr>
				  <th>
					  主营业务
				  </th>
				  <td colspan="3" id="mainBusiness">

				  </td>
			  </tr>

			  <tr id="rg123">
				  <th>城市</th>
				  <td colspan="3">
					  <span id="citySpan" name="cloudEnterprise.city"></span>

				  </td>
			  </tr>

			  <tr id="rg223" style="display: none;">
				  <th>城市</th>
				  <td colspan="3">
					  <input name="cloudEnterprise.city" id="resourceArea2" type="hidden"/>
					  <input class="easyui-combobox" name="resourceAreaProvince" id="province"
							 data-options="
	                    method:'post',
	                    url:'${pageContext.request.contextPath}/province/provinceAction!doNotNeedSecurity_getProvince2ComboBox.action?countryId=1',
	                    valueField:'provinceName',
	                    textField:'provinceName',
	                    editable:false,
	                    prompt:'省',
	                    icons:[{
			                iconCls:'icon-clear',
			                handler: function(e){
			                	$('#province').combobox('clear');
			                	$('#city').combobox('clear');
			                	$('#city').combobox('loadData',[]);
			                	$('#area').combobox('clear');
								$('#area').combobox('loadData',[]);
			                }
			            }],
			            onSelect: function(rec){
							var url = '${pageContext.request.contextPath}/city/cityAction!doNotNeedSecurity_getCity2ComboBox.action?provinceId='+rec.id;
							$('#city').combobox('clear');
							$('#city').combobox('reload', url);
							$('#area').combobox('clear');
							$('#area').combobox('loadData',[]);
						}
                    	"/>
					  &nbsp; <input class="easyui-combobox" name="resourceAreaCity" id="city"
									data-options="
	                    method:'post',
	                    valueField:'cityName',
	                    textField:'cityName',
	                    editable:false,
	                    prompt:'市',
	                    icons:[{
			                iconCls:'icon-clear',
			                handler: function(e){
			                	$('#city').combobox('clear');
			                	$('#area').combobox('clear');
								$('#area').combobox('loadData',[]);
			                }
			            }],
                    	"/>

				  </td>
			  </tr>

			  <tr>
				  <th>
					  企业地址
				  </th>
				  <td colspan="3">
					  <input  name="enterprise.longitude" id="longitude" type="hidden" class="easyui-validatebox"/>
					  <input  name="enterprise.latitude"  id="latitude" type="hidden" class="easyui-validatebox"/>
					  <input  name="enterprise.locationDesc" id="locationDesc" type="hidden" class="easyui-validatebox"/>
					  <span id="address"></span>
				  </td>
			  </tr>

			  <tr>
				  <th>
					  选择详细企业地址
				  </th>
				  <td>
					  <div id="bdMap" style="margin-left: 10px;"></div>
					  <div id="locationDescSpan"></div>
				  </td>
			  </tr>


			  <tr>
				  <th>
					  网址
				  </th>
				  <td colspan="3" id="website">
				  </td>
			  </tr>


			  <tr>
				  <th>
					  企业邮箱
				  </th>
				  <td colspan="3" id="recruitEmail">
				  </td>
			  </tr>

			  <tr>
				  <th>
					  联系人名称
				  </th>
				  <td colspan="3" id="linkman">

				  </td>
			  </tr>

			  <tr>
				  <th>
					  联系电话
				  </th>
				  <td colspan="3" id="contactNumber">

				  </td>
			  </tr>
			  <tr>
				  <th>
					  标语
				  </th>
				  <td colspan="3" id="slogan">

				  </td>
			  </tr>

			  <tr>
				  <th>公司简介</th>
				  <td>
					  <textarea id="summary" rows="5" cols="100" name="cloudEnterprise.summary" style="width: 700px; height: 150px;" disabled></textarea>
				  </td>
			  </tr>

		<%--	  <tr>
				  <th>图文详情</th>
				  <td>
					  <textarea id="description" rows="5" cols="100" name="cloudEnterprise.description" style="width: 700px; height: 250px;" disabled></textarea>
				  </td>
			  </tr>--%>
			  <tr>
				  <th>Logo</th>
				  <td>
					  <div style="float:left;width:180px;" id="logoPic">
					  </div>
				  </td>
			  </tr>
			<%--  <tr>
				  <th>海报图片</th>
				  <td>
					  <div style="float:left;width:180px;" id="posterPic">
					  </div>
				  </td>
			  </tr>--%>
		  </table>
	  </fieldset>
</form>
  </body>
</html>
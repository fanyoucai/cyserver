<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">

    <title>编辑黑白名单</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <jsp:include page="../../../inc.jsp"></jsp:include>
    <script type="text/javascript">
        $(function () {
            if ($('#id').val() > 0) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/schoolInfo/schoolInfoAction!getById.action',
                    data: $('form').serialize(),
                    dataType: 'json',
                    success: function (result) {
                        if (result['id']) {
                            $('form').form('load', {
                                'schoolInfo.id': result['id'],
                                'schoolInfo.schoolId': result['schoolId'],
                                'schoolInfo.name': result['name'],
                                'schoolInfo.key': result['key']
                            });
                        }
                    },
                    beforeSend: function () {
                        parent.$.messager.progress({
                            text: '数据加载中....'
                        });
                    },
                    complete: function () {
                        parent.$.messager.progress('close');
                    }
                });
            }
        });
        
        
        
        /**--编辑后保存--**/
        var submitForm = function ($dialog, $grid, $pjq) {
            if ($('form').form('validate')) {
                var url;
                url = '${pageContext.request.contextPath}/schoolInfo/schoolInfoAction!update.action';
                $.ajax({
                    url: url,
                    data: $('form').serialize(),
                    dataType: 'json',
                    success: function (result) {
                        if (result.success) {
                            $grid.datagrid('reload');
                            $dialog.dialog('destroy');
                            $pjq.messager.alert('提示', result.msg, 'info');
                        } else {
                            $pjq.messager.alert('提示', result.msg, 'error');
                        }
                    },
                    beforeSend: function () {
                        parent.$.messager.progress({
                            text: '数据提交中....'
                        });
                    },
                    complete: function () {
                        parent.$.messager.progress('close');
                    }
                });
            }
            
            
        };
        
        /**--通过ajax得到更新后的KEY--**/
        function updateKey(){
            $.ajax({
            	url: "${pageContext.request.contextPath}/schoolInfo/schoolInfoAction!doNotNeedSessionAndSecurity_updateKey.action",
	            type: "GET",
	            data: "",
	            dataType: "text",
	            success: function(data){
	            	$("#key").val(data);
	            }
            });
        }
        
    </script>
</head>

<body>
<form method="post" class="form">
<input id="id" value="${param.id}" name="schoolInfo.id" type="hidden">
    <fieldset>
        <legend>
            基本信息
        </legend>
        <table class="ta001">
            <tr>
                <th>
                    学校编号
                </th>
                <td>
                    <input name="schoolInfo.schoolId" class="easyui-validatebox" style="width: 250px"
               				readonly="readonly"
                           data-options="
                                required:true,
                                validType:'deptNo'
                           ">
                    </input>
                </td>
            </tr>         
            <tr>
            	 <th>
                    学校名称
                </th>
                <td>
                    <input name="schoolInfo.name" class="easyui-validatebox" style="width: 250px"
                           data-options="
                                required:true
                           ">
                    </input>
                </td>
            </tr>
            
            
            <tr>
                <th>
                   学校密钥
                </th>
                <td>
                   <input id="key" name="schoolInfo.key" class="easyui-validatebox" style="width: 250px"
                   	readonly="readonly"
                           data-options="
                                required:true
                           ">
                    </input>
                    
                  	<input type="button" onclick="updateKey();" value="修改密钥" />
                </td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>

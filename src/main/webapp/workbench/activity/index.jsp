<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
	%>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){

		//查询市场活动
		function getActivity(pageNo, pageSize){

			//每次刷新市场活动列表之前，需要将全选的复选框取消选择
			$("#fatherChecked").prop("checked", false)

			//查询之前，将条件框恢复到上一次查询的条件
			$("#search-name").val($.trim($("#hidden-name").val()))
			$("#search-owner").val($.trim($("#hidden-owner").val()))
			$("#search-startDate").val($.trim($("#hidden-startDate").val()))
			$("#search-endDate").val($.trim($("#hidden-endDate").val()))
			$.ajax({
				url : "workbench/activity/queryActivity.do",
				type : "get",
                data : {
                    "pageNo" : pageNo,
                    "pageSize" : pageSize,
                    "name" : $.trim($("#search-name").val()),
                    "owner" : $.trim($("#search-owner").val()),
                    "startDate" : $.trim($("#search-startDate").val()),
                    "endDate" : $.trim($("#search-endDate").val()),
                },
				dataType : "json",
				success : function (data) {
					var html = "";
					$.each(data.activityData, function (i, n) {

						html += "<tr class='active'><td><input name='sonChecked' type='checkbox' value=" + n.id +" /></td>" +
								"<td><a style='text-decoration: none; cursor: pointer;' onclick=\"window.location.href='workbench/activity/detail.jsp';\">" +
								n.name + "</a></td>" +
								"<td>" + n.owner +"</td>" +
								"<td>" + n.startDate + "</td>" +
								"<td>" + n.endDate + "</td>" +
								"</tr>"

					})
					$("#showActivity").html(html);
					//计算总页数
                    var totalPages = data.countSize%pageSize==0 ? data.countSize/pageSize : parseInt(data.countSize/pageSize) + 1
					//分页插件
                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.countSize, // 总记录条数
                        visiblePageLinks: 1, // 显示几个卡片
                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        onChangePage : function(event, data){
                            getActivity(data.currentPage , data.rowsPerPage);
                        }
                    });

                }
			})
		}
		//当页面加载完毕后，更新市场活动列表
        getActivity(1, 2)

        //当点击查询按钮后，更新市场活动列表
        $("#search-btn").click(function () {
        	//点击查询按钮后，将查询条件框中的数据保存到隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-startDate").val($.trim($("#search-startDate").val()))
			$("#hidden-endDate").val($.trim($("#search-endDate").val()))
            getActivity(1, 2)
        })

		$("#addActivity").click(function () {

			/*打开模态窗口之前应该动态展现所有者下拉框中的数据*/

			$.ajax({
				url : "workbench/activity/getUser.do",
				type : "get",
				dataType : "json",
				success : function (data) {
					var html = ""
					$.each(data, function (i, n) {
						html += "<option value=" + n.id + ">" + n.name + "</option>"
					})
					$("#createOwner").html(html);
					$("#createOwner").val("${user.id}");
				}
			})

			//打开创建模态窗口
			$("#createActivityModal").modal("show");
		})

		//时间控件
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//点击模态窗口中的保存按钮后执行的代码
		$("#saveActivity").click(function () {

			//使用Ajax给后台发送用户填写的数据并插入到数据库中
			$.ajax({
				url : "workbench/activity/saveActivity.do",
				type : "post",
				data : {
					owner : $.trim($("#createOwner").val()),
					name : $.trim($("#createName").val()),
					startDate : $.trim($("#createStartDate").val()),
					endDate : $.trim($("#createEndDate").val()),
					cost : $.trim($("#createCost").val()),
					description : $.trim($("#createDescription").val()),
					createBy : $.trim($("#createOwner").val())
				},
				dataType : "json",
				success : function (data) {
					if (data.success){
						//成功插入后，将模态窗口关闭，同时重置模态窗口的内容并刷新市场活动区域
                        //关闭模态窗口
						$("#createActivityModal").modal("hide")
                        //重置模态窗口的内容
                        $("#addActivityForm")[0].reset()
						//访问数据库，查询出所有订单
                        getActivity(1, 2);
					}
				}
			})

		})
		
		//为全选复选框绑定单击事件
		$("#fatherChecked").click(function () {
			$("input[name=sonChecked]").prop("checked",this.checked)
		})

		//为动态生成的复选框绑定单击事件
		/*
		* 	动态生成的元素不能以普通绑定事件的方式绑定事件，需要以on方法来绑定事件
		* 	语法：$(需要绑定事件的有效外层元素).on(绑定的事件，需要绑定元素的jQuery对象，回调函数)
		*  有效外层元素是指不是动态生成上一级的元素。
		* */
		$("#showActivity").on("click", $("input[name=sonChecked]"), function () {
			$("#fatherChecked").prop("checked", $("input[name=sonChecked]").length == $("input[name=sonChecked]:checked").length)
		})
		/*
		* 代码解读：
		* 	$("#showActivity"):需要绑定的元素是input标签，上一层是td，动态生成的，再上一层是tr，也是动态生成的，再上一层是tbody，tbody才是有效外层元素
		* 	on("click", $("input[name=sonChecked]"), function () {}:
		* 	绑定单击事件	 选择所有name=sonChecked的标签，这些标签都是动态生成的
		* 	function () {		回调函数
		* 		$("#fatherChecked").prop("checked", $("input[name=sonChecked]").length == $("input[name=sonChecked]:checked").length)
		* 	$("#fatherChecked").prop：改变#fatherChecked复选框的状态。
		*  		checked：改为选定状态
		* 		$("input[name=sonChecked]").length == $("input[name=sonChecked]:checked").length：改成选定状态的条件
		* 		name=sonChecked的input标签的长度等于name=sonChecked的input标签的所有选中的长度，即全选时，将fatherChecked复选框改为选择状态
		* 	}
		*
		*
		* */

		//删除
		$("#deleteBtn").click(function () {
			if ($("input[name=sonChecked]:checked").length==0){
				alert("请选择要删除的记录后再点击删除按钮")
			} else {
				if (confirm("删除后不可恢复，确认删除吗？")){
					var count = $("input[name=sonChecked]:checked").length
					var param = "";
					for (i = 0; i < count; i++) {
						param += "id=" + $("input[name=sonChecked]:checked")[i].value
						if (i < count - 1){
							param += "&"
						}
					}
					$.ajax({
						url : "workbench/activity/deleteActivity.do",
						type : "post",
						data : param,
						dataType : "json",
						success : function (data) {
							alert(data)
							if (data){
								getActivity(1, 2)
							} else{
								alert("删除失败")
							}
						}
					})
				}
			}
		})
	});
	
</script>
</head>
<body>

	<%--给四个条件框创建四个隐藏域，用来保存数据--%>
	<input type="hidden" id="hidden-name" />
	<input type="hidden" id="hidden-owner" />
	<input type="hidden" id="hidden-startDate" />
	<input type="hidden" id="hidden-endDate" />

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="addActivityForm" class="form-horizontal" role="form">
					<%--for="create-marketActivityOwner"--%>
						<div class="form-group">
							<label for="createOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="createOwner" >
								</select>
							</div>
                            <label for="createName"  class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="createName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime"  class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="createStartDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="createEndDate" >
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="createCost" >
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="createDescription" ></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveActivity">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input id="search-name" class="form-control" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input id="search-owner" class="form-control" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button id="search-btn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addActivity"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="fatherChecked" name="fatherChecked" type="checkbox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="showActivity">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>
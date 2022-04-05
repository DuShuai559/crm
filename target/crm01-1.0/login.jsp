
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
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function () {
            function login(){
                var loginName = $.trim($("#loginName").val());
                var loginPwd = $.trim($("#loginPwd").val());
                if (loginName == "" || loginPwd == ""){
                    $("#msg").html("用户名或密码不能为空");
                    return false;
                }
                $.ajax({
                    url : "user/login.do",
                    //虽是查询操作，但提供给后台的数据中包含密码，使用POST请求
                    type : "post",
                    data : {
                        loginName :$("#loginName").val(),
                        loginPwd : $("#loginPwd").val()
                    },
                    dataType : "json",
                    success : function (data) {
                        if (data.success) {
                            //登录成功跳转到登录页
                            document.location.href = "workbench/index.jsp";
                        }else{
                            //登录失败提示错误信息
                            $("#msg").html(data.msg)
                        }
                    }
                })
            }
            //让用户名自动获得焦点
            $("#loginName").focus(function () {
                //获得焦点后将错误信息处清空
                $("#msg").html("");
            });
            $("#loginPwd").focus(function () {
                //获得焦点后将错误信息处清空
                $("#msg").html("");
            });
            //单击登录按钮后执行login函数
            $("#loginBtn").click(function () {
               login();
            })
            //用户按下回车键后执行login函数
            $(window).keydown(function (event) {
                if (event.keyCode == 13){
                    login();
                }
            })
        })
    </script>
</head>
<body>


<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2022&nbsp;杜帅</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="user/login.do" class="form-horizontal" role="form" method="post">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input id="loginName" name="loginName" class="form-control" type="text" placeholder="用户名">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input id="loginPwd" name="loginPwd" class="form-control" type="password" placeholder="密码">
                </div>
                <div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

                    <span id="msg" style="color: red"></span>

                </div>
                <button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>

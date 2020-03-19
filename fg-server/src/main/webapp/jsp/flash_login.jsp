<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String errorMsg = request.getAttribute("login.invalid.msg") != null ? (String)request.getAttribute("login.invalid.msg") : "";
%>
<html>
	<head>
		<title>Game Login</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/base.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/font.css" type="text/css">
		<script type="text/javascript">
			document.onkeypress = function(we) {
				var windowEvent = we || window.event;
				if (windowEvent.keyCode == 13) {
					doLogin();
				}
			}
			function doLogin() {
				if (document.forms[0].playerId.value == '') {
					alert('Player Id is Mandetory');
					document.forms[0].playerId.focus();
					return;
				}
				if (document.forms[0].password.value == '') {
					alert('Password is Mandetory');
					document.forms[0].password.focus();
					return;
				}
				document.forms[0].submit();
			}
			function doReset() {
				document.forms[0].playerId.value = '';
				document.forms[0].password.value = '';
			}
		</script>
	</head>	
	<body onload="document.getElementById('playerId').focus()">
		<table class="LRborder" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td>						
					<!-- header -->
					<table border="0" cellspacing="0" cellpadding="0" align="center">
						<tr><td colspan="2" width="900"><img src="${pageContext.request.contextPath}/resources/images/topimg.gif" width="900" height="27"/></td></tr>
						<tr>
							<td width="157"><img src="${pageContext.request.contextPath}/resources/images/logo.gif" width="157" height="52" alt="MFRP"/></td>
							<td width="743" align="right" class="bannerbg">
						</tr>
					</table>
					<table border="0" cellspacing="0" cellpadding="0" align="center">
						<tr><td width="900" class="menubg" height="35">&nbsp;</td></tr>
					</table>
					<table width="900" border="0" align="center" cellpadding="0" cellspacing="0" class="mainbg">
						<tr>
							<td width="860" height="50" style="padding-left:40px"><h2>Game Login</h2></td>
						</tr>
						<tr><td align="center"><img src="${pageContext.request.contextPath}/resources/images/line.gif" width="860" height="2" /></td></tr>
						<tr><td align="right" height="30">You are not currently logged in.</td></tr>
						<tr><td align="right" height="30"></td></tr>
						<tr>
							<td width="860" align="center">
								<table border="0" align="center" cellpadding="0" cellspacing="0">
									<tr>
										<td width="15" align="right"><img src="${pageContext.request.contextPath}/resources/images/titbgleft_gray.gif" width="15" height="20" /></td>
										<td width="72" align="center" class="titbg_gray white t13 bold">Log In</td>
										<td width="300" align="left" class="titbgline_gray"><img src="${pageContext.request.contextPath}/resources/images/titbgright_gray.gif" /></td>
									</tr>
									<tr>
										<td colspan="3" class="contentbg_gray grayborder">
											<form name="frmLogin" action="${pageContext.request.contextPath}/domain/login" method="post" style="frmCfg">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr><td colspan="3" height="50" valign="middle"><b></b></td></tr>
												<tr>
													<td align="center">Player Id</td>
													<td align="center">
														<input name="playerId" id="playerId" maxlength="150" size="26" value="" style="width:200px; height:20px;"/>
													</td>
												</tr>
												<tr><td colspan="2" height="5" valign="top"></td></tr>
												<tr>
													<td align="center">Password</td>
													<td align="center">
														<input type="password" name="password" id="password" maxlength="150" size="26" value="" style="width:200px; height:20px;"/>
													</td>
												</tr>
												<tr><td colspan="2" height="30" valign="top"></td></tr>
												<tr>
													<td align="center">&nbsp;</td>
													<td align="center">
														<input type="button" class="buttonbg_gen t12 bold white" value="Enter" onclick="doLogin()"/>
														<input type="button" class="buttonbg_gen t12 bold white" value="Reset" onclick="doReset()"/>
													</td>
												</tr>
												<tr>
													<td colspan="3" height="25" style="padding-left:15px; color:#CC0000">
														<span id="msg"><%=errorMsg%></span>
													</td>
												</tr>
											</table>
											<input type="hidden" name="status" value=""/>
											</form>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td colspan="2" align="right" height="30"></td></tr>
					</table>

					<!-- Login Fom -->
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="900" align="center">
								<table border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="center" height="25" class="menusubpro grayborder notopborder paddingleft5">
											<table border="0" width="380" cellpadding="0" cellspacing="0">
												<tr>
													<td align="center"><a href="${pageContext.request.contextPath}/jsp/flash_home.jsp">Flash Home</a></td>
													<td align="center"><a href="${pageContext.request.contextPath}/jsp/flash_registration.jsp">Register</a></td>
													<td align="center"><a href="${pageContext.request.contextPath}/jsp/flash_forgotpwd.jsp">Forgot password</a></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td colspan="2" height="50" colspan="2"></td></tr>
						<tr>
							<td colspan="2" height="23" colspan="2" class="LRborder footerbg white paddingleft15">
								<table border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="8">&nbsp;</td>
										<td>
											<a href="#" target="_blank">Sitemap</a>
										</td>
										<td width="20">&nbsp;</td>
										<td>
											<a href="${pageContext.request.contextPath}/jsp/flash_faq.jsp" target="_blank">FAQs</a>
										</td>
										<td width="20">&nbsp;</td>
										<td>
											<a href="#" target="_blank">Feedback</a>
										</td>
										<td width="320"></td>
										<td>
											&nbsp;
										</td>
										<td width="20">&nbsp;</td>
										<td>
											&nbsp;
										</td>
										<td width="10">&nbsp;</td>
										<td>
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div id="footer" >
			<!--disclaimer-->
			<jsp:include page="disclaimer.jsp"/>
		</div>
	</body>
</html>
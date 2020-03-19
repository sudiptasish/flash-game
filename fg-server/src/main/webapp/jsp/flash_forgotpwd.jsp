<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String passwordMsg = request.getAttribute("password.msg") != null ? (String)request.getAttribute("password.msg") : "";
%>
<html>
	<head>
		<title>Forgot Password</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/base.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/font.css" type="text/css">
		<script type="text/javascript">
			function sendPassword() {
				if (document.forms[0].playerId.value == '' || document.forms[0].email.value == '') {
					alert('All Fields are required');
					return;
				}
				document.forms[0].submit();
			}
		</script>
	</head>	
	<body>
		<table class="LRborder" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td>						
					<!-- header -->
					<table border="0" cellspacing="0" cellpadding="0" align="center">
						<tr><td colspan="2" width="760"><img src="${pageContext.request.contextPath}/resources/images/topimg.gif" width="900" height="27"/></td></tr>
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
										<td width="122" align="center" class="titbg_gray white t13 bold">Forgot Password</td>
										<td width="490" align="left" class="titbgline_gray"><img src="${pageContext.request.contextPath}/resources/images/titbgright_gray.gif" /></td>
									</tr>
									<tr>
										<td colspan="3" class="contentbg_gray grayborder">
											<form name="frmFPassword" action="${pageContext.request.contextPath}/domain/forgotpwd" method="post">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr><td colspan="2" height="30"></td></tr>
												<tr>
													<td colspan="2" style="padding-left:35px; color:#CC0000">To Retrieve your password, please enter your User Id and E-mail address. Your password will be shown on the screen.</td>
												</tr>
												<tr><td colspan="2" height="20"></td></tr>
												<tr>
													<td align="left" style="padding-left:35px">* Player Id</td>
													<td align="right" style="padding-right:40px">
														<input name="playerId" id="playerId" maxlength="150" size="26" value="" style="width:200px; height:20px;"/>
													</td>
												</tr>
												<tr><td colspan="2" height="10"></td></tr>
												<tr>
													<td align="left" style="padding-left:35px">* Email Address</td>
													<td align="right" style="padding-right:40px">
														<input name="email" id="email" maxlength="150" size="26" value="" style="width:200px; height:20px;"/>
													</td>
												</tr>
												<tr><td colspan="2" height="30" valign="top"></td></tr>
												<tr><td colspan="2" style="padding-left:35px;color:#CC0000"><%=passwordMsg%></td></tr>
												<tr>
													<td align="center">&nbsp;</td>
													<td align="right" style="padding-right:40px">
														<input type="button" class="buttonbg_gen t12 bold white" value="Submit" onclick="sendPassword()"/>
														<input type="button" class="buttonbg_gen t12 bold white" value="Back" onclick="window.location.href = '${pageContext.request.contextPath}/jsp/flash_login.jsp';"/>
													</td>
												</tr>
												<tr><td colspan="3" height="25"></td></tr>
											</table>
											</form>
										</td>
									</tr>
									<tr>
										<td colspan="3" style="color:#CC0000"><span id="msg"></span></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td colspan="2" align="right" height="30"></td></tr>
					</table>

					<jsp:include page="footer.jsp"/>
				</td>
			</tr>
		</table>
		<div id="footer" >
			<!--disclaimer-->
			<jsp:include page="disclaimer.jsp"/>
		</div>
	</body>
</html>
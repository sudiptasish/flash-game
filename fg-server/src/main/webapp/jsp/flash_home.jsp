<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String playerId = session.getAttribute("session.player") != null ? (String)session.getAttribute("session.player") : "";
%>
<html>
	<head>
		<title>Flash Home</title>
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		
		<!--base href="${pageContext.request.contextPath}"-->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/base.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/font.css" type="text/css">
		<style>
			.menuC {
				text-align: center;
				vertical-align: middle;
				font-size: 13px;
				font-weight: bold;
			}
			div#container table thead tr th {
				text-align:center;
				background:url('${pageContext.request.contextPath}/resources/images/cell_1.png') repeat-x;
				color:#FFFFFF;
			}
			div#container table tbody tr td {
				text-align:center;
				background-color:#ffff66;
				color:#000000;
			}
		</style>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/game.js"></script>
	</head>
	<body onload="loadTables('${pageContext.request.contextPath}', '<%=playerId%>')">
		<table class="LRborder" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td>						
					<!-- header -->
					<jsp:include page="header.jsp"/>
					<table width="900" border="0" align="center" cellpadding="0" cellspacing="0" class="mainbg">
						<tr>
							<td colspan="2" width="860" height="50" style="padding-left:40px"><h2>Welcome to Flash</h2></td>
						</tr>
						<tr><td colspan="2" align="center"><img src="${pageContext.request.contextPath}/resources/images/line.gif" width="860" height="2" /></td></tr>
						<tr>
							<td align="left" width="350" height="30" style="padding-left:35px; color:#CC0000">Current Time: [2016-12-29 13:36:32]</td>
							<td align="right" width="550" height="30" style="padding-right:35px;color:#CC0000">										
							<%
								if (playerId.length() == 0) {
							%>
								Hi. Guest. Please &nbsp;<a href="${pageContext.request.contextPath}/jsp/flash_login.jsp">login</a>&nbsp;&nbsp;or <a href="${pageContext.request.contextPath}/jsp/flash_registration.jsp">register</a>
							<%
								}
								else {
							%>
								Welcome <%=playerId%>&nbsp;&nbsp;&nbsp;|&nbsp;<a href="${pageContext.request.contextPath}/domain/view">My Profile</a>&nbsp;|&nbsp;<a href="${pageContext.request.contextPath}/domain/logout">Logout</a>&nbsp;|
							<%
								}
							%>
							</td>
						</tr>
						<tr><td colspan="2" align="right" height="30"><span id="session_msg">&nbsp;</span></td></tr>
						<tr>
							<td colspan="2" align="center">
								<table border="0" align="center" cellpadding="0" cellspacing="0">
									<tr>
										<td width="15" align="right"><img src="${pageContext.request.contextPath}/resources/images/titbgleft_gray.gif" width="15" height="20" /></td>
										<td width="122" align="center" class="titbg_gray white t13 bold">Home</td>
										<td width="703" align="left" class="titbgline_gray"><img src="${pageContext.request.contextPath}/resources/images/titbgright_gray.gif" /></td>
									</tr>
									<tr>
										<td colspan="3" class="contentbg_gray grayborder">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr><td colspan="2" height="30"></td></tr>
												<tr>
													<td colspan="2" style="padding-left:35px; color:#CC0000"><b></b></td>
												</tr>
												<tr><td colspan="2" height="20" align="right" style="padding-right:5px"><a href="javascript:createTable()">Create New Table</a></td></tr>
												<tr>
													<td width="140" align="center">
														&nbsp;
													</td>
													<td></td>
												</tr>
												<tr><td colspan="2" height="10" valign="top"></td></tr>
												<tr>
													<td colspan="2" align="center">
														<div id="newTable" style="position:relative">
															<div id="container" style="overflow-y:auto; height:300px">
															</div>
															<div id="tableBox" style="position:absolute;left:200px;top:50px;width:400px;height:230px;visibility:hidden;">
																<table border="0" width="100%" class="mainbg">
																	<tr><td colspan="2" height="20"></td></tr>
																	<tr>
																		<td align="left" style="padding-left:10px">* Table Name</td>
																		<td align="right" style="padding-right:10px">
																			<input name="tablename" id="tablename" maxlength="150" size="26" value="" style="width:200px; height:20px;"/>
																		</td>
																	</tr>
																	<tr><td colspan="2" height="10"></td></tr>
																	<tr>
																		<td align="left" style="padding-left:10px">* Description</td>
																		<td align="right" style="padding-right:10px">
																			<input name="tabledesc" id="tabledesc" maxlength="150" size="26" value="" style="width:200px; height:20px;"/>
																		</td>
																	</tr>
																	<tr><td colspan="2" height="10"></td></tr>
																	<tr>
																		<td align="left" style="padding-left:10px">* Max Capacity</td>
																		<td align="right" style="padding-right:10px">
																			<input name="capacity" id="capacity" maxlength="150" size="26" value="2" disabled style="width:200px; height:20px;"/>
																		</td>
																	</tr>
																	<tr><td colspan="2" height="10"></td></tr>
																	<tr>
																		<td align="left" style="padding-left:10px">* Table Type</td>
																		<td align="right" style="padding-right:10px">
																			<input name="tabletype" id="tabletype" maxlength="150" size="26" value="Permanent" disabled style="width:200px; height:20px;"/>
																		</td>
																	</tr>
																	<tr><td colspan="2" height="20"></td></tr>
																	<tr>
																		<td>&nbsp;</td>
																		<td align="right" style="padding-right:10px">
																			<input type="button" class="buttonbg_gen t12 bold white" value="Create" onclick="doTableCreate('${pageContext.request.contextPath}', '<%=playerId%>')"/>
																			<input type="button" class="buttonbg_gen t12 bold white" value="Close" onclick="document.getElementById('tableBox').style.visibility='hidden'"/>
																		</td>
																	</tr>
																</table>
															</div>
														</div>														
													</td>
												</tr>										
											</table>
										</td>
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
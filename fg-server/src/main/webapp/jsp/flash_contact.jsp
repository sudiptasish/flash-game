<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    String playerId = session.getAttribute("session.player") != null ? (String)session.getAttribute("session.player") : "";
%>
<html>
    <head>
        <title>Contact</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/base.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/font.css" type="text/css">
        <style>
            .menuC {
                    text-align: center;
                    vertical-align: middle;
                    font-size: 13px;
                    font-weight: bold;
            }
        </style>
        <script>
        </script>
    </head>
    <body>
        <table class="LRborder" border="0" cellpadding="0" cellspacing="0" align="center">
            <tr>
                <td>						
                    <!-- header -->
                    <jsp:include page="header.jsp"/>
                    <table width="900" border="0" align="center" cellpadding="0" cellspacing="0" class="mainbg">
                        <tr>
                            <td colspan="2" width="860" height="50" style="padding-left:40px"><h2>Welcome to Flash</h2></td>
                        </tr>
                        <tr><td align="center" colspan="2"><img src="${pageContext.request.contextPath}/resources/images/line.gif" width="860" height="2" /></td></tr>
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
                                        <td width="122" align="center" class="titbg_gray white t13 bold">Contact Us</td>
                                        <td width="703" align="left" class="titbgline_gray"><img src="${pageContext.request.contextPath}/resources/images/titbgright_gray.gif" /></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" class="contentbg_gray grayborder">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr><td colspan="2" height="30"></td></tr>
                                                <tr>
                                                        <td colspan="2" style="padding-left:35px; color:#CC0000"><b></b></td>
                                                </tr>
                                                <tr><td colspan="2" height="30" valign="top"></td></tr>
                                                <tr>
                                                        <td colspan="2" style="padding-left:35px; color:#CC0000">
                                                                Contact Us Content
                                                        </td>
                                                </tr>
                                                <tr><td colspan="2" height="30" valign="top"></td></tr>
                                                <tr><td colspan="2" height="100"></td></tr>												
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr><td colspan="2" align="right" height="30"></td></tr>
                    </table>

                    <!-- Login Fom -->
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
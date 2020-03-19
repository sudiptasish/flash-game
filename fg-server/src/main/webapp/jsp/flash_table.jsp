<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String playerId = session.getAttribute("session.player") != null ? (String)session.getAttribute("session.player") : "";
	String tableId = request.getAttribute("request.table") != null ? (String)request.getAttribute("request.table") : "";
%>
<html>
	<head>
		<title>Flash Game Table</title>
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />

		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/base.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/font.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/card.css" type="text/css">
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/msg.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bid_msg.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/card.js"></script>
        <script type="text/javascript">
			var client;

            function join() {
				var playerId = document.getElementById('playerId').value;
				var tableId = document.getElementById('tableId').value;

				client = new WebSocket('ws://localhost:8080/game/flash/table/' + tableId + '?playerId=' + playerId);
				client.onmessage = function(event) { onMessage(event) };
				client.onerror = function(event) { onError(event) };
            }
			function start() {
				var msg = new Message();
				msg.setType('START');
				msg.setCurrentPlayer(document.getElementById('playerId').value);
				client.send(JSON.stringify(msg));
			}
			function placeBid() {
				var bv = document.getElementById('bid');
				if (bv.value.trim() == '') {
					alert('You must enter a numeric value');
					return;
				}
				var msg = new BidMessage();
				msg.setGameId(document.getElementById('gameId').value);
				msg.setType('BID');
				msg.setStatus(0);
				msg.setBidValue(bv.value);
				msg.setCurrentPlayer(document.getElementById('playerId').value);
				msg.setOrder(0);
				
				client.send(JSON.stringify(msg));
			}
			function pack() {
				if (!confirm('Are you sure you want pack ?')) {
					return;
				}
				var msg = new BidMessage();
				msg.setGameId(document.getElementById('gameId').value);
				msg.setType('PACK');
				msg.setStatus(3);
				msg.setCurrentPlayer(document.getElementById('playerId').value);
				msg.setOrder(0);
				
				client.send(JSON.stringify(msg));
			}
			function show() {
				if (!confirm('Are you sure you want show ?')) {
					return;
				}
				var msg = new BidMessage();
				msg.setGameId(document.getElementById('gameId').value);
				msg.setType('SHOW');
				msg.setStatus(4);
				msg.setCurrentPlayer(document.getElementById('playerId').value);
				msg.setOrder(0);
				
				client.send(JSON.stringify(msg));
			}
            function onMessage(event) {
				var jsonObj = JSON.parse(event.data);
				if (jsonObj.type == 'ON_BOARD') {
					for (var i = 0; i < jsonObj.players.length; i ++) {
						var chairDiv = document.getElementById('chair_' + jsonObj.players[i].chairId);
						chairDiv.style.backgroundImage = "url('../resources/images/pholder.jpg')";
						chairDiv.innerHTML = '<br/><br/><br/><font color="#FFFFFF">' + jsonObj.players[i].id + '</font>';
					}
					if (jsonObj.players.length >= 2) {
						document.getElementById('table_msg').innerHTML = 'Ready to Start';
					}
				}
				else if (jsonObj.type == 'START') {
					document.getElementById('gameId').value = jsonObj.gameId;
					deal(jsonObj);
				}
				else if (jsonObj.type == 'BID') {
					var currentPlayerChairId = -1;
					for (var i = 0; i < jsonObj.players.length; i ++) {
						if (jsonObj.players[i].id == jsonObj.currentPlayer) {
							currentPlayerChairId = jsonObj.players[i].chairId;
						}
					}
					document.getElementById('bid_' + currentPlayerChairId).innerHTML = jsonObj.bidValue;
					document.getElementById('bid').value = '';
					if (document.getElementById('playerId').value == jsonObj.currentPlayer) {
						document.getElementById('bid').disabled = true;
					}
					else {
						document.getElementById('bid').disabled = false;
					}
				}
				else if (jsonObj.type == 'PACK') {
					var currentPlayerChairId = -1;
					var player = document.getElementById('playerId').value;
					for (var i = 0; i < jsonObj.players.length; i ++) {
						if (jsonObj.players[i].id == player) {
							currentPlayerChairId = jsonObj.players[i].chairId;
							break;
						}
					}
					var val = document.getElementById('bid_' + currentPlayerChairId).innerHTML;
					document.getElementById('bid_' + currentPlayerChairId).innerHTML = val + '<br/>' + 'Pack';

					document.getElementById('packLink').style.cursor = 'default';
					document.getElementById('packLink').removeAttribute('HREF');
				}
				else if (jsonObj.type == 'SHOW') {
					document.getElementById('table_msg').innerHTML = 'Player ' + jsonObj.currentPlayer + ' requested for a show';
					var msg = new BidMessage();
					msg.setGameId(document.getElementById('gameId').value);
					msg.setType('COMPLETE');
					msg.setStatus(5);
					msg.setOrder(0);
					
					client.send(JSON.stringify(msg));
				}
				else if (jsonObj.type == 'COMPLETE') {
					showAll(jsonObj);
				}
				else if (jsonObj.type == 'ERROR') {
					document.getElementById('game_msg').innerHTML = 'Player ' + jsonObj.currentPlayer + ' encountered error: ' + jsonObj.error;
				}
				else if (jsonObj.type == 'EXIT') {
					var chairDiv = document.getElementById('chair_' + jsonObj.exitChairId);
					chairDiv.style.backgroundImage = "url('../resources/images/chair_" + jsonObj.exitChairId + ".png')";
					document.getElementById('game_msg').innerHTML = 'Player ' + jsonObj.currentPlayer + ' exited';
				}
            }            
            function onError(event) {
				document.getElementById('game_msg').innerHTML = event.data;
			}
        </script>
    </head>
    <body onload="join()">
		<table width="900" border="0" align="center" cellpadding="0" cellspacing="0" class="mainbg">
			<tr>
				<td colspan="2" width="860" height="50" style="padding-left:40px"><h2>Welcome to Flash</h2></td>
			</tr>
			<tr><td colspan="2"><img src="${pageContext.request.contextPath}/resources/images/line.gif" width="850" height="2" /></td></tr>
			<tr>
				<td align="left" width="350" height="30" style="padding-left:35px; color:#CC0000">Current Time: [2016-12-29 13:36:32]</td>
				<td align="right" width="550" height="30" style="padding-right:35px;color:#CC0000">
					<%=playerId%>&nbsp;&nbsp;&nbsp;&nbsp;<a id="startLink" href="javascript:start()">Start Game</a>&nbsp;|&nbsp;<a href="javascript:window.close()">Exit</a>
				</td>
			</tr>
			<tr><td colspan="2" align="right" height="30"><span id="session_msg">&nbsp;</span></td></tr>
			<tr>
				<td colspan="2" align="center">
					<table border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td width="15" align="right"><img src="../resources/images/titbgleft_gray.gif" width="15" height="20" /></td>
							<td width="122" align="center" class="titbg_gray white t13 bold"><%=tableId%></td>
							<td width="703" align="left" class="titbgline_gray"><img src="../resources/images/titbgright_gray.gif" /></td>
						</tr>
						<tr>
							<td colspan="3" bgcolor="#000000">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>&nbsp;</td>
										<td align="center">
											<div style="position:relative">
												<div id="chair_0" style="width:100px; height:100px; background-image:url('../resources/images/chair_0.png'); background-size:100px 100px; no-repeat">
												</div>
											</div>
										</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td align="right">
											<div style="position:relative">
												<div id="chair_2" style="width:100px; height:100px; background-image:url('../resources/images/chair_2.png'); background-size:100px 100px; no-repeat">
												</div>
											</div>
										</td>
										<td width="640" height="320" style="background-image:url('../resources/images/table_small.png'); background-size:640px 320px;">
											<div style="position:relative; width:640px; height:320px">
												<div id="p_c_00" style="position:absolute;top:10px;left:250px;visibility:hidden">
												</div>
												<div id="p_c_01" style="position:absolute;top:10px;left:285px;visibility:hidden">
												</div>
												<div id="p_c_02" style="position:absolute;top:10px;left:320px;visibility:hidden">
												</div>
												<div id="bid_0" style="position:absolute;top:60px;left:420px;color:#FFFFFF;font-weight:bold"></div>

												<!--div id="table_msg" style="position:absolute;top:155px;left:280px;color:#FFFFFF;font-weight:bold;font-size:14px">Waiting for Players</div-->
												<div id="banner"><span id="table_msg">Waiting for Players</span></div>

												<div id="p_c_10" style="position:absolute;top:200px;left:250px;visibility:hidden">
												</div>
												<div id="p_c_11" style="position:absolute;top:200px;left:285px;visibility:hidden">
												</div>
												<div id="p_c_12" style="position:absolute;top:200px;left:320px;visibility:hidden">
												</div>
												<div id="bid_1" style="position:absolute;top:250px;left:420px;color:#FFFFFF;font-weight:bold"></div>
											</div>
										</td>
										<td>
											<div style="position:relative">
												<div id="chair_3" style="width:100px; height:100px; background-image:url('../resources/images/chair_3.png'); background-size:100px 100px; no-repeat">
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td align="center">
											<div style="position:relative">
												<div id="chair_1" style="width:100px; height:100px; background-image:url('../resources/images/chair_1.png'); background-size:100px 100px; no-repeat">
												</div>
											</div>
										</td>
										<td align="center">
											<input name="bid" id="bid" maxlength="90" size="26" value="" style="width:90px; height:20px;"/><br/><br/>
											<a id="bidLink"><font color="#FFFFFF">Bid</font></a>&nbsp;|&nbsp;
											<a id="showLink"><font color="#FFFFFF">Show</font></a>&nbsp;|&nbsp;
											<a id="packLink"><font color="#FFFFFF">Pack</font></a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right" height="30">
					<input type="hidden" id="playerId" value="<%=playerId%>"/>
					<input type="hidden" id="tableId" value="<%=tableId%>"/>
					<input type="hidden" id="gameId" value=""/>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2"><span id="game_msg" style="color:#CC0000">&nbsp;</span></td></tr>
		</table>
    </body>
</html>
var columns = ['Table Name', 'Max Capacity', 'Active Players', 'Status', 'Top Bid', 'Action'];

function getAjaxRequest() {
	var xmlHttp = null;
	try {
		xmlHttp = new XMLHttpRequest();
	}
	catch (e) {
		try {
			xmlHttp = new ActiveXObject('Msxml2.XMLHTTP');
		}
		catch (e) {
			try {
				xmlHttp = new ActiveXObject('Microsoft.XMLHTTP');
			}
			catch (e) {
				xmlHttp = window.createRequest();
			}
		}
	}
	return xmlHttp;
}
document.onkeypress = function (e) {
	var ae = window.event || e;
}
document.onclick = function (e) {
	notClosing = true;
}
window.onbeforeunload = function (e) {
	if (!notClosing) {
		destroySession();
	}
}
function loadTables(ctx, playerId) {
	if (playerId == '') {
		return;
	}
	var ajax = getAjaxRequest();
	ajax.onreadystatechange = function() {
		if (ajax.readyState == 4) {
			displayTable(ctx, playerId, ajax.responseText);
		}
	}
	ajax.open('GET', 'http://localhost:8080/table/tableMgmt/tables?playerId=' + playerId, true);
	ajax.send(null);
}
function displayTable(ctx, playerId, str) {
	var table = document.createElement('TABLE');
	table.setAttribute('border', '0');
	table.setAttribute('cellspacing', '2');
	table.setAttribute('cellpadding', '5');
	table.setAttribute('width', '100%');

	var thead = document.createElement('THEAD');
	createHeader(thead);

	var tbody = document.createElement('TBODY');
	tbody.setAttribute("id", "ft_body");
	createBody(ctx, playerId, tbody, str);

	table.appendChild(thead);
	table.appendChild(tbody);
	document.getElementById('container').appendChild(table);
}

function createHeader(thead) {
	var tr = document.createElement('TR');
	for (var i = 0; i < columns.length; i ++) {
		var th = document.createElement('TH');
		th.appendChild(document.createTextNode(columns[i]));
		tr.appendChild(th);
	}
	thead.appendChild(tr);
}

function createBody(ctx, playerId, tbody, str) {
	var jsonArr = JSON.parse(str);

	for (var i = 0; i < jsonArr.length; i ++) {
		var tr = document.createElement('TR');
		populateColumn(tr, jsonArr[i].name);
		populateColumn(tr, jsonArr[i].maxCapacity);
		populateColumn(tr, jsonArr[i].playerCount);
		populateColumn(tr, jsonArr[i].statusDesc);
		populateColumn(tr, '0');

		td = document.createElement('TD');
		var link = document.createElement('A');
		link.setAttribute('href', '#');
		var flag = jsonArr[i].maxCapacity == jsonArr[i].playerCount || jsonArr[i].frozen ? 1 : 0;
		link.setAttribute('onclick', 'validateAndOpen(\'' + ctx + '\',\'' + jsonArr[i].id + '\',\'' + playerId + '\',\'' + flag + '\')');
	
		var linkTxt = document.createTextNode(flag == 1 ? 'Watch' : 'Join');
		link.appendChild(linkTxt);
		td.appendChild(link);
		tr.appendChild(td);

		tbody.appendChild(tr);
	}
}
function populateColumn(tr, val) {
	td = document.createElement('TD');
	td.appendChild(document.createTextNode(val));
	tr.appendChild(td);
}
function validateAndOpen(ctx, tableId, playerId, flag) {
	if (flag == 1) {
		alert('A game has already been started for this room. Please try another room');
		return;
	}
	window.open(ctx + '/domain/table?tableId=' + tableId + '&playerId=' + playerId + '&action=' + flag, '_blank', 'toolbar=no,scrollbars=no,resizable=no,titlebar=no,status=no,top=40,left=300,width=950,height=750');
}
function destroySession() {
	var xmlHttp = getAjaxRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4) {
			alert('destroyed !!!');
		}
	}
	xmlHttp.open('POST', '${pageContext.request.contextPath}/domain/logout', true);
	xmlHttp.send(null);
}
function sendMsg() {
	isRefresh = false;
	var text = document.getElementById('usertext').value;
	if (text == '') {
		document.getElementById('txt_msg').innerHTML = 'Please Enter Text';
		return;
	}
	document.getElementById('usertext').value = '';
	document.getElementById('txt_msg').innerHTML = '';
	var xmlHttp = getAjaxRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4) {
			displayContent(xmlHttp.responseXML);
		}
	}
	xmlHttp.open('POST', '/ChatApp/chat/chat/userChat?roomname=<%=roomName%>&content=' + text, true);
	xmlHttp.send(null);
}

function createTable() {
	document.getElementById('tableBox').style.visibility = 'visible';
}
function doTableCreate(ctx, playerId) {
	var tableDesc = '{"name":"' + document.getElementById('tablename').value + '", "description":"' + document.getElementById('tablename').value + '",'
					+ '"maxCapacity":' + document.getElementById('capacity').value + ', "tableType":2}';

	var xmlHttp = getAjaxRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4) {
			updateTableList(ctx, playerId, xmlHttp.responseText);
		}
	}
	xmlHttp.open('POST', 'http://localhost:8080/table/tableMgmt/tables', true);
	xmlHttp.setRequestHeader("Content-type", "application/json");
	xmlHttp.send(tableDesc);
}
function updateTableList(ctx, playerId, str) {
	var jsonObj = JSON.parse(str);

	var tr = document.createElement('TR');
	populateColumn(tr, jsonObj.name);
	populateColumn(tr, jsonObj.maxCapacity);
	populateColumn(tr, jsonObj.playerCount);
	populateColumn(tr, jsonObj.statusDesc);
	populateColumn(tr, '0');

	td = document.createElement('TD');
	var link = document.createElement('A');
	link.setAttribute('href', '#');
	var flag = jsonObj.maxCapacity == jsonObj.playerCount || jsonObj.frozen ? 1 : 0;
	link.setAttribute('onclick', 'validateAndOpen(\'' + ctx + '\',\'' + jsonObj.id + '\',\'' + playerId + '\',\'' + flag + '\')');

	var linkTxt = document.createTextNode(flag == 1 ? 'Watch' : 'Join');
	link.appendChild(linkTxt);
	td.appendChild(link);
	tr.appendChild(td);

	document.getElementById('ft_body').appendChild(tr);
	document.getElementById('tablename').value = '';
	document.getElementById('description').value = '';
	document.getElementById('tableBox').style.visibility = 'hidden';
}
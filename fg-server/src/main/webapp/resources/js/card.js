function cardInfo(cards) {
	var ci = '[ ';
	if (cards != null) {
		for (var i = 0; i < cards.length; i ++) {
			ci += '(' + cards[i].suit + ' ' + cards[i].rank + ')  ';
		}
	}
	ci += ' ]';
	return ci;
}

function showAll(jsonObj) {
	for (var i = 0; i < jsonObj.allCards.length; i ++) {
		showCard(jsonObj.players[i].chairId, jsonObj.allCards[i]);
	}
	document.getElementById('table_msg').innerHTML = 'Player ' + jsonObj.currentPlayer + ' has won !';
}

function deal(jsonObj) {
	if (jsonObj.cards == null) {
		return;
	}
	var currentPlayerChairId = -1;
	var player = document.getElementById('playerId').value;

	for (var i = 0; i < jsonObj.players.length; i ++) {
		document.getElementById('bid_' + jsonObj.players[i].chairId).innerHTML = '';
		var chairDiv = document.getElementById('chair_' + jsonObj.players[i].chairId);
		chairDiv.style.backgroundImage = "url('../resources/images/pholder.jpg')";
		chairDiv.innerHTML = '<br/><br/><br/><font color="#FFFFFF">' + jsonObj.players[i].id + '</font>';
		
		if (jsonObj.players[i].id == player) {
			currentPlayerChairId = jsonObj.players[i].chairId;
			// We will display the card for current player, later
		}
		else {
			// Hide the cards for opponent.
			for (var j = 0; j < 3; j ++) {
				var container = document.getElementById('p_c_' + jsonObj.players[i].chairId + j);
				container.innerHTML = '';
				var cardDiv = document.createElement('DIV');
				cardDiv.setAttribute('class', 'card');
				container.appendChild(cardDiv);
				container.style.visibility = 'visible';
			}
		}
	}
	// Set the game as started:
	document.getElementById('table_msg').innerHTML = 'Game Started';
	// Made the players available.
	
	showCard(currentPlayerChairId, jsonObj.cards);
}

function showCard(chairId, cards) {
	for (var i = 0; i < cards.length; i ++) {
		var card = cards[i];
		var container = document.getElementById('p_c_' + chairId + i);
		container.innerHTML = '';
		var cardDiv = document.createElement('DIV');
		cardDiv.setAttribute('class', 'card');

		var suit = getSuit(cards[i]);
		var frontDiv = document.createElement('DIV');
		
		if (card.suit == 'HEART' || card.suit == 'DIAMOND') {
			frontDiv.setAttribute('class', 'front red');
		}
		else {
			frontDiv.setAttribute('class', 'front black');
		}
		
		var indexDiv = document.createElement('DIV');
		indexDiv.setAttribute('class', 'index');
		indexDiv.appendChild(document.createTextNode(getRank(card)));
		indexDiv.appendChild(document.createElement('BR'));
		indexDiv.appendChild(document.createTextNode(suit));
		frontDiv.appendChild(indexDiv);

		if (card.rankValue == 0) {	// 2
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_2");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_8");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 1) {	// 3
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_2");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_5");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_8");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 2) {	// 4
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_3");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_7");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_9");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 3) {	// 5
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_3");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_5");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_7");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_9");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 4) {	// 6
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_3");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_4");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_6");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_7");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_9");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 5) {	// 7
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_3");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_4");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_6");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_7");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_9");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_middle_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 6) {	// 8
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_3");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_4");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_6");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_7");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_9");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_middle_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_middle_2");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 7) {	// 9
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_left_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_left_2");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_7");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_middle_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_3");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_right_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_right_2");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_9");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 8) {	// 10
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_left_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_left_2");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_7");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_middle_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_middle_2");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_3");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_right_1");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_right_2");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);

			posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_regular_9");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 9) {	// J
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_jack");
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 10) {	// Q
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_queen");
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 11) {	// K
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "pos_king");
			frontDiv.appendChild(posDiv);
		}
		else if (card.rankValue == 12) {	// A
			var posDiv = document.createElement('DIV');
			posDiv.setAttribute("class", "ace");
			posDiv.appendChild(document.createTextNode(suit));
			frontDiv.appendChild(posDiv);
		}
		cardDiv.appendChild(frontDiv);
		container.appendChild(cardDiv);
		container.style.visibility = 'visible';
	}
	var startL = document.getElementById('startLink');
	startL.style.cursor = 'default';
	startL.removeAttribute('HREF');
	startL.innerHTML = 'Game Satrted';
	
	document.getElementById('bidLink').setAttribute('HREF', 'javascript:placeBid()');
	document.getElementById('showLink').setAttribute('HREF', 'javascript:show()');
	document.getElementById('packLink').setAttribute('HREF', 'javascript:pack()');
}
function getRank(card) {
	if (card.rankValue < 9) {
		return card.rankValue + 2;
	}
	if (card.rankValue == 9) {
		return 'J';
	}
	if (card.rankValue == 10) {
		return 'Q';
	}
	if (card.rankValue == 11) {
		return 'K';
	}
	if (card.rankValue == 12) {
		return 'A';
	}
}
function getSuit(card) {
	if (card.suit == 'CLUB') {
		return '\u2663';
	}
	if (card.suit == 'DIAMOND') {
		return '\u2666';
	}
	if (card.suit == 'HEART') {
		return '\u2665';
	}
	if (card.suit == 'SPADE') {
		return '\u2660';
	}
}
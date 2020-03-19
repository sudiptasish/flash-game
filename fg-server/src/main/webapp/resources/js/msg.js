function Message() {
}

Message.prototype.setGameId = function(gameId) {
	this.gameId = gameId;
}

Message.prototype.getGameId = function() {
	return this.gameId;
}

Message.prototype.setBid = function(bid) {
	this.bid = bid;
}

Message.prototype.getBid = function() {
	return this.bid;
}

Message.prototype.setType = function(type) {
	this.type = type;
}

Message.prototype.getType = function() {
	return this.type;
}

Message.prototype.setCurrentPlayer = function(currentPlayer) {
	this.currentPlayer = currentPlayer;
}

Message.prototype.getCurrentPlayer = function() {
	return this.currentPlayer;
}

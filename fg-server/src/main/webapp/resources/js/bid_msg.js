function BidMessage() {
}

BidMessage.prototype.setGameId = function(gameId) {
	this.gameId = gameId;
}

BidMessage.prototype.getGameId = function() {
	return this.gameId;
}

BidMessage.prototype.setOrder = function(order) {
	this.order = order;
}

BidMessage.prototype.getOrder = function() {
	return this.order;
}

BidMessage.prototype.setBidValue = function(bidValue) {
	this.bidValue = bidValue;
}

BidMessage.prototype.getBidValue = function() {
	return this.bidValue;
}

BidMessage.prototype.setCurrentPlayer = function(currentPlayer) {
	this.currentPlayer = currentPlayer;
}

BidMessage.prototype.getCurrentPlayer = function() {
	return this.currentPlayer;
}

BidMessage.prototype.setStatus = function(status) {
	this.status = status;
}

BidMessage.prototype.getStatus = function() {
	return this.status;
}

BidMessage.prototype.setType = function(type) {
	this.type = type;
}

BidMessage.prototype.getType = function() {
	return this.type;
}

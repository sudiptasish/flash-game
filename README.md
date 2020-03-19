## flash-game
3 card flash game - fun with **WebSocket**.

## Summary

Flash game, which is a micro service based architecture, has the following services:
1. Player Management Service (context root: **player**). It takes care of player creation/maintaining their state, etc.
1. Table Management Service (context root: **table**). It maintains the state of various game tables (board). At any point of time one can query this service to find out the status of a game table, whether a particular table is full/waiting for more players, etc. Whenever a player joins any table, it's state would be changed in this service.
1. Play Station Service (context root: **ps**). Once a game is initiated, the game information and ALL it's associated bids will be stored in this service. Whenever any player places a bid, the same will be updated in this service. At the end of a game all such info (with proper order) will be persisted into the db.
1. Game Server (context root: **game**). This is the entry point. Contains the servlet and other classes to login and maintain the dashboard. It also contains the web socket related classes that are required for communication between client (browser) and server. Request is first intercepted by the game server, which in turn invokes the REST API to call the appropriate service.

## Motivation

Back in 2007-2010, when we were building UI to display live market quotes, I used to wonder, instead of browser pulling the data, is there a way for **server** to **push** data to the **browser**. Developers used to rely on their very own **setInterval()** API to periodically make asynchronous call (AJAX) to backend server in order to **pull** the market data (bid, offer, greeks, volatility, etc) and display them on UI. This approach has multiple disadvantages:
* Even if there is no change in state, browser would still make a call.
* Server has to handle such calls, thus wasting it's Thread-CPU cycle.
* There is no way the server can communicate directly with the browser javascript engine.

We should also understand that **http** is a stateless protocol. Although it was invented for the World Wide Web, and has been used by browsers since then, it had limitations. Whenever, browser makes a request to download a file from server or pull response from server, it will create a new socket(port), transfer the data, and post that, the socket will be closed. Opening and closing socket has some overhead, especially where browser wants to display live streaming data, or need rapid response. Of late, the **Keep-Alive** header was introduced in order to allow the sender to hint about how the connection may be used to set a timeout and a maximum amount of requests.

But even with all this, it was still far from reality, as to how a backend server could directly interact with javascript. In 2011, **WebSocket** was standardized, thus opening a new era of javascript to server communication and vice versa.

In online game, the response from one player has to be rapidly transmitted to other players (sitting in different region) of the table. WebSocket helps us doing that. When a player places a bid, that event is quickly transferred to other players, so that they can act accordingly. The game server was written in late **2015**. I do not have a plan to commercialize it yet. 

## How to Setup

If you want to have some fun time, download the libraries, deploy them in the WebServer (e.g., Tomcat) that has support for **WebSocket**, configure the schema by running the table script present in the **db/script** directory. Right now I am using **Derby Database** (pure java db and easily portable). I might shift to MySql for better scalability. 

Happy Gaming !


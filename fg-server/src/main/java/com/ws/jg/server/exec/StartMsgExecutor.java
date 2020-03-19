package com.ws.jg.server.exec;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.ws.jg.common.client.GameMgmtClient;
import com.ws.jg.common.model.Card;
import com.ws.jg.common.model.Deck;
import com.ws.jg.common.model.FlashGame;
import com.ws.jg.common.model.GameDeck;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.UUIDGenerator;
import com.ws.jg.server.core.GameManager;
import com.ws.jg.server.session.PlayingSession;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class StartMsgExecutor extends MessageExecutor {

    /**
     * @param gameManager
     */
    public StartMsgExecutor(GameManager gameManager) {
        super(gameManager);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.exec.MessageExecutor#execute(com.ws.jg.server.exec.Packet)
     */
    @Override
    public void execute(Packet packet) {
        if (getGameManager().isStarted()) {
            Logger.log(String.format("Game already started"));
        }
        FlashGame game;
        synchronized(getGameManager()) {
            if (getGameManager().isStarted()) {
                return;
            }
            // At this stage, create a new game in the game store.
            game = new FlashGame(UUIDGenerator.generateUnique()
                    , "Flash Game"
                    , getGameManager().getTable().getId()
                    , getGameManager().getSessionHandler().sessionCount()
                    , new Date(System.currentTimeMillis())
                    , 0     // ACTIVE
                    , null);
            
            getGameManager().start(new Object());
        }
        
        // Signal received to start the game.
        // Freeze the table and send signal to all connected players about it.
        getGameManager().inGame(game.getId());
        
        // Now, distribute the cards from this deck.
        deal(game);
        
        // Publish the latest table status to the remote service.
        updateTableDefinition(getGameManager().getTable());
        
        // Invoke the remote service to store the game info, with
        // the details about card distribution amongst the player(s).
        GameMgmtClient.get().createGame(game);
                
        // Shuffle the card and send the cards to respective player(s).
        // Broadcast START game message.
        getGameManager().getSessionHandler().broadcastGameStart(game.getId(), packet.getGmSession());
    }
    
    /**
     * Get a new deck, shuffle it and distribute it to the player(s) on-board.
     * @param game
     */
    private void deal(FlashGame game) {
        // Now prepare the deck of cards and distribute them...
        Deck deck = Deck.newDeck();
        deck.shuffle();
        deck.print();
        
        int playerCount = getGameManager().getSessionHandler().sessionCount();
        int total = playerCount * 3;
        int index = 0;
        List<GameDeck> list = new ArrayList<>(total);
        
        for (PlayingSession gs : getGameManager().getSessionHandler().availableSessions()) {
            Card[] cards = new Card[3];    
            for (int i = 0; i < 3; i ++) {            
                cards[i] = deck.cardAt(index);                
                list.add(new GameDeck(
                            game.getId()
                            , gs.getPlayer().getId()
                            , cards[i].getSuit()
                            , cards[i].getRank()));
                
                index ++;
            }
            // Simple sort.
            sort(cards);
            gs.assign(cards);
        }
        game.setCards(list);
    }

    /**
     * Sort the cards based on their ranks and irrespective of their suits.
     * Sorting will be done in ascending order.
     * @param cards
     */
    private void sort(Card[] cards) {
        Arrays.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                if (c2.getRankValue() == c1.getRankValue()) {
                    return c2.getSuit().ordinal() - c1.getSuit().ordinal();
                }
                return c2.getRankValue() - c1.getRankValue();
            }
        });
        
        // Special handling for DUBLET.
        if (cards[0].getRank() != cards[1].getRank()
                && cards[1].getRank() == cards[2].getRank()) {
            
            Card temp = cards[0];
            cards[0] = cards[2];
            cards[2] = temp;
        }
    }
}

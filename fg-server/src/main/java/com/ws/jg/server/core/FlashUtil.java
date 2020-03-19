package com.ws.jg.server.core;

import java.util.List;

import com.ws.jg.common.model.Card;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class FlashUtil {

    /**
     * Choose the winning set of (3) cards out of the list.
     * It will return the correct index (from the list) of the winning card set.
     * 
     * @param cardList
     * @return int
     */
    public static int chooseWinner(List<Card[]> cardList) {
        FlashRank highest = FlashRank.HIGH_CARD;
        int highestIndex = -1;
        Card[] highestCards = null;
        
        for (int i = 0; i < cardList.size(); i ++) {
            Card[] cards = cardList.get(i);
            FlashRank currRank = decideRank(cards);
            
            if (currRank.ordinal() > highest.ordinal()) {
                highest = currRank;
                highestIndex = i;
                highestCards = cards;
            }
            else if (currRank.ordinal() == highest.ordinal()) {
                if (highestCards != null) {
                    if (cards[0].getRankValue() > highestCards[0].getRankValue()) {
                        highest = currRank;
                        highestIndex = i;
                        highestCards = cards;
                    }
                    else if (cards[0].getRankValue() == highestCards[0].getRankValue()) {
                        if (cards[0].getSuit().ordinal() > highestCards[0].getSuit().ordinal()) {
                            highest = currRank;
                            highestIndex = i;
                            highestCards = cards;
                        }
                    }
                }
                else {
                    highestCards = cards;
                }
            }
        }
        return highestIndex;
    }
    
    /**
     * Decide the rank of this set.
     * Note: The set of cards is already sorted in ascending order.
     * 
     * @param cards
     * @return FlashRank
     */
    public static FlashRank decideRank(Card[] cards) {
        // 1. Check if they are triplet.
        if (cards[0].getRank() == cards[1].getRank()
                && cards[0].getRank() == cards[2].getRank()) {
            return FlashRank.TRIPLET;
        }
        // 2. Check for pure/straight sequence
        else if (cards[0].getRankValue() - 1 == cards[1].getRankValue()
                && cards[1].getRankValue() - 1 == cards[2].getRankValue()) {
            
            if (cards[0].getSuit() == cards[1].getSuit() && cards[1].getSuit() == cards[2].getSuit()) {
                return FlashRank.PURE_SEQUENCE;
            }
            return FlashRank.STRAIGHT_SEQUENCE;
        }
        // 3. Check for color
        else if (cards[0].getSuit() == cards[1].getSuit() && cards[1].getSuit() == cards[2].getSuit()) {
            return FlashRank.COLOUR;
        }
        // 4. Check for dublet
        else if (cards[0].getRank() == cards[1].getRank() || cards[1].getRank() == cards[2].getRank()) {
            return FlashRank.DUBLET;
        }
        // 5. Otherwise highest card
        else {
            return FlashRank.HIGH_CARD;
        }
    }
}

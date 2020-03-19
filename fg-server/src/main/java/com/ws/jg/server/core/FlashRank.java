package com.ws.jg.server.core;

/**
 * 
 * @author Sudiptasish Chanda
 */
public enum FlashRank {
    
    HIGH_CARD ("Three cards that do not belong to any of the above types. Compare the highest card first," +
            " then the second highest, then the lowest. The best hand of this type is A-K-J of mixed suits," +
            " and the worst is 5-3-2."),
            
    DUBLET ("Two cards of the same rank. Between two such hands, compare the pair first, then the odd card" +
            " if these are equal. The highest pair hand is therefore A-A-K and the lowest is 2-2-3."),
            
    COLOUR ("Any three cards of the same suit. When comparing two colours, compare the highest card;" +
            " if these are equal compare the second; if these are equal too, compare the lowest. Thus" +
            " the highest colour is A-K-J and the lowest is 5-3-2."),
            
    STRAIGHT_SEQUENCE ("Three consecutive cards, not all of the same suit. A-2-3 is the best normal run" +
            ", then A-K-Q, K-Q-J and so on down to 4-3-2. 2-A-K is not valid."),
            
    PURE_SEQUENCE ("Three consecutive cards of the same suit. Ace can be used in the run A-2-3," +
            " which is the highest straight run. Next comes A-K-Q, K-Q-J and so on down to 4-3-2," +
            " which is the lowest. 2-A-K is not a valid run"),

    TRIPLET ("Three cards of the same rank. Three aces are the best trio and three twos are the lowest.");
    
    private final String description;
    
    private FlashRank(String description) {
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}

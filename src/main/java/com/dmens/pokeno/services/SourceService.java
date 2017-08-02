package com.dmens.pokeno.services;

import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.player.Player;

enum SourceTypes {
    DECK, DISCARD;
}

public class SourceService {
	private static SourceService service = new SourceService();
	
	private SourceService(){}

    public static SourceService getInstance(){
        return service;
    }
    
    public static void clearInstance(){
        service = new SourceService();
    }
    
    public CardContainer getCardContainerSourceFromPlayer(Player player, String source){
    	switch(SourceTypes.valueOf(source.toUpperCase())){
    	case DECK:
    		return player.getDeck();
		case DISCARD:
    		return player.getDiscards();
		default:
			return null;
    	}
    }
}

package com.dmens.pokeno.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.Deck;

/*
 * Parser object
 *
 * @author Jing
 * @author James
 */
public class DeckCreator {
    private static final Logger LOG = LogManager.getLogger(DeckCreator.class);
	private static DeckCreator instance = null;
	
	private DeckCreator() { }
	
	/*
	 * Get the instance (lazy instantiation).
	 * 
	 * @param		The instance of this object.
	 */
	public static DeckCreator Instance( ) {
		if(instance == null)
			instance = new DeckCreator();
		return instance;
	}
	
	public Deck DeckCreation(String deckLocation){		
		Deck deck = new Deck();
		FileUtils.getFileContentsAsList(deckLocation).forEach(line ->{
			deck.addCard(CardsDatabase.getInstance().query(Integer.parseInt(line)));
		});
		return deck;
	}
}

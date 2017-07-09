package com.dmens.pokeno.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.CardTypes;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.utils.CardParser;
import com.dmens.pokeno.utils.FileUtils;

public class CardsDatabase extends Database<Card>{
	private static Database<Card> database;
	private static String[] supportedPokemon = {"Electrike", "Froakie", "Electabuzz", "Machop", "Zubat", "Shellder", "Frogadier", "Machoke", "Electivire", "Espurr"};
	private static String[] supportedTrainer = {"Tierno"};
	
	private static final Logger LOG = LogManager.getLogger(CardsDatabase.class);
	
	public static Database<Card> getInstance(){
		if(database == null)
			database = new CardsDatabase();
		return database;
	}
	
	private CardsDatabase(){
		db = new ArrayList<Card>();
	}
	
	public void initialize(String cardsFilePath){
		List<String> list = FileUtils.getFileContentsAsList(cardsFilePath);
		list.stream().filter(line -> !line.isEmpty()).forEach(line -> {
			LOG.trace("Cards DB: "+line);
			db.add(CardParser.getCardFromString(line));
		});
	}
	
	@Override
	public Card query(int index) {
		Card card = super.query(index);
		if(card.isType(CardTypes.POKEMON) && !Arrays.asList(supportedPokemon).contains(card.getName())){
			return new EnergyCard("Colorless", "colorless");
		}else if(card.isType(CardTypes.TRAINER) && !Arrays.asList(supportedTrainer).contains(card.getName())){
			return new EnergyCard("Colorless", "colorless");
		}
		return card.copy();
	}
	
	public Card queryByName(String name){
		LOG.debug("Query Cards DB By name: "+name);
		
		Optional<Card> hit = db.stream().filter(card->card.getName().equals(name)).findAny();
		return hit.get();
	}
}

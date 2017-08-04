package com.dmens.pokeno.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.CardTypes;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.utils.CardParser;
import com.dmens.pokeno.utils.FileUtils;

public class CardsDatabase extends Database<Card>{
	private static Database<Card> database;

	private static String[] supportedPokemon = {
			"Glameow","Pikachu Libre","Pikachu","Raichu","Shellder",
			"Seaking","Goldeen","Frogadier","Froakie","Cloyster","Suicune",
			"Swanna","Ducklett","Purugly","Manectric","Electrike","Electivire",
			"Electabuzz","Helioptile","Clemont","Jynx","Jirachi",
			"Meowth","Machop","Doduo","Dodrio","Geodude","Zubat","Haunter","Gastly"
			,"Slowpoke","Hitmonlee","Hitmonchan","Machoke","Espurr","Persian"
			,"Diglett", "Dugtrio"//, "Meowstic"
	};

	private static String[] supportedTrainer = {
			"Tierno","Potion","Misty's Determination",
			"Pokémon Center Lady","Clemont","Poké Ball","Shauna",
			"Pokémon Fan Club","Switch","Energy Switch","Red Card"
			//,"Floral Crown", "Wally"
	};

	private static final Logger LOG = LogManager.getLogger(CardsDatabase.class);
	
	public static Database<Card> getInstance(){
		if(database == null)
			database = new CardsDatabase();
		return database;
	}
	
	public static void removeNullPointersInDB() {
		if(database == null)
			return;
		for(int i =0; i < database.db.size(); i++){
			if(database.db.get(i) == null)
				database.db.set(i, new Pokemon("null"));
		}
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
		LOG.debug(hit.get().getName()+ " found");
		return hit.get().copy();
	}
}

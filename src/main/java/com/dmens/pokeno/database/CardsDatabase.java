package com.dmens.pokeno.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.CardTypes;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.utils.CardUtil;
import com.dmens.pokeno.utils.FileUtils;

public class CardsDatabase extends Database<Card>{
	private static Database<Card> database;
	private static String[] supportedPokemon = {"Electrike", "Froakie", "Electabuzz", "Machop", "Zubat", "Shellder"};
	private static String[] supportedTrainer = {};
	
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
			db.add(CardUtil.getCardFromString(line));
		});
	}
	
	@Override
	public Card query(int index) {
		Card card = super.query(index);
		if(card.isType(CardTypes.POKEMON) && !Arrays.asList(supportedPokemon).contains(card.getName())){
			return new EnergyCard("Colorless", "colorless");
		}else if(card.isType(CardTypes.TRAINER) && !Arrays.asList(supportedTrainer).contains(index)){
			return new EnergyCard("Colorless", "colorless");
		}
		return card.copy();
	}
}

package com.dmens.pokeno.services;

import java.util.ArrayList;
import java.util.Stack;

import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.controller.GameController;

public class CountService {

	enum CountTypes {
	    YOUR_BENCH("your-bench"),
	    YOUR_ACTIVE("your-active"),
	    OPPONENT_ACTIVE("opponent-active"),
	    YOUR_HAND("your-hand"),
	    OPPONENT_HAND("opponent-hand"),
	    DAMAGE("damage"),
	    ENERGY("energy");

	    private CountTypes(String name){
	        this.name = name;
	    }

	    public String getName(){
	        return this.name;
	    }

	    public static CountTypes fromName(String type){
	        if(YOUR_BENCH.equals(type))
	            return YOUR_BENCH;
	        if(YOUR_ACTIVE.equals(type))
	            return YOUR_ACTIVE;
	        if(OPPONENT_ACTIVE.equals(type))
	            return OPPONENT_ACTIVE;
	        if(YOUR_HAND.equals(type))
	            return YOUR_HAND;
	        if(OPPONENT_HAND.equals(type))
	            return OPPONENT_HAND;
	        if(DAMAGE.equals(type))
	            return DAMAGE;
	        if(ENERGY.equals(type))
	            return ENERGY;
	        else
	            return null;
	    }

	    public boolean equals(String name){
	        return this.name.equals(name);
	    }
	    private String name;
	}
	
	private static CountService instance;
	    
	public static CountService getInstance(){
		  	
		if(instance == null) {
			instance = new CountService();
		}
	    return instance;
	}			
	
	public int getCount(String countInfo) {
		int count = 0;
		
		Stack<String> countInfoStack = new Stack<String>();
		String[] parsedString = countInfo.replaceAll("[()]", ":").split(":");
		for (int i = parsedString.length - 1; i >= 0; --i) {
			countInfoStack.add(parsedString[i]);
		}
		countInfoStack.pop(); //count
		countInfoStack.pop(); //target
		CountTypes type = CountTypes.fromName(countInfoStack.pop());

		if(type == CountTypes.YOUR_BENCH) {
			count = GameController.getActivePlayer().getBenchedPokemon().size();
		} else if(type == CountTypes.YOUR_ACTIVE) {
			type = CountTypes.fromName(countInfoStack.pop());
			if(type == CountTypes.DAMAGE) {
				count = GameController.getActivePlayer().getActivePokemon().getDamageCounter();
			} else if(type == CountTypes.ENERGY) {
				if(!countInfoStack.isEmpty()) {
				 	// count the number of a specific type of energy cards attached to the pokemon
					String energyCategory = countInfoStack.pop();
					ArrayList<EnergyCard> cards = GameController.getActivePlayer().getActivePokemon().getAttachedEnergy();
					for(EnergyCard energyCard: cards) {
						if(energyCard.getCategory() == EnergyTypes.valueOf(energyCategory.toUpperCase())) {
							++count;							
						}
					}
					
				} else {
					count = GameController.getActivePlayer().getActivePokemon().getAttachedEnergy().size();
				}
			}
		} else if(type == CountTypes.OPPONENT_ACTIVE) {
			type = CountTypes.fromName(countInfoStack.pop());
			if(type == CountTypes.DAMAGE) {
				count = GameController.getActivePlayer().getOpponent().getActivePokemon().getDamageCounter();
			} else if(type == CountTypes.ENERGY) {
				if(countInfoStack.peek() != null) {
				 	// count the number of a specific type of energy cards attached to the pokemon
					String energyCategory = countInfoStack.pop();
					ArrayList<EnergyCard> cards = GameController.getActivePlayer().getOpponent().getActivePokemon().getAttachedEnergy();
					for(EnergyCard energyCard: cards) {
						if(energyCard.getCategory() == EnergyTypes.valueOf(energyCategory.toUpperCase())) {
							++count;							
						}
					}
					
				} else {
					count = GameController.getActivePlayer().getOpponent().getActivePokemon().getAttachedEnergy().size();
				}
			}
		} else if(type == CountTypes.YOUR_HAND) {
			count = GameController.getActivePlayer().getHand().size();
		} else if(type == CountTypes.OPPONENT_HAND) {
			count = GameController.getActivePlayer().getOpponent().getHand().size();
		}
		
		return count;
	}
}

package com.dmens.pokeno.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.CardTypes;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;

public class Hand extends CardContainer {
	
	public boolean hasBasicPokemon(){
		for(Card card : cards)
            if(card.isType(CardTypes.POKEMON) && !((Pokemon)card).isEvolvedCategory())
                return true;
		return false;
	}
	
	public List<Card> getPokemon(){
		List<Card> pokemon = new ArrayList<Card>();
		cards.forEach(card->{
			if(card.isType(CardTypes.POKEMON)){
				pokemon.add(card);
			}
		});
		return pokemon;
	}
	
	
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	public Card getEnergyOfType(EnergyTypes energy){
		// Setting variable inside lambda
		AtomicReference<Card> energyInHand = new AtomicReference<>();
		cards.forEach(card ->{
			if(card.isType(CardTypes.ENERGY) && ((EnergyCard)card).isCategory(energy)){
				energyInHand.set(card);
			}
		});
		return energyInHand.get();
	}
}

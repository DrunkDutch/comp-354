package com.dmens.pokeno.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.dmens.pokeno.Card.Card;
import com.dmens.pokeno.Card.CardTypes;
import com.dmens.pokeno.Card.EnergyCard;
import com.dmens.pokeno.Card.EnergyTypes;
import com.dmens.pokeno.Card.Pokemon;

public class Hand extends CardContainer {
	
	public boolean hasBasicPokemon(){
		for(Card card : cards)
            if(card.isType(CardTypes.POKEMON) && ((Pokemon)card).getBasePokemonName().equals(card.getName()))
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

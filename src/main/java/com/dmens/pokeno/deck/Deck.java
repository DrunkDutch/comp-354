package com.dmens.pokeno.deck;

import java.util.ArrayList;
import java.util.Collections;

import com.dmens.pokeno.ability.AbilityCost;
import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Stack;

public class Deck extends CardContainer {
	
	public Deck(){
		super();
	}
	
	/**
	 * Draw a number n of cards from deck
	 * @param n
	 * @return array of cards
	 */
	public ArrayList<Card> draw(int n){
		ArrayList<Card> drawnCards = new ArrayList<Card>(n);
		for(int i = 0; i < n; i++){
			drawnCards.add(super.pickCard());
		}
		return drawnCards;
	}
	/**
	 * Draw exactly 1 card
	 * @return card
	 */
	public Card draw(){
		return super.pickCard();
	}
	
	public boolean checkValidity(){
		// deck should have exactly 60 cards
		if(size() != 60)
			return false;
		// deck should have at least one base pokemon card
		if(!deckHasPokemonCard()) 
			return false;
		if(deckHasMoreThanFourNoneEnergyCard())
			return false;
		if(!allCardsArePlayableInDeck())
			return false;
		return true;
	}
	
	private boolean deckHasPokemonCard() {
		for(int i = 0; i < size(); ++i) {
			Card card = cards.get(i);
			if(card instanceof Pokemon && !((Pokemon)card).isEvolvedCategory())
				return true;
		}
		return false;
	}
	
	private boolean deckHasMoreThanFourNoneEnergyCard() {
	     
		// TODO
		return false;
	}
	
	private boolean allCardsArePlayableInDeck() {
                Stack<Pokemon> evolvedPokemon = new Stack<Pokemon>();
                HashSet<Pokemon> pokemonInDeck = new HashSet<Pokemon>();
                HashSet<String> basePokemonInDeckNames = new HashSet<String>();
                Hashtable<EnergyTypes,Integer> energyCards = new Hashtable<EnergyTypes,Integer>();
                
                // iterate through the deck only once, then do each validation check
		for(int i = 0; i < size(); ++i) {
			Card card = cards.get(i);
                        if(card instanceof Pokemon)
                        {
                            pokemonInDeck.add((Pokemon)card);
                            
                            if(((Pokemon)card).isEvolvedCategory())
                                evolvedPokemon.add((Pokemon)card);
                            else
                                basePokemonInDeckNames.add(((Pokemon)card).getName().toLowerCase());
                        }
                        else if(card instanceof EnergyCard) {
                            EnergyTypes energyType = ((EnergyCard)card).getCategory();
                            Integer cardsOfSameType = energyCards.put(energyType, 1);
                            if(cardsOfSameType != null)
                                energyCards.put(energyType,cardsOfSameType.intValue()+1);
                        }
		}
                if(basePokemonInDeckNames.isEmpty())
                    return false;
                
                return basePokemonExistsForEachEvolvedPokemonInDeck(evolvedPokemon,basePokemonInDeckNames)
                        && eachPokemonHasAPlayableAbility(pokemonInDeck,energyCards);
	}
        
        private boolean basePokemonExistsForEachEvolvedPokemonInDeck(Stack<Pokemon> evolvedPokemon, HashSet<String> basePokemonInDeckNames) {
            // check each evolved Pokemon's base Pokemon is in the deck at least once
                while(!evolvedPokemon.isEmpty()) {
                    Pokemon evolution = evolvedPokemon.pop();
                    
                    if(!basePokemonInDeckNames.contains(evolution.getBasePokemonName().toLowerCase()))
                        return false; // evolved Pokemon was included in a deck not containing the base Pokemon
                }
                
                return true;
        }
        
        private boolean eachPokemonHasAPlayableAbility(HashSet<Pokemon> pokemonInDeck,Hashtable<EnergyTypes,Integer> energyCards) {
            // check there are enough energy cards for each Pokemon to potentially use an ability
                for(Pokemon pokemon : pokemonInDeck) {
                    ArrayList<AbilityCost> abilityCosts = pokemon.getAbilitiesAndCost();
                    
                    boolean foundPlayableAbility = false;
                    int abilityIndex = 0;
                    // try each ability until we find one that has its required energy in the deck
                    do {
                        boolean abilityIsAffordable = true;
                        
                        AbilityCost abilityCost = abilityCosts.get(abilityIndex);
                        
                        // verify each energy cost for this ability can be satisfied by cards in the deck
                        for(EnergyTypes energyType : abilityCost.getCosts().keySet()) {
                            if(!energyCards.containsKey(energyType) || abilityCost.getCosts().get(energyType) > energyCards.get(energyType)) {
                                // cost is greater than the number of this energy type's cards included in the deck
                                abilityIsAffordable = false;
                                break;
                            }
                        }
                        foundPlayableAbility = abilityIsAffordable;
                        // try next ability if current ability is too expensive for this deck
                    } while(!foundPlayableAbility && ++abilityIndex < abilityCosts.size());
                    
                    if(!foundPlayableAbility) // this Pokemon cannot be played with this deck build
                        return false;
                }
                
                return true;
        }
	
	public void shuffle(){
		Collections.shuffle(cards);
	}
}

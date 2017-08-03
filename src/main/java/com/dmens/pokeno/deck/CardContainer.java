package com.dmens.pokeno.deck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.CardTypes;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;

public class CardContainer {
	protected ArrayList<Card> cards;
	
	public CardContainer(){
		cards = new ArrayList<Card>();
	}
	
	/**
	 * pick first card
	 * @return
	 */
	public Card pickCard(){
		return pickCardFromPosition(0);
	}
	
	/**
	 * pick a card from position
	 * @param index
	 * @return
	 */
	public Card pickCardFromPosition(int index){
		Card card = cards.get(index);
		cards.remove(index);
		return card;
	}
	
	/**
	 * Add a card to the top of the container
	 * @param card
	 */
	public void addCard(Card c){
		cards.add(c);
	}
	
	/**
	 * Remove card from container
	 */
	public void removeCard(Card cardToRemove){
		cards.remove(cardToRemove);
	}
	
	/**
	 * Add multiple cards to container
	 * @param cardsToAdd
	 */
	public void addCards(Collection<? extends Card> cardsToAdd){
		cards.addAll(cardsToAdd);
	}
	
	public ArrayList<Card> dumpCards(){
		ArrayList<Card> dump = cards;
		cards = new ArrayList<Card>();
		return dump;
	}
	
	//This is a bit of a memory leak, but it isn't used anywhere where it could be changed (yet)
	public ArrayList<Card> getCards()
	{
		return cards;
	}
	
	public Card viewCardInPosition(int pos){
		return cards.get(pos);
	}
	
	public int size(){
		return cards.size();
	}
	
	public List<Card> getAllPokemon(){
		return cards.stream().filter(card -> card.isType(CardTypes.POKEMON)).collect(Collectors.toList());
	}
	
	public List<Card> getAllPokemonOfType(String type){
		return cards.stream().filter(card -> card.isType(CardTypes.POKEMON) && ((Pokemon)card).getCategory().equalsIgnoreCase(type)).collect(Collectors.toList());
	}
	
	public List<Card> getAllPokemonWithEnergy(){
		return cards.stream().filter(card -> card.isType(CardTypes.POKEMON) && ((Pokemon)card).getAttachedEnergy().size() > 0).collect(Collectors.toList());
	}
	
	public List<Card> getAllEnergy(){
		return cards.stream().filter(card -> card.isType(CardTypes.ENERGY)).collect(Collectors.toList());
	}
	
	public List<Card> getAllEnergyOfType(String type){
		return cards.stream().filter(card -> card.isType(CardTypes.ENERGY) && ((EnergyCard) card).isCategory(EnergyTypes.valueOf(type.toUpperCase()))).collect(Collectors.toList());
	}
	
	public List<Card> peekFromTop(int amount){
		return cards.subList(0, amount);
	}
	
	public List<Card> peekFromBottom(int amount){
		return cards.subList(amount, cards.size());
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		cards.forEach(card ->{
			sb.append(card.getType()+": "+card.getName()+"\n");
		});
		return sb.toString();
	}
}

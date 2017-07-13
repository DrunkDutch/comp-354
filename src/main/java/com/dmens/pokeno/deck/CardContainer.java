package com.dmens.pokeno.deck;

import java.util.ArrayList;
import java.util.Collection;

import com.dmens.pokeno.card.Card;

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
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		cards.forEach(card ->{
			sb.append(card.getType()+": "+card.getName()+"\n");
		});
		return sb.toString();
	}
}

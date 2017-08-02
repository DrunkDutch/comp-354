package com.dmens.pokeno.effectTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.effect.Search;

public class SearchTest {
	
	@Test
	public void testPokemonAllFilter(){
		CardContainer container = new CardContainer();
		Pokemon pikachu = new Pokemon("Pikachu");
		Pokemon raichu = new Pokemon("Raichu");
		container.addCard(pikachu);
		container.addCard(new EnergyCard("Colorless", "colorless"));
		container.addCard(raichu);
		Search searchEffect = new Search();
		searchEffect.setFilter("pokemon");
		List<Card> resultCards = new ArrayList<Card>();
		searchEffect.filterCards(container, resultCards);
		assertEquals(2, resultCards.size());
		assertEquals(pikachu, resultCards.get(0));
		assertEquals(raichu, resultCards.get(1));
	}
	
	@Test
	public void testPokemonWithTypeFilter(){
		CardContainer container = new CardContainer();
		Pokemon pikachu = new Pokemon("Pikachu");
		pikachu.setCategory("basic");
		Pokemon raichu = new Pokemon("Raichu");
		raichu.setCategory("stage-one");
		container.addCard(pikachu);
		container.addCard(new EnergyCard("Colorless", "colorless"));
		container.addCard(raichu);
		Search searchEffect = new Search();
		searchEffect.setFilter("pokemon:basic");
		List<Card> resultCards = new ArrayList<Card>();
		searchEffect.filterCards(container, resultCards);
		assertEquals(1, resultCards.size());
		assertEquals(pikachu, resultCards.get(0));
		searchEffect.setFilter("pokemon:stage-one");
		resultCards = new ArrayList<Card>();
		searchEffect.filterCards(container, resultCards);
		assertEquals(1, resultCards.size());
		assertEquals(raichu, resultCards.get(0));
	}
	
	@Test
	public void testEnergyAllFilter(){
		CardContainer container = new CardContainer();
		Pokemon pikachu = new Pokemon("Pikachu");
		Pokemon raichu = new Pokemon("Raichu");
		EnergyCard colorless = new EnergyCard("Colorless", "colorless");
		EnergyCard water = new EnergyCard("Water", "water");
		container.addCard(pikachu);
		container.addCard(colorless);
		container.addCard(raichu);
		container.addCard(water);
		Search searchEffect = new Search();
		searchEffect.setFilter("energy");
		List<Card> resultCards = new ArrayList<Card>();
		searchEffect.filterCards(container, resultCards);
		assertEquals(2, resultCards.size());
		assertEquals(colorless, resultCards.get(0));
		assertEquals(water, resultCards.get(1));
	}
	
	@Test
	public void testEnergyWithTypeFilter(){
		CardContainer container = new CardContainer();
		Pokemon pikachu = new Pokemon("Pikachu");
		Pokemon raichu = new Pokemon("Raichu");
		EnergyCard colorless = new EnergyCard("Colorless", "colorless");
		EnergyCard water = new EnergyCard("Water", "water");
		container.addCard(pikachu);
		container.addCard(colorless);
		container.addCard(raichu);
		container.addCard(water);
		Search searchEffect = new Search();
		searchEffect.setFilter("energy:water");
		List<Card> resultCards = new ArrayList<Card>();
		searchEffect.filterCards(container, resultCards);
		assertEquals(1, resultCards.size());
		assertEquals(water, resultCards.get(0));
	}

}

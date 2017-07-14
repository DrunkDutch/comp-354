package com.dmens.pokeno.PlayerTest;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.*;
import com.dmens.pokeno.ability.*;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.utils.Randomizer;
import com.dmens.pokeno.view.GameBoard;
import com.dmens.pokeno.utils.CardParser;
import com.dmens.pokeno.utils.AbilityParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class, Randomizer.class})
@PowerMockIgnore("javax.management.*")
public class AttackTest {

	private static final Logger LOG = LogManager.getLogger(Pokemon.class);
	
	private static Pokemon poke1;
	private static Pokemon poke2;
	
	private static String HitmonchanStr = "Hitmonchan:pokemon:cat:basic:cat:fight:90:retreat:cat:colorless:1:attacks:cat:colorless:2:61,cat:colorless:2,cat:fight:1:62";
	private static String PikachuStr = "Pikachu:pokemon:cat:basic:cat:lightning:60:retreat:cat:colorless:1:attacks:cat:colorless:1:5,cat:colorless:2:6";
	
	private static Ability abilityMultiFlip;
	private static Ability abilityParalyzed;
	private static Ability abilitySleep;
	private static Ability abilityStuck;
	private static Ability abilityPoisined;
	
	private static String abilityMultiFlipStr = "Bullet Punch:dam:target:opponent-active:20,cond:flip:dam:target:opponent-active:20,cond:flip:dam:target:opponent-active:20";
	private static String abilityParalyzedStr = "Nuzzle:cond:flip:applystat:status:paralyzed:opponent-active";
	
	static GameBoard mockBoard = Mockito.mock(GameBoard.class);
	
	@BeforeClass
	public static void setup(){
		AbilitiesDatabase.getInstance().initialize("abilities.txt");
		CardsDatabase.getInstance().initialize("cards.txt");
		
		GameController.setBoard(mockBoard);
		PowerMockito.mockStatic(GameController.class);
	}
	
	@BeforeClass
	public static void setupAbilities(){
		abilityParalyzed = AbilityParser.getAbilityFromString(abilityParalyzedStr);
	}
	
	@Before
	public void newPokemon(){
		poke1 = (Pokemon) CardParser.getCardFromString(HitmonchanStr);
		poke2 = (Pokemon) CardParser.getCardFromString(PikachuStr);
		Assert.assertEquals(poke1.getName(), "Hitmonchan");
		Assert.assertEquals(poke2.getName(), "Pikachu");
	}
	
	@Test 
	public void paralyzedAttack()
	{
		AbilityCost abcP = new AbilityCost(abilityParalyzed);
		abcP.addCost(EnergyTypes.COLORLESS, 1);
		
		Randomizer rand = Mockito.mock(Randomizer.class);
		Mockito.when(rand.getFiftyPercentChance()).thenReturn(true);
		
		poke1.AddAbilityAndCost(abcP);
		poke1.addEnergy(new EnergyCard("Colorless", "colorless"));
		Assert.assertEquals(poke1.useAbility(0, poke2), true);
		Assert.assertEquals(poke2.isParalyzed(), true);
	}
}

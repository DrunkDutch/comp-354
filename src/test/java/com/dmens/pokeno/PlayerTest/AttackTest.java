package com.dmens.pokeno.PlayerTest;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
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

public class AttackTest {

	private static final Logger LOG = LogManager.getLogger(Pokemon.class);
	
	private static String HitmonchanStr = "Hitmonchan:pokemon:cat:basic:cat:fight:90:retreat:cat:colorless:1:attacks:cat:colorless:2:61,cat:colorless:2,cat:fight:1:62";
	private static String PikachuStr = "Pikachu:pokemon:cat:basic:cat:lightning:60:retreat:cat:colorless:1:attacks:cat:colorless:1:5,cat:colorless:2:6";
	private static String EspurrStr = "Espurr:pokemon:cat:basic:cat:psychic:50:retreat:cat:colorless:1:attacks:cat:colorless:1:64";
	private static String JynxStr = "Jynx:pokemon:cat:basic:cat:psychic:70:retreat:cat:colorless:1:attacks:cat:colorless:2,cat:psychic:1:38";

	
	public static GameBoard mockBoard;

	static Robot robot;
	public Robot okRobot;


	@BeforeClass
	public static void setup(){
		AbilitiesDatabase.getInstance().initialize("abilities.txt");
		CardsDatabase.getInstance().initialize("cards.txt");

		mockBoard = new GameBoard();

		GameController.setBoard(mockBoard);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test 
	public void paralyzedAttack()
	{
		Pokemon poke1 = Mockito.spy((Pokemon) CardParser.getCardFromString(HitmonchanStr));
		Pokemon poke2 = Mockito.spy((Pokemon) CardParser.getCardFromString(PikachuStr));
		Mockito.doNothing().when(poke1).displayMessage(Mockito.anyString());
		Mockito.doNothing().when(poke2).displayMessage(Mockito.anyString());
		Assert.assertEquals(poke1.getName(), "Hitmonchan");
		Assert.assertEquals(poke2.getName(), "Pikachu");

		poke2.addEnergy(new EnergyCard("Colorless", "colorless"));
		poke2.useAbility(0, poke1);

		Assert.assertEquals(true, poke1.isParalyzed());
	}
	
	@Test 
	public void asleepAttack()
	{
		Pokemon poke1 = Mockito.spy((Pokemon) CardParser.getCardFromString(HitmonchanStr));
		Pokemon poke2 = Mockito.spy((Pokemon) CardParser.getCardFromString(EspurrStr));
		Mockito.doNothing().when(poke1).displayMessage(Mockito.anyString());
		Mockito.doNothing().when(poke2).displayMessage(Mockito.anyString());
		Assert.assertEquals(poke1.getName(), "Hitmonchan");
		Assert.assertEquals(poke2.getName(), "Espurr");

		poke2.addEnergy(new EnergyCard("Colorless", "colorless"));
		poke2.useAbility(0, poke1);

		Assert.assertEquals(true, poke1.isSleep());
	}
	
	@Test 
	public void stuckAttack()
	{
		Pokemon poke1 = Mockito.spy((Pokemon) CardParser.getCardFromString(HitmonchanStr));
		Pokemon poke2 = Mockito.spy((Pokemon) CardParser.getCardFromString(JynxStr));
		Mockito.doNothing().when(poke1).displayMessage(Mockito.anyString());
		Mockito.doNothing().when(poke2).displayMessage(Mockito.anyString());
		Assert.assertEquals(poke1.getName(), "Hitmonchan");
		Assert.assertEquals(poke2.getName(), "Jynx");

		poke2.addEnergy(new EnergyCard("Colorless", "colorless"));
		poke2.addEnergy(new EnergyCard("Colorless", "colorless"));
		poke2.addEnergy(new EnergyCard("Psychic", "psychic"));
		poke2.useAbility(0, poke1);

		Assert.assertEquals(true, poke1.isStuck());
	}
	

}

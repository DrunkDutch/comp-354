package com.dmens.pokeno.integration;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.TargetService;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import com.dmens.pokeno.utils.CardParser;
import com.dmens.pokeno.view.GameBoard;

public class AttackTestIT {

	private static final Logger LOG = LogManager.getLogger(Pokemon.class);
	
	private static String HitmonchanStr = "Hitmonchan:pokemon:cat:basic:cat:fight:90:retreat:cat:colorless:1:attacks:cat:colorless:2:61,cat:colorless:2,cat:fight:1:62";
	private static String PikachuStr = "Pikachu:pokemon:cat:basic:cat:lightning:60:retreat:cat:colorless:1:attacks:cat:colorless:1:5,cat:colorless:2:6";
	private static String EspurrStr = "Espurr:pokemon:cat:basic:cat:psychic:50:retreat:cat:colorless:1:attacks:cat:colorless:1:64";
	private static String JynxStr = "Jynx:pokemon:cat:basic:cat:psychic:70:retreat:cat:colorless:1:attacks:cat:colorless:2,cat:psychic:1:38";

	
	public static GameBoard mockBoard;

	static Robot robot;
	public Robot okRobot;
	static Player p1;
	static Player p2;

	@BeforeClass
	public static void setup(){
		AbilitiesDatabase.getInstance().initialize("abilities.txt");
		CardsDatabase.getInstance().initialize("cards.txt");
		
		p1 = Mockito.spy(new Player());
		p2 = Mockito.spy(new Player());
		TargetServiceHandler.getInstance().setYouPlayer(p1);
		TargetServiceHandler.getInstance().setThemPlayer(p2);
		Mockito.doReturn(true).when(p1).flipCoin();
		Mockito.doNothing().when(p1).displayMessage(Mockito.anyString());

		mockBoard = new GameBoard();

		GameController.setBoard(mockBoard);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void paralyzedAttack()
	{
		Pokemon poke1 = Mockito.spy((Pokemon) CardParser.getCardFromString(HitmonchanStr));
		Pokemon poke2 = Mockito.spy((Pokemon) CardParser.getCardFromString(PikachuStr));
		p1.setActivePokemon(poke2);
		p2.setActivePokemon(poke1);
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
		p1.setActivePokemon(poke2);
		p2.setActivePokemon(poke1);
		Assert.assertEquals(poke1.getName(), "Hitmonchan");
		Assert.assertEquals(poke2.getName(), "Espurr");

		poke2.addEnergy(new EnergyCard("Colorless", "colorless"));
		poke2.useAbility(0, poke1);

		Assert.assertEquals(true, poke1.isSleep());
	}

	@Test 
	public void stuckAttack() {
		Pokemon poke1 = Mockito.spy((Pokemon) CardParser.getCardFromString(HitmonchanStr));
		Pokemon poke2 = Mockito.spy((Pokemon) CardParser.getCardFromString(JynxStr));
		p1.setActivePokemon(poke2);
		p2.setActivePokemon(poke1);
		Assert.assertEquals(poke1.getName(), "Hitmonchan");
		Assert.assertEquals(poke2.getName(), "Jynx");
		poke2.addEnergy(new EnergyCard("Colorless", "colorless"));
		poke2.addEnergy(new EnergyCard("Colorless", "colorless"));
		poke2.addEnergy(new EnergyCard("Psychic", "psychic"));
		poke2.useAbility(0, poke1);
		TargetService.clearInstance();
		TargetServiceHandler.clearInstance();
		Assert.assertEquals(true, poke1.isStuck());
	}
}

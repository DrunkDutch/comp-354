package com.dmens.pokeno.effect;

import java.util.ArrayList;
import java.util.List;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.condition.Condition;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.TargetService;

/**
 * 	reenergize:[target:source]:[target:destination]:[amount]
 *	move energy from one pokemon to another, [amount] times
 * @author James
 *
 */
public class Reenergize extends Effect {

	private int mAmount;
	private String mSource;
	private String mDestination;
	
	/*
	 * Constructor
	 * 
	 * @param		target			String.
	 * @param		destination		String.
	 */
	public Reenergize(int amt, String src, String dst, Condition con) {
		super("", con);
		this.mAmount = amt;
		this.mSource = src;
		this.mDestination = dst;
	}
	
	public int getAmount() {
		return this.mAmount;
	}

	@Override
	public void execute() {
		
		// 1) Choose a pokemon as the source
		Pokemon pokeSrc = null;
		ArrayList<EnergyTypes> energies = new ArrayList<EnergyTypes>();
		Player player = TargetService.getInstance().getPlayer(mSource);
		List<Card> potentialSources = player.getHand().getAllPokemonWithEnergy();
		
		// 2) Choose and remove the n energy
		for (int i = 0; i < mAmount; i++) {
			if(pokeSrc.getAttachedEnergy().size() == 0)
				GameController.displayMessage(pokeSrc.getName() + " has no (more) energy to remove!");
			
			EnergyTypes type = player.createEnergyOptionPane(player.getActivePokemon(), "Remove an Energy from " + pokeSrc.getName(), "Which energy would you like to remove?", false);	
			System.out.println("Removed: " + pokeSrc.removeSingleEnergy(type));
			energies.add(type);
		}
		
		// 3) Choose the destination pokemon
		Pokemon pokeDest = null;
		List<Card> potentialDestinations = player.getHand().getAllPokemon();
		
		// 4) Add n energies to the destination
		for (int i = 0; i < energies.size(); i++) {
			EnergyTypes e = energies.get(i);
			
			switch(e) {
				case COLORLESS: pokeDest.addEnergy(new EnergyCard("Colorless", "colorless"));
					break;
				case WATER:	pokeDest.addEnergy(new EnergyCard("Water", "water"));
					break;
				case PSYCHIC: pokeDest.addEnergy(new EnergyCard("Psychic", "psychic"));
					break;
				case LIGHTNING: pokeDest.addEnergy(new EnergyCard("Lightning", "lightning"));
					break;
				case FIGHT: pokeDest.addEnergy(new EnergyCard("Fight", "fight"));
					break;
				default:
					break;
			}
		}

	}

	@Override
	public String toString() {
		return String.format("Reenergize: Src: %s, Dst: %s, Amt: %d", this.mSource, this.mDestination, this.mAmount);
	}
}

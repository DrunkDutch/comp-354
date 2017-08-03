package com.dmens.pokeno.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.CardTypes;
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

	private static final Logger LOG = LogManager.getLogger(Reenergize.class);
	
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
		ArrayList<EnergyTypes> energies = new ArrayList<EnergyTypes>();
		Player player = TargetService.getInstance().getPlayer(mSource);
		
		List<Pokemon> potentialSources =  new ArrayList<Pokemon>(player.getBenchedPokemon().stream().filter(card -> ((Pokemon)card).getAttachedEnergy().size() > 0).collect(Collectors.toList()));
		if(player.getActivePokemon().getAttachedEnergy().size() > 0) {
			potentialSources.add(player.getActivePokemon());
		}
		
		if(potentialSources.size() == 0) {
			player.displayMessage("No pokemon had energy!");
			LOG.info("No pokemon had energy!");
			return;
		}
			
		int iS = player.createPokemonOptionPane("Source selection", "These pokemon have energy:", false, potentialSources);
		Pokemon pokeSrc = (Pokemon) potentialSources.get(iS);
		
		// 2) Choose and remove the n energy
		for (int i = 0; i < mAmount; i++) {
			if(pokeSrc.getAttachedEnergy().size() == 0) {
				GameController.displayMessage(pokeSrc.getName() + " has no (more) energy to remove!");
				break;
			}
			
			EnergyTypes type = player.createEnergyOptionPane(pokeSrc, "Remove an Energy from " + pokeSrc.getName(), "Which energy would you like to remove?", false);	
			pokeSrc.removeSingleEnergy(type);
			LOG.info("Removed " + type.toString() + " from " + pokeSrc.getName());
			energies.add(type);
		}
		
		// 3) Choose the destination pokemon
		List<Pokemon> potentialDestinations =  new ArrayList<Pokemon>(player.getBenchedPokemon());
		potentialDestinations.add(player.getActivePokemon());
		int iD = player.createPokemonOptionPane("Destination selection", "Energies will be added to:", false, potentialDestinations);
		Pokemon pokeDest = (Pokemon) potentialSources.get(iD);
		
		// 4) Add n energies to the destination
		for (int i = 0; i < energies.size(); i++) {
			EnergyTypes e = energies.get(i);
			LOG.info("Adding " + e.toString() + " to " + pokeDest.getName());
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
		player.updateActivePokemonOnBoard();
	}

	@Override
	public String toString() {
		return String.format("Reenergize: Src: %s, Dst: %s, Amt: %d", this.mSource, this.mDestination, this.mAmount);
	}
}

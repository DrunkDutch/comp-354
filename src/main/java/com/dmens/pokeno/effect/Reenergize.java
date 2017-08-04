package com.dmens.pokeno.effect;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

/**
 * 	reenergize:[target:source]:[target:destination]:[amount]
 *	move energy from one pokemon to another, [amount] times
 * @author James
 *
 */
public class Reenergize extends Effect {

	private static final Logger LOG = LogManager.getLogger(Reenergize.class);
	
	private EffectAmount mAmount;
	private String mSource;
	private String mDestination;
	
	/*
	 * Constructor
	 * 
	 * @param		target			String.
	 * @param		destination		String.
	 */
	public Reenergize(String amt, String src, String dst) {
		super("");
		this.mAmount = new EffectAmount(amt);
		this.mSource = src;
		this.mDestination = dst;
	}
	
	public int getAmount() {
		return this.mAmount.eval();
	}

	@Override
	public void execute() {
		Pokemon sourcePokemon = (Pokemon) TargetServiceHandler.getInstance().getTarget(mSource).get(0);
		Player currentPlayer = TargetServiceHandler.getInstance().getPlayingPlayer();
		List<EnergyCard> energies = sourcePokemon.getAttachedEnergy();
		if(energies.isEmpty()){
			currentPlayer.displayMessage("Pokemon has no energy");
			return;
		}
		List<EnergyCard> cardsChosen = currentPlayer.ChooseMultipleCards(sourcePokemon.getAttachedEnergy(), mAmount.eval());
		Pokemon destination = (Pokemon) TargetServiceHandler.getInstance().getTarget(mDestination).get(0);
		sourcePokemon.removeEnergy(cardsChosen);
		cardsChosen.forEach(energy ->destination.addEnergy(energy));
		currentPlayer.updateBoard();
	}

	@Override
	public String toString() {
		return String.format("Reenergize: Src: %s, Dst: %s, Amt: %s", this.mSource, this.mDestination, this.mAmount);
	}
	
	@Override
	public String str() {
		return String.format("REE %s", this.mAmount);
	}
}

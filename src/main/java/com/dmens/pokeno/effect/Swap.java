package com.dmens.pokeno.effect;

import java.util.List;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

public class Swap extends Effect {

	private String mDestination;
	
	/*
	 * Constructor
	 * 
	 * @param		target			String.
	 * @param		destination		String.
	 */
	public Swap(String target, String destination)
	{
		super(target, null);
		this.mDestination = destination;		
	}
	
	public String getDestination() {
		return this.mDestination;
	}
	
	@Override
	public void execute() {
		List<Card> targetPokemonList = (TargetServiceHandler.getInstance()).getTarget(mTarget);
 		assert targetPokemonList.size() == 1;
		Pokemon targetPokemon = (Pokemon)targetPokemonList.get(0);
		
		List<Card> destinationPokemonList = (TargetServiceHandler.getInstance()).getTarget(mDestination);
		assert targetPokemonList.size() == 1;
		Pokemon destinationPokemon = (Pokemon)destinationPokemonList.get(0);

 		if(mTarget.contains("active")) {
			// put targetPokemon to bench and destinationPokemon to active
			GameController.getActivePlayer().benchPokemon(targetPokemon);
			GameController.getActivePlayer().setActivePokemon(destinationPokemon);
			GameController.getActivePlayer().getBenchedPokemon().remove(destinationPokemon);
		} else {
			// put destinationPokemon to bench and targetPokemon to active
			GameController.getActivePlayer().benchPokemon(destinationPokemon);
			GameController.getActivePlayer().setActivePokemon(targetPokemon);
			GameController.getActivePlayer().getBenchedPokemon().remove(targetPokemon);
		}
	}

	@Override
	public String toString() {
		return String.format("SWAP: Target: %s, Destination: %s", this.mTarget, this.mDestination);
	}

	@Override
	public boolean equals(Object obj)
	{
		Swap d = (Swap) obj;
		if(d.mTarget.equals(this.mTarget) && d.mDestination == this.mDestination)
			return true;
		
		return false;
	}
}

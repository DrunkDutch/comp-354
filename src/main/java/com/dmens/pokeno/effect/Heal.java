package com.dmens.pokeno.effect;

import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.*;
import com.dmens.pokeno.card.Pokemon;

/*
 * A Heal effect.
 *
 * @author James
 */
public class Heal implements Effect {

	private int mValue;
	private String mTarget;
	
	// we have three possible targets to heal
	private final String YOUR_ACTIVE = "your-active";
	private final String YOUR = "your";
	private final String SELF = "self";

	/*
	 * Constructor
	 * 
	 * @param		tar		Target.
	 * @param		val		Integer value (amount).
	 */
	public Heal(String tar, int val)
	{
		this.mTarget = tar;
		this.mValue = val;		
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		h		Heal Effect.
	 */
	public Heal(Heal h)
	{
		this.mTarget = h.mTarget;
		this.mValue = h.mValue;		
	}
	
	/*
     * Get the target of this Effect.
     * 
     * @return		The target as a string.
     */
	public String getTarget()
	{
		return this.mTarget;
	}
	
	/*
     * Get the value of this Effect.
     * 
     * @return		The value as an integer.
     */
	public int getValue()
	{
		return this.mValue;
	}

	@Override
	public void execute()
	{
		if(mTarget.equals(YOUR_ACTIVE)) {
			// heal the player's active pokement
			if(GameController.getIsHomePlayerPlaying()) {
				GameController.getHomePlayer().getActivePokemon().removeDamage(this.mValue);
			} else {
				GameController.getAIPlayer().getActivePokemon().removeDamage(this.mValue);
			}
		} else if (mTarget.equals(YOUR)) {
			if(GameController.getIsHomePlayerPlaying()) {
				Player homePlayer= GameController.getHomePlayer();
				int pokemonIndex = homePlayer.createPokemonOptionPane("Heal", "Which Pokemon would you like to heal?", false);
;				Pokemon pokemonToHeal = null;
				if(homePlayer.getActivePokemon() != null) {
					if(pokemonIndex == 0) {
						pokemonToHeal = homePlayer.getActivePokemon();
					} else {
						pokemonToHeal = homePlayer.getBenchedPokemon().get(pokemonIndex - 1);
					}
				} else {
					pokemonToHeal = homePlayer.getBenchedPokemon().get(pokemonIndex);
				}
				pokemonToHeal.removeDamage(this.mValue);
			} else {
				// AI heals a damaged pokemon
				AIPlayer ai = (AIPlayer)(GameController.getAIPlayer());
				Pokemon pokemonToHeal = ai.getDamangedPokemon();
				if(pokemonToHeal != null) {
					pokemonToHeal.removeDamage(this.mValue);
				}
			}
		} else if(mTarget.equals(SELF)) {
			// TODO: heal the pokemon this card is attached to, specific to Floral Crown
		}

	}

	@Override
	public String toString()
	{
		return String.format("HEAL: Target: %s, Value: %d", this.mTarget, this.mValue);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Heal h = (Heal) obj;
		if(h.mTarget.equals(this.mTarget) && h.mValue == this.mValue)
			return true;
		
		return false;
	}
}

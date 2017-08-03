package com.dmens.pokeno.effect;

import java.util.List;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

/*
 * A Heal effect.
 *
 * @author James
 */
public class Heal extends Effect {

	private int mValue;
	
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
		super(tar);
		this.mValue = val;		
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		h		Heal Effect.
	 */
	public Heal(Heal h)
	{
		this.mTarget = h.getTarget();
		this.mValue = h.mValue;		
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
		List<Card> pokemonToHeal = TargetServiceHandler.getInstance().getTarget(mTarget);
		pokemonToHeal.forEach(poke -> ((Pokemon)poke).removeDamage(this.mValue));
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

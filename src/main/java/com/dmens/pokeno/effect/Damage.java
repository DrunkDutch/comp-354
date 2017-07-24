package com.dmens.pokeno.effect;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.condition.*;
import com.dmens.pokeno.services.TargetService;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

import java.util.List;

/*
 * A Damage effect.
 *
 * @author James
 */
public class Damage implements Effect {

	private int mValue;
	private String mTarget;
	private Condition mCondition = null;
	
	/*
	 * Constructor
	 * 
	 * @param		tar		Target.
	 * @param		val		Integer value (amount).
	 * @param		con		Condition.
	 */
	public Damage(String tar, int val, Condition con)
	{
		this.mTarget = tar;
		this.mValue = val;	
		this.mCondition = con;
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		d		Damage Effect.
	 */
	public Damage(Damage d)
	{
		this.mTarget = d.mTarget;
		this.mValue = d.mValue;
		
		if(d.mCondition instanceof Flip)
			this.mCondition = new Flip();
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
		List<Card> targetPokemon = (TargetServiceHandler.getInstance()).getTarget(mTarget);
		targetPokemon.forEach(pokemon -> ((Pokemon) pokemon).addDamage(mValue));
	}

	@Override
	public String toString()
	{
		return String.format("Damage: Target: %s, Value: %d", this.mTarget, this.mValue);
	}
	
	//TODO: condition check
	@Override
	public boolean equals(Object obj)
	{
		Damage d = (Damage) obj;
		if(d.mTarget.equals(this.mTarget) && d.mValue == this.mValue)
			return true;
		
		return false;
	}
	
	@Override
	public boolean hasCondition()
	{
		return (mCondition != null);
	}
	
	@Override
	public Condition getCondition()
	{
		return mCondition;
	}
}
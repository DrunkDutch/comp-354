package com.dmens.pokeno.effect;

import java.util.List;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

/*
 * A Damage effect.
 *
 * @author James
 */
public class Damage extends Effect {

	private EffectAmount mValue;
	
	/*
	 * Constructor
	 * 
	 * @param		tar		Target.
	 * @param		val		Integer value (amount).
	 * @param		con		Condition.
	 */
	public Damage(String tar, String val)
	{
		super(tar);
		this.mValue = new EffectAmount(val);
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
	}
	
	/*
     * Get the value of this Effect.
     * 
     * @return		The value as an integer.
     */
	public int getValue()
	{
		return this.mValue.eval();
	}	
	
	@Override
	public void execute() 
	{
		LOG.info(mTarget);
		List<Card> targetPokemon = (TargetServiceHandler.getInstance()).getTarget(mTarget);
		for(Card pokemon: targetPokemon) {
			System.out.println(pokemon.getName());
			((Pokemon) pokemon).addDamage(getValue());
		}
		System.out.println(mValue);
	}

	@Override
	public String toString()
	{
		return String.format("Damage: Target: %s, Value: %d", this.mTarget, this.mValue);
	}
	
	@Override
	public String str() {
		return String.format("DM %s, %s", this.mTarget, this.mValue);
	}
	
	//TODO: condition check
	@Override
	public boolean equals(Object obj)
	{
		Damage d = (Damage) obj;
		if(d.mTarget.equals(this.mTarget) && d.getValue() == this.getValue())
			return true;
		
		return false;
	}
}
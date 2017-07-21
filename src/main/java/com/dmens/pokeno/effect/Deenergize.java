package com.dmens.pokeno.effect;

import com.dmens.pokeno.condition.Condition;
import com.dmens.pokeno.condition.Flip;


public class Deenergize implements Effect {

	private int mAmount;
	private String mTarget;
	private Condition mCondition = null;
	
	/*
	 * Constructor
	 * 
	 * @param		amo		Integer value (amount).
	 * @param		tar		Target.
	 * @param		con		Condition.
	 */
	public Deenergize(int val, String tar, Condition con)
	{
		this.mAmount = val;
		this.mTarget = tar;
		this.mCondition = con;
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		d		Damage Effect.
	 */
	public Deenergize(Deenergize d)
	{
		this.mTarget = d.mTarget;
		this.mAmount = d.mAmount;
		
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
     * @return		The amount as an integer.
     */
	public int getAmount()
	{
		return this.mAmount;
	}

	@Override
	public void execute()
	{
		
	}
	
	@Override
	public String toString()
	{
		return String.format("Deenergize: Target: %s, Amount: %d", this.mTarget, this.mAmount);
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

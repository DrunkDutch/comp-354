package com.dmens.pokeno.Effect;

/*
 * A Heal effect.
 *
 * @author James
 */
public class Heal implements Effect {

	private int mValue;
	private String mTarget;
	
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
		// TODO Auto-generated method stub

	}

	@Override
	public String toString()
	{
		return String.format("%s:\t\tTAR: %s\t\tVAL:%d", Heal.class, this.mTarget, this.mValue);
	}
}

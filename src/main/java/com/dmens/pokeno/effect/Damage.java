package com.dmens.pokeno.effect;

/*
 * A Damage effect.
 *
 * @author James
 */
public class Damage implements Effect {

	private int mValue;
	private String mTarget;
	
	/*
	 * Constructor
	 * 
	 * @param		tar		Target.
	 * @param		val		Integer value (amount).
	 */
	public Damage(String tar, int val)
	{
		this.mTarget = tar;
		this.mValue = val;		
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
		return String.format("Damage: Target: %s, Value: %d", this.mTarget, this.mValue);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Damage d = (Damage) obj;
		if(d.mTarget.equals(this.mTarget) && d.mValue == this.mValue)
			return true;
		
		return false;
	}
}
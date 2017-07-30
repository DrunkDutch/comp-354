package com.dmens.pokeno.effect;

import com.dmens.pokeno.condition.Condition;

/*
 * Effect absract class
 *
 * @author James
 */
public abstract class Effect 
{
	
	protected String mTarget;
	protected Condition mCondition;
	
	/*
	 * Default Constructor
	 */
	public Effect() {
		mTarget = "";
		mCondition = null;
	}
	
	/*
	 * Constructor
	 * 
	 * @param		value		Integer value (amount).
	 * @param		target		Target.
	 */
	public Effect(String target, Condition mCondition) {
		this.mTarget = target;
		this.mCondition = mCondition; 
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
     * Check if a condition applies.
     * 
     * @return		Boolean value indicating the presence of a condition.
     */
	public boolean hasCondition()
	{
		return (this.mCondition != null);
	}
	
	/*
     * Get the condition.
     * 
     * @return		The condition as a Condition.
     */
	public Condition getCondition()
	{
		return this.mCondition;
	}
	
	public abstract void execute();
	
	public abstract String toString();
}
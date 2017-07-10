package com.dmens.pokeno.effect;

import com.dmens.pokeno.condition.*;

/*
 * An ApplyStatus effect.
 *
 * @author James
 */
public class ApplyStatus implements Effect {

	private String mTarget;
	private String mStatus;
	private Condition mCondition = null;
	
	/*
	 * Constructor
	 * 
	 * @param		tar		Target.
	 * @param		stat	Status.
	 */
	public ApplyStatus(String tar,String stat)
	{
		this.mTarget = tar;
		this.mStatus = stat;
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		a		ApplyStatus Effect.
	 */
	public ApplyStatus(ApplyStatus a)
	{
		this.mTarget = a.mTarget;
		this.mStatus = a.mStatus;		
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
     * Get the status that the effect will apply.
     * 
     * @return		The status as a string.
     */
	public String getStatus()
	{
		return this.mStatus;
	}
	

	@Override
	public void execute()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString()
	{
		return String.format("%s:\t\tTAR: %s\t\tSTA: %s", ApplyStatus.class, this.mTarget, this.mStatus);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		ApplyStatus a = (ApplyStatus) obj;
		if(a.mTarget.equals(this.mTarget) && a.mStatus.equals(this.mStatus))
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

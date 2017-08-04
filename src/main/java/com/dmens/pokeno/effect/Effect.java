package com.dmens.pokeno.effect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.utils.AbilityParser;

/*
 * Effect absract class
 *
 * @author James
 */
public abstract class Effect 
{
	protected static final Logger LOG = LogManager.getLogger(Effect.class);
	protected String mTarget;
	
	/*
	 * Default Constructor
	 */
	public Effect() {
		mTarget = "";
	}
	
	/*
	 * Constructor
	 * 
	 * @param		value		Integer value (amount).
	 * @param		target		Target.
	 */
	public Effect(String target) {
		this.mTarget = target;
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
	
	public abstract void execute();
	
	public abstract String toString();
	
	public abstract String str();
}
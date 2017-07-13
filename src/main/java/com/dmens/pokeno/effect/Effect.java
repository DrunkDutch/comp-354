package com.dmens.pokeno.effect;

import com.dmens.pokeno.condition.Condition;

/*
 * Effect interface
 *
 * @author James
 */
public interface Effect 
{
	public abstract void execute();
	
	public abstract String toString();
	
	public abstract boolean hasCondition();
	
	public abstract Condition getCondition();
}
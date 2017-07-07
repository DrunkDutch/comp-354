package com.dmens.pokeno.Ability;

import com.dmens.pokeno.condition.Condition;
import com.dmens.pokeno.effect.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/*
 * An Ability has a Name and a list of effects (Damage, Heal, ApplyStatus, etc...) 
 *
 * @author James
 */
public class Ability {
	
    private static final Logger LOG = LogManager.getLogger(Ability.class);
    private String mName = "";
    
    private ArrayList<Effect> mEffects;
    private ArrayList<Condition> mConditions;
    
    /*
     * Constructor
     * 
     * @param		name	Name of the ability.
     */
    public Ability(String name)
    {
    	this.mName = name;
    	this.mEffects = new ArrayList<Effect>();
    	this.mConditions = new ArrayList<Condition>();
    }
    
    /*
     * Add an effect to this Ability.
     * Check what the class it is, then cast the Effect to create a copy of it.
     * 
     * @param		e		Effect to add.	
     */
    public void addEffect(Effect e)
	{
    	this.mEffects.add(e);
	} 
    
    /*
     * TODO: Reevalute this part of design.
     * Add a condition to this Ability.
     * 
     * @param		c		Condition to add.	
     */
    public void addCondition(Condition c)
	{
		this.mConditions.add(c);
	}
    
    /*
     * Get the name of this Ability.
     * 
     * @return		The name as a string.
     */
    public String getName()
    {
    	return this.mName;
    }
    
    /*
     * Get a reference to all the effects of this Ability.
     * 
     * @return		As a ArrayList<Effect>.
     */
    public ArrayList<Effect> getEffects()
    {
    	return this.mEffects;
    }
    
    // TODO: make these three generic
    
    /*
     * Get the single Damage effect.
     * Assumption is that there is only one.
     * 
     * @return		Effect cast as Damage.
     */
    public Damage getDamageEffect()
    {
    	for (Effect effect: mEffects)
    	{
    	   if(effect.getClass() == Damage.class)
    	   {
    		   return (Damage) effect;
    	   }
    	}
    	 return null;
    }
    
    /*
     * Get the single Heal effect.
     * Assumption is that there is only one.
     * 
     * @return		Effect cast as Heal.
     */
    public Heal getHealEffect()
    {
    	for (Effect effect: mEffects)
    	{
    	   if(effect.getClass() == Heal.class)
    	   {
    		   return (Heal) effect;
    	   }
    	}
    	 return null;
    }
    
    /*
     * Get the single ApplyStatus effect.
     * Assumption is that there is only one.
     * 
     * @return		ApplyStatus cast as Damage.
     */
    public ApplyStatus getApplyStatusEffect()
    {
    	for (Effect effect: mEffects)
    	{
    	   if(effect.getClass() == ApplyStatus.class)
    	   {
    		   return (ApplyStatus) effect;
    	   }
    	}
    	 return null;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
	{
    	StringBuilder effectsAsList = new StringBuilder();
    	
    	for (Effect effect: mEffects)
    	{
    		effectsAsList.append("---" + effect.toString() + "\n");
    	}
    	
		return String.format("%s:->%s\n%s", Ability.class, this.mName, effectsAsList.toString());
	}
    
    public void performAbility() {
    	for (Effect effect: mEffects) {
    		effect.execute();
    	}
    	
    	for (Condition condition: mConditions) {
    		// TODO: wait until we decide how we are implementing conditions
    		//condition.execute();
    	}
    }
}
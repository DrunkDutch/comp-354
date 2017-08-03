package com.dmens.pokeno.ability;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.effect.ApplyStatus;
import com.dmens.pokeno.effect.Condition;
import com.dmens.pokeno.effect.Damage;
import com.dmens.pokeno.effect.Deenergize;
import com.dmens.pokeno.effect.DrawCard;
import com.dmens.pokeno.effect.Effect;
import com.dmens.pokeno.effect.Heal;
import com.dmens.pokeno.effect.Swap;

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
    	if(e == null) {
    		return;
    	}
    	
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
     * Get the single DrawCard effect.
     * Assumption is that there is only one.
     * 
     * @return		Effect cast as DrawCard.
     */
    public DrawCard getDrawCardEffect()
    {
    	for (Effect effect: mEffects)
    	{
    	   if(effect.getClass() == DrawCard.class)
    	   {
    		   return (DrawCard) effect;
    	   }
    	}
    	 return null;
    }
    
    /*

     * Get the single Swap effect.
     * Assumption is that there is only one.
     * 
     * @return		Effect cast as Swap.
     */
    public Swap getSwapEffect() {
		for (Effect effect : mEffects) {
			if (effect.getClass() == Swap.class) {
				return (Swap) effect;
			}
		}
		return null;
	}
	/*
     * Get the single Deenergize effect.
     * Assumption is that there is only one.
     * 
     * @return		Effect cast as Deenergize.
     */
    public Deenergize getDeenergizeEffect()
    {
    	for (Effect effect: mEffects)
    	{
    	   if(effect.getClass() == Deenergize.class)
    	   {
    		   return (Deenergize) effect;

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
    		effectsAsList.append(effect.toString() + "\n");
    	}
    	
		return effectsAsList.toString();
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
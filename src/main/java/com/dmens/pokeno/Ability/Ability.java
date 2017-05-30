package com.dmens.pokeno.Ability;

import com.dmens.pokeno.Effect.Effect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Devin on 2017-05-26.
 */
public class Ability {
	
    private static final Logger LOG = LogManager.getLogger(Ability.class);
    private String mName = "";
    
    private ArrayList<Effect> mEffects;
    
    public Ability(String name)
    {
    	this.mName = name;
    	this.mEffects = new ArrayList<Effect>();
    }
    
    public void AddEffect(Effect e)
	{
		this.mEffects.add(e);
	}  
}

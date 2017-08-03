package com.dmens.pokeno.effect;

import java.util.ArrayList;
import java.util.List;

public abstract class Condition extends Effect{

	private List<Effect> conditionTrueEffects;
	private List<Effect> conditionFalseEffects;
	
	public Condition(){
		conditionTrueEffects = new ArrayList<Effect>();
		conditionFalseEffects = new ArrayList<Effect>();
	}

	public void execute(){
		execute(true);
	}
	
	public void execute(boolean conditionPassed){
		List<Effect> conditionsToExecute = (conditionPassed ? conditionTrueEffects : conditionFalseEffects);
		conditionsToExecute.forEach(effect -> effect.execute());
	}
	
	public void addEffectTrue(Effect effect){
		conditionTrueEffects.add(effect);
	}
	
	public void addEffectFalse(Effect effect){
		conditionFalseEffects.add(effect);
	}
}

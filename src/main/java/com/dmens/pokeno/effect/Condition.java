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

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(Effect e : conditionTrueEffects){
			sb.append("-"+e.toString()+"\n");
		}
		if(!conditionFalseEffects.isEmpty()){
			sb.append("else:");
			for(Effect e : conditionTrueEffects){
				sb.append("-"+e.toString()+"\n");
			}
		}
		return sb.toString().trim();
	}

	@Override
	public String str()
	{
		StringBuilder sb = new StringBuilder();
		for(Effect e : conditionTrueEffects){
			sb.append("-"+e.str()+"\n");
		}
		if(!conditionFalseEffects.isEmpty()){
			sb.append("else:");
			for(Effect e : conditionTrueEffects){
				sb.append("-"+e.str()+"\n");
			}
		}
		return sb.toString().trim();
	}
}

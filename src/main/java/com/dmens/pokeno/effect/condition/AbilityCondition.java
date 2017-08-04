package com.dmens.pokeno.effect.condition;

import com.dmens.pokeno.effect.Condition;
import com.dmens.pokeno.effect.Effect;

public class AbilityCondition extends Condition {
	private Effect effectCondition;
	
	@Override
	public void execute(){
		// TODO do condition effect
		effectCondition.execute();
		super.execute(true);
	}
	
	public void setEffectCondition(Effect effectCondition) {
		this.effectCondition = effectCondition;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String str() {
		// TODO Auto-generated method stub
		return null;
	}
}

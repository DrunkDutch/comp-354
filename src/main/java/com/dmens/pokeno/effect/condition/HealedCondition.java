package com.dmens.pokeno.effect.condition;

import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.effect.Condition;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

public class HealedCondition extends Condition {
	private String mTarget;
	
	@Override
	public void execute(){
		Pokemon target = (Pokemon) TargetServiceHandler.getInstance().getTarget(this.mTarget).get(0);
		if(target.isHealed())
			super.execute(true);
		else
			super.execute(false);
	}
	
	public void setTarget(String target){
		this.mTarget = target;
	}

	@Override
	public String toString() {
		return "COND:HEALED\n"+super.str();
	}

	@Override
	public String str() {
		return "COND:HEALED\n"+super.str();
	}

}

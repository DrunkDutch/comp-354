package com.dmens.pokeno.effect;

import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.services.TargetService;

public class DeStat extends Effect
{
	public DeStat(String target)
	{
		mTarget = target;
	}
	
	@Override
	public void execute()
	{
		Pokemon poke = (Pokemon) TargetService.getInstance().getTarget(mTarget).get(0);
		poke.clearStatus();
	}

	@Override
	public String toString() {
		return String.format("Clear Status: Target: %s", this.mTarget);
	}
	
	@Override
	public String str() {
		return String.format("DESTAT %s", this.mTarget);
	}
}

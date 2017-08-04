package com.dmens.pokeno.effect.condition;

import com.dmens.pokeno.effect.Condition;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

public class FlipCondition extends Condition {
	
	public FlipCondition() {
		super();
	}
	
	@Override
	public void execute(){
		LOG.info("Executing Flip Condtion");
		// Get player
		Player player = TargetServiceHandler.getInstance().getPlayingPlayer();
		// Flip coin
		boolean heads = player.flipCoin();
		if(heads)
			super.execute(true);
		else
			super.execute(false);
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
 
package com.dmens.pokeno.effect;

import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;

public class ShuffleDeck extends Effect
{
	public ShuffleDeck(String target)
	{
		super(target, null);
		
	}
	
	public ShuffleDeck(ShuffleDeck s)
	{
		this.mTarget = s.mTarget;
	}
	
	@Override
	public void execute() {
		Player activePlayer = GameController.getActivePlayer();
		if(this.mTarget.equals("self")) {
			activePlayer.shuffleDeck();
		} else if(this.mTarget.equals("opponent")) {
			activePlayer.getOpponent().shuffleDeck();
		}
	}

	@Override
	public String toString() {
		return String.format("SHUFFLE: Target: %s", this.mTarget);
	}
	
}

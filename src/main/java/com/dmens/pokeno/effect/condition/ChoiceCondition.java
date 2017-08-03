package com.dmens.pokeno.effect.condition;

import javax.swing.JOptionPane;

import com.dmens.pokeno.effect.Condition;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

public class ChoiceCondition extends Condition {
	
	public void execute(){
		Player currentPlayer = TargetServiceHandler.getInstance().getPlayingPlayer();
		int reply = currentPlayer.makeChoice("Execute Condition Ability?");
		if(reply == JOptionPane.YES_OPTION)
			super.execute(true);
		else
			super.execute(false);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}

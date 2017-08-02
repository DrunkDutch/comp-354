package com.dmens.pokeno.effect;

import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import com.dmens.pokeno.condition.*;

/*
 * A Draw card effect.
 *
 * @author Jing
 */
public class DrawCard extends Effect{

	private int mValue;
	
	/*
	 * Constructor
	 * 
	 * @param		value		Integer value (amount).
	 * @param		target		Target.
	 */
	public DrawCard(int value, String target) {
		super(target, null);
		this.mValue = value;
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		d		DrawCard Effect.
	 */
	public DrawCard(DrawCard d)
	{
		this.mTarget = d.getTarget();
		this.mValue = d.getValue();		
	}
	
	/*
     * Get the value of this Effect.
     * 
     * @return		The value as an integer.
     */
	public int getValue() {
		return this.mValue;
	}
	
	@Override
	public void execute()
	{
		Player activePlayer = TargetServiceHandler.getInstance().getService().getPlayer("your-active");
		if(this.mTarget.equals("self")) {
			activePlayer.drawCardsFromDeck(mValue);
		} else if(this.mTarget.equals("opponent")) {
			activePlayer.getOpponent().drawCardsFromDeck(mValue);
		}
	}

	@Override
	public String toString()
	{
		return String.format("DRAW: Target: %s, Value: %d", this.mTarget, this.mValue);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		DrawCard d = (DrawCard) obj;
		if(d.mTarget.equals(this.mTarget) && d.mValue == this.mValue)
			return true;
		
		return false;
	}
}

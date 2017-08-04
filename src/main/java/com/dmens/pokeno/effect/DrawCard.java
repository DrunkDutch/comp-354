package com.dmens.pokeno.effect;

import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

/*
 * A Draw card effect.
 *
 * @author Jing
 */
public class DrawCard extends Effect{

	private EffectAmount mValue;
	
	/*
	 * Constructor
	 * 
	 * @param		value		Integer value (amount).
	 * @param		target		Target.
	 */
	public DrawCard(String value, String target) {
		super(target);
		this.mValue = new EffectAmount(value);
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		d		DrawCard Effect.
	 */
	public DrawCard(DrawCard d)
	{
		this.mTarget = d.getTarget();
		this.mValue = d.mValue;		
	}
	
	/*
     * Get the value of this Effect.
     * 
     * @return		The value as an integer.
     */
	public int getValue() {
		return this.mValue.eval();
	}
	
	@Override
	public void execute()
	{
		Player activePlayer = TargetServiceHandler.getInstance().getService().getPlayer("your-active");
		if(this.mTarget.equals("self")) {
			activePlayer.drawCardsFromDeck(getValue());
		} else if(this.mTarget.equals("opponent")) {
			activePlayer.getOpponent().drawCardsFromDeck(getValue());
		}
	}

	@Override
	public String toString()
	{
		return String.format("DRAW: Target: %s, Value: %s", this.mTarget, this.mValue);
	}
	
	@Override
	public String str() {
		return String.format("DRAW %s, %s", this.mTarget, this.mValue);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		DrawCard d = (DrawCard) obj;
		if(d.mTarget.equals(this.mTarget) && d.getValue() == this.getValue())
			return true;
		
		return false;
	}
}

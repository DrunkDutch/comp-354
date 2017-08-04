package com.dmens.pokeno.effect;

import java.util.List;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

/*
 * An ApplyStatus effect.
 *
 * @author James
 */
public class ApplyStatus extends Effect {

	private String mStatus;	
	/*
	 * Constructor
	 * 
	 * @param		tar		Target.
	 * @param		stat	Status.
	 */
	public ApplyStatus(String tar,String stat)
	{
		super(tar);
		this.mStatus = stat;
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		a		ApplyStatus Effect.
	 */
	public ApplyStatus(ApplyStatus a)
	{
		this.mTarget = a.mTarget;
		this.mStatus = a.mStatus;		
	}
	
	/*
     * Get the status that the effect will apply.
     * 
     * @return		The status as a string.
     */
	public String getStatus()
	{
		return this.mStatus;
	}
	

	@Override
	public void execute()
	{
		Player targetPlayer = TargetServiceHandler.getInstance().getService().getPlayer(this.mTarget);
		List<Card> targets = TargetServiceHandler.getInstance().getTarget(this.mTarget);
		targets.forEach(target -> {
			((Pokemon)target).setStatus(this.mStatus);
			TargetServiceHandler.getInstance().getPlayingPlayer().displayMessage(target.getName()+ "was " + mStatus);
			targetPlayer.updatePokemonStatusOnBoard();
		});
	}

	@Override
	public String toString()
	{
		return String.format("%s:\t\tTAR: %s\t\tSTA: %s", ApplyStatus.class, this.mTarget, this.mStatus);
	}
	
	@Override
	public String str() {
		return String.format("ST %s, %s", this.mTarget, this.mStatus);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		ApplyStatus a = (ApplyStatus) obj;
		if(a.mTarget.equals(this.mTarget) && a.mStatus.equals(this.mStatus))
			return true;
		
		return false;
	}
}

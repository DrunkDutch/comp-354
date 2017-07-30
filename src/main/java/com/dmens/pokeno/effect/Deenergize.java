package com.dmens.pokeno.effect;

import com.dmens.pokeno.condition.Condition;
import com.dmens.pokeno.condition.Flip;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.TargetService;
import com.dmens.pokeno.utils.Randomizer;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;


public class Deenergize extends Effect {

	private int mAmount;
	
	/*
	 * Constructor
	 * 
	 * @param		amo		Integer value (amount).
	 * @param		tar		Target.
	 * @param		con		Condition.
	 */
	public Deenergize(int val, String tar, Condition con)
	{
		super(tar, con);
		this.mAmount = val;
	}
	
	/*
	 * Copy Constructor
	 * 
	 * @param		d		Damage Effect.
	 */
	public Deenergize(Deenergize d)
	{
		this.mTarget = d.mTarget;
		this.mAmount = d.mAmount;
		
		if(d.mCondition instanceof Flip)
			this.mCondition = new Flip();
	}
	
	/*
     * Get the value of this Effect.
     * 
     * @return		The amount as an integer.
     */
	public int getAmount()
	{
		return this.mAmount;
	}

	@Override
	public void execute()
	{
		boolean proceedWithAttack = true;
		
		// 1) Get the target
		Pokemon poke = (Pokemon) TargetService.getInstance().getTarget(mTarget).get(0);
		
		// 2) Determine if there is a condition, if so... handle it
		if(mCondition != null)
		{
			if(mCondition instanceof Flip)
			{
				if(Randomizer.Instance().getFiftyPercentChance())
				{
					proceedWithAttack = false;
					GameController.displayMessage(poke.getName() + " avoided the attack!");
				}
			}
		}
		
		// 3) Use the effect!
		if(proceedWithAttack)
		{
			for (int i = 0; i < mAmount; i++)
			{
				if(poke.getAttachedEnergy().size() == 0)
					GameController.displayMessage(poke.getName() + " has no (more) energy to remove!");
				
				Player player = TargetService.getInstance().getPlayer(mTarget);
    			EnergyTypes type = player.createEnergyOptionPane(player.getActivePokemon(), "Remove an Energy from " + poke.getName(), "Which energy would you like to remove?", false);
    			
    			System.out.println("Removed: " + poke.removeSingleEnergy(type));
			}
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("Deenergize: Target: %s, Amount: %d", this.mTarget, this.mAmount);
	}
}

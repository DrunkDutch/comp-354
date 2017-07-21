package com.dmens.pokeno.effect;

import com.dmens.pokeno.condition.Condition;
import com.dmens.pokeno.condition.Flip;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.utils.Randomizer;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;


public class Deenergize implements Effect {

	private int mAmount;
	private String mTarget;
	private Condition mCondition = null;
	
	/*
	 * Constructor
	 * 
	 * @param		amo		Integer value (amount).
	 * @param		tar		Target.
	 * @param		con		Condition.
	 */
	public Deenergize(int val, String tar, Condition con)
	{
		this.mAmount = val;
		this.mTarget = tar;
		this.mCondition = con;
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
     * Get the target of this Effect.
     * 
     * @return		The target as a string.
     */
	public String getTarget()
	{
		return this.mTarget;
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
		Player player = null;
		
		// 1) Determine the target
		if(mTarget.equals("opponent-active"))
		{
			player = GameController.getOpponentPlayer();
		}
		else if (mTarget.equals("your-active"))
		{
			player = GameController.getActivePlayer();
		}
		
		// 2) Determine if there is a condition, if so... handle it
		if(mCondition != null)
		{
			if(mCondition instanceof Flip)
			{
				if(Randomizer.Instance().getFiftyPercentChance())
				{
					proceedWithAttack = false;
					GameController.displayMessage(player.getActivePokemon().getName() + " avoided the attack!");
				}
			}
		}
		
		// 3) Use the effect!
		if(proceedWithAttack)
		{
			for (int i = 0; i < mAmount; i++)
			{
				if(player.getActivePokemon().getAttachedEnergy().size() == 0)
					GameController.displayMessage(player.getActivePokemon().getName() + " has no (more) energy to remove!");
    			EnergyTypes type = player.createEnergyOptionPane(player.getActivePokemon(), "Remove an Energy", "Which energy would you like to remove?", false);
    			player.getActivePokemon().removeSingleEnergy(type);
			}
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("Deenergize: Target: %s, Amount: %d", this.mTarget, this.mAmount);
	}

	@Override
	public boolean hasCondition()
	{
		return (mCondition != null);
	}

	@Override
	public Condition getCondition()
	{
		return mCondition;
	}

}

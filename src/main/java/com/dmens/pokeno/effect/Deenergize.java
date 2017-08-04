package com.dmens.pokeno.effect;

import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.CountService;
import com.dmens.pokeno.services.TargetService;


public class Deenergize extends Effect {

	private EffectAmount mAmount;
	
	/*
	 * Constructor
	 * 
	 * @param		amo		Integer value (amount).
	 * @param		tar		Target.
	 * @param		con		Condition.
	 */
	public Deenergize(String val, String tar)
	{
		super(tar);
		this.mAmount = new EffectAmount(val);
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
	}
	
	/*
     * Get the value of this Effect.
     * 
     * @return		The amount as an integer.
     */
	public int getAmount()
	{
		return this.mAmount.eval();
	}

	@Override
	public void execute()
	{
		// 1) Get the target
		System.out.println("target: " + mTarget);
		Pokemon poke = (Pokemon) TargetService.getInstance().getTarget(mTarget).get(0);
	
		// 2) Use the effect!
		for (int i = 0; i < mAmount.eval(); i++)
		{
			if(poke.getAttachedEnergy().size() == 0)
				GameController.displayMessage(poke.getName() + " has no (more) energy to remove!");

			Player player = TargetService.getInstance().getPlayer(mTarget);
			EnergyTypes type = player.createEnergyOptionPane(player.getActivePokemon(), "Remove an Energy from " + poke.getName(), "Which energy would you like to remove?", false);

			System.out.println("Removed: " + poke.removeSingleEnergy(type));
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("Deenergize: Target: %s, Amount: %s", this.mTarget, this.mAmount);
	}
	
	@Override
	public String str() {
		return String.format("DEE %s, %s", this.mTarget, this.mAmount);
	}
}

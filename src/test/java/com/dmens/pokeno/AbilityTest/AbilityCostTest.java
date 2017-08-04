package com.dmens.pokeno.AbilityTest;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.ability.AbilityCost;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.effect.Damage;

public class AbilityCostTest {
	static String mAbilityName = "Ability";
	static String mEffectTarget = "opponent-active";
	static String mEffectValue = "20";
	static int cost1 = 2;
	static int cost2 = 1;
	private Ability ability;
	
	@Before
	public void createAbility(){
		ability = new Ability(mAbilityName);
		Damage damage = new Damage(mEffectTarget, mEffectValue);
		ability.addEffect(damage);
	}

	@Test
	public void testAbilityCostCreation() {
		EnergyTypes type1 = EnergyTypes.FIGHT;
		EnergyTypes type2 = EnergyTypes.COLORLESS;
		String output1 = type1+": "+cost1+"\n";
		String output2 = type2 + ": "+cost2+"\n" + output1;
		AbilityCost abilityCost = new AbilityCost(ability);
		abilityCost.addCost(type1, cost1);
		Assert.assertTrue(abilityCost.getCosts().get(type1) == cost1);
		
		abilityCost.addCost(type2, cost2);
		Assert.assertTrue(abilityCost.getCosts().get(type1) == cost1);
		Assert.assertTrue(abilityCost.getCosts().get(type2) == cost2);
		
		abilityCost.addCost(type1, cost2);
		Assert.assertTrue(abilityCost.getCosts().get(type1) == cost1 + cost2);
		Assert.assertTrue(abilityCost.getCosts().get(type2) == cost2);
	}

}

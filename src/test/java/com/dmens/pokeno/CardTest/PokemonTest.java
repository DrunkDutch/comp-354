package com.dmens.pokeno.CardTest;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.ability.AbilityCost;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.effect.Damage;

public class PokemonTest {
	
	//Froakie:pokemon:cat:basic:cat:water:50:retreat:cat:colorless:1:attacks:cat:colorless:1:14
	static String mPokemonFroakieName = "Froakie";
	static int mPokemonFroakieHP = 50;
	static int mPokemonFroakieRetreatCost = 1;
	static int mPokemonFroakieAttackCost = 1;
	static EnergyTypes mPokemonFroakieAttackRequiredType = EnergyTypes.COLORLESS;
	
	//Pound:dam:target:opponent-active:10
	static String mAbilityPoundName = "Pound";
	static String mAbilityPoundTarget = "opponent-active";
	static String mAbilityPoundValue = "10";

	static Pokemon froakie;

	@Before
	public void setup(){
		froakie = new Pokemon(mPokemonFroakieName, "basic", mPokemonFroakieHP, mPokemonFroakieRetreatCost);
	}
	
	@Test
	public void testPokemonCreation() {
		Ability abilityPound = new Ability(mAbilityPoundName);
        abilityPound.addEffect(new Damage(mAbilityPoundTarget, mAbilityPoundValue));
        
        AbilityCost abilityCost = new AbilityCost(abilityPound);
		abilityCost.addCost(mPokemonFroakieAttackRequiredType, mPokemonFroakieAttackCost);


        froakie.AddAbilityAndCost(abilityCost);
        
        Assert.assertEquals(froakie.getName(), mPokemonFroakieName);
        Assert.assertEquals(froakie.getCategory(), "basic");
        Assert.assertEquals(froakie.getHP(), mPokemonFroakieHP);
        Assert.assertEquals(froakie.getRetreatCost(), mPokemonFroakieRetreatCost);

        froakie.setStuck(true);
        Assert.assertEquals(true, froakie.isStuck());
        froakie.setParalyzed(true);
        Assert.assertEquals(true, froakie.isParalyzed());
        froakie.setPoisoned(true);
        Assert.assertEquals(true, froakie.isPoisoned());
        froakie.setParalyzed(true);
        Assert.assertEquals(true, froakie.isParalyzed());
	}
	
	@Test
	public void testEvolvePokemon(){
		//Create pokemon 1 (Basic)
		froakie.setCategory("basic");
		// Create pokemon 2 (Stage-one)
		Pokemon frogadier = new Pokemon("frogadier");
		frogadier.setCategory("stage-one");
		frogadier.setBasePokemonName("froakie");
		// Create pokemon 3
		Pokemon machop = new Pokemon("machop");
		froakie.setCategory("basic");
		
		
		// Try to evolve machop to frogadier
		assertFalse(frogadier.evolvePokemon(machop));
		
		// Attach energy to froakie and give damage
		froakie.addDamage(10);
		froakie.addEnergy(new EnergyCard("Water", "water"));
		
		// Should evolve from froakie
		assertTrue(frogadier.evolvePokemon(froakie));
		// Should transfer energy
		assertEquals(froakie.getAttachedEnergy().size(), frogadier.getAttachedEnergy().size());
		// Should transfer damage
		assertEquals(froakie.getDamage(), frogadier.getDamage());
		// Should keep a pointer to the base's card
		assertEquals(froakie, frogadier.getBaseCardReference());
	}

	@Test
	public void testPokemonStatuses(){
		froakie.setStuck(true);
		Assert.assertEquals(true, froakie.isStuck());
		froakie.setParalyzed(true);
		Assert.assertEquals(true, froakie.isParalyzed());
		froakie.setPoisoned(true);
		Assert.assertEquals(true, froakie.isPoisoned());
		froakie.setParalyzed(true);
		Assert.assertEquals(true, froakie.isParalyzed());
		froakie.setHealed(true);
		Assert.assertEquals(true, froakie.isHealed());
	}
}

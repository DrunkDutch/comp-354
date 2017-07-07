package com.dmens.pokeno.CardTest;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.Ability.AbilityCost;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.card.TrainerCard;
import com.dmens.pokeno.effect.Damage;
import com.dmens.pokeno.effect.Heal;

public class CardTest {
	
	static String mEnergyCardName = "Water";
	static String mEnergyCardCategory = "water";
	
	static String mTrainerCardPotionName = "Potion";
	static String mTrainerCardPotionCategory = "item";
	
	static String mAbilityPotionName = "Potion";
	static String mAbilityPotionTarget = "your";
	static int mAbilityPotionValue = 30;
	
	//Froakie:pokemon:cat:basic:cat:water:50:retreat:cat:colorless:1:attacks:cat:colorless:1:14
	static String mPokemonFroakieName = "Froakie";
	static int mPokemonFroakieHP = 50;
	static int mPokemonFroakieRetreatCost = 1;
	static int mPokemonFroakieAttackCost = 1;
	static EnergyTypes mPokemonFroakieAttackRequiredType = EnergyTypes.COLORLESS;
	
	//Pound:dam:target:opponent-active:10
	static String mAbilityPoundName = "Pound";
	static String mAbilityPoundTarget = "opponent-active";
	static int mAbilityPoundValue = 10;
    
	@Test
    public void cardTest(){

		// Test EnergyCard
        EnergyCard energyCard = new EnergyCard(mEnergyCardName, mEnergyCardCategory);
        Assert.assertEquals(energyCard.getName(), mEnergyCardName);
        Assert.assertEquals(energyCard.getCategory(), EnergyTypes.WATER);
        
        // Test TrainerCard
        Ability abilityPotion = new Ability(mAbilityPotionName);
        abilityPotion.addEffect(new Heal(mAbilityPotionTarget, mAbilityPotionValue));
        		
        TrainerCard trainerCard = new TrainerCard(mTrainerCardPotionName, mTrainerCardPotionCategory, abilityPotion);
        Assert.assertEquals(trainerCard.getName(), mTrainerCardPotionName);
        Assert.assertEquals(trainerCard.getCategory(), mTrainerCardPotionCategory);
        Assert.assertEquals(trainerCard.getAbility(), abilityPotion);
        
        Assert.assertEquals(trainerCard.getAbility().getName(), mAbilityPotionName);
        Assert.assertEquals(trainerCard.getAbility().getHealEffect().getTarget(), mAbilityPotionTarget);
        Assert.assertEquals(trainerCard.getAbility().getHealEffect().getValue(), mAbilityPotionValue);
        
        // Test Pokemon    
        Ability abilityPound = new Ability(mAbilityPoundName);
        abilityPound.addEffect(new Damage(mAbilityPoundTarget, mAbilityPoundValue));
        
        AbilityCost abilityCost = new AbilityCost(abilityPound);
		abilityCost.addCost(mPokemonFroakieAttackRequiredType, mPokemonFroakieAttackCost);

        Pokemon froakie = new Pokemon(mPokemonFroakieName, "basic", mPokemonFroakieHP, mPokemonFroakieRetreatCost);
        froakie.AddAbilityAndCost(abilityCost);
        
        Assert.assertEquals(froakie.getName(), mPokemonFroakieName);
        Assert.assertEquals(froakie.getCategory(), "basic");
        Assert.assertEquals(froakie.getHP(), mPokemonFroakieHP);
        Assert.assertEquals(froakie.getRetreatCost(), mPokemonFroakieRetreatCost);

        froakie.setConfused(true);
        Assert.assertEquals(true, froakie.isConfused());
        froakie.setParalyzed(true);
        Assert.assertEquals(true, froakie.isParalyzed());
        froakie.setPoisoned(true);
        Assert.assertEquals(true, froakie.isPoisoned());
        froakie.setParalyzed(true);
        Assert.assertEquals(true, froakie.isParalyzed());


    }

}

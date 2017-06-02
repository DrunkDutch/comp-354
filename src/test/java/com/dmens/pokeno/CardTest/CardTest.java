package com.dmens.pokeno.CardTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.Effect.*;
import com.dmens.pokeno.Utils.Tuple;
import com.dmens.pokeno.Card.*;

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
	static ArrayList<String> mPokemonFroakieCategories = new ArrayList<String>();
	static int mPokemonFroakieHP = 50;
	static int mPokemonFroakieRetreatCost = 1;
	static int mPokemonFroakieAttackCost = 1;
	static String mPokemonFroakieAttackRequiredType = "colorless";
	
	//Pound:dam:target:opponent-active:10
	static String mAbilityPoundName = "Pound";
	static String mAbilityPoundTarget = "opponent-active";
	static int mAbilityPoundValue = 10;

	@Test
    public void cardTest(){

		// Test EnergyCard
        EnergyCard energyCard = new EnergyCard(mEnergyCardName, mEnergyCardCategory);
        Assert.assertEquals(energyCard.getName(), mEnergyCardName);
        Assert.assertEquals(energyCard.getCategory(), mEnergyCardCategory);
        
        // Test TrainerCard
        ArrayList<Ability> potionAbilities = new ArrayList<Ability>();
        Ability abilityPotion = new Ability(mAbilityPotionName);
        abilityPotion.addEffect(new Heal(mAbilityPotionTarget, mAbilityPotionValue));
        potionAbilities.add(abilityPotion);
        		
        TrainerCard trainerCard = new TrainerCard(mTrainerCardPotionName, mTrainerCardPotionCategory, potionAbilities);
        Assert.assertEquals(trainerCard.getName(), mTrainerCardPotionName);
        Assert.assertEquals(trainerCard.getCategory(), mTrainerCardPotionCategory);
        Assert.assertEquals(trainerCard.getAbilities(), potionAbilities);
        
        Assert.assertEquals(trainerCard.getAbilities().get(0).getName(), mAbilityPotionName);
        Assert.assertEquals(trainerCard.getAbilities().get(0).getHealEffect().getTarget(), mAbilityPotionTarget);
        Assert.assertEquals(trainerCard.getAbilities().get(0).getHealEffect().getValue(), mAbilityPotionValue);
        
        // Test Pokemon    
        Ability abilityPound = new Ability(mAbilityPoundName);
        abilityPound.addEffect(new Damage(mAbilityPoundTarget, mAbilityPoundValue));
        mPokemonFroakieCategories.add("basic");
        mPokemonFroakieCategories.add("water");
        
        Tuple<Ability, String, Integer> tuple = new Tuple<Ability, String, Integer>(abilityPound, mPokemonFroakieAttackRequiredType, mPokemonFroakieAttackCost);

        Pokemon froakie = new Pokemon(mPokemonFroakieName, mPokemonFroakieCategories, mPokemonFroakieHP, mPokemonFroakieRetreatCost);
        froakie.AddAbilityAndCost(tuple);
        
        Assert.assertEquals(froakie.getName(), mPokemonFroakieName);
        Assert.assertEquals(froakie.getCategories(), mPokemonFroakieCategories);
        Assert.assertEquals(froakie.getHP(), mPokemonFroakieHP);
        Assert.assertEquals(froakie.getRetreatCost(), mPokemonFroakieRetreatCost);

    }

}

package com.dmens.pokeno.CardTest;

import org.junit.Assert;
import org.junit.Test;


import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.TrainerCard;
import com.dmens.pokeno.effect.Heal;

public class CardTest {
	
	static String mEnergyCardName = "Water";
	static String mEnergyCardCategory = "water";
	
	static String mTrainerCardPotionName = "Potion";
	static String mTrainerCardPotionCategory = "item";
	
	static String mAbilityPotionName = "Potion";
	static String mAbilityPotionTarget = "your";
	static int mAbilityPotionValue = 30;
    
	@Test
    public void testCardCreation(){
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
    }

}

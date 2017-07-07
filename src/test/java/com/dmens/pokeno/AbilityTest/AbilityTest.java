package com.dmens.pokeno.AbilityTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.effect.*;

/**
*
* @author James
*/
public class AbilityTest {
	
	 static String mAbilityName = "Ability";
	 static String mEffectTarget = "opponent-active";
	 static int mEffectValue = 20;
	 static int mEffectValueDifferent = 10;
	 static String mEffectStatus = "asleep";
	 static String mEffectStatusDifferent = "poisoned";

    @Test
    public void abilityTest(){
        
    	Ability ability = new Ability(mAbilityName);
    	Assert.assertEquals(ability.getName(), mAbilityName);
    	
    	// Stand alone Effects
    	Heal heal = new Heal(mEffectTarget, mEffectValue);
    	Assert.assertEquals(heal.getTarget(), mEffectTarget);
    	Assert.assertEquals(heal.getValue(), mEffectValue);
    	
    	Damage damage = new Damage(mEffectTarget, mEffectValue);
    	Assert.assertEquals(damage.getTarget(), mEffectTarget);
    	Assert.assertEquals(damage.getValue(), mEffectValue);
    	
    	ApplyStatus applyStatus = new ApplyStatus(mEffectTarget, mEffectStatus);
    	Assert.assertEquals(applyStatus.getTarget(), mEffectTarget);
    	Assert.assertEquals(applyStatus.getStatus(), mEffectStatus);
    	
    	// Add each Effect to the Ability
    	ability.addEffect(heal);
    	Assert.assertEquals(ability.getHealEffect(), heal);
    	Assert.assertEquals(ability.getHealEffect().getTarget(), mEffectTarget);
    	Assert.assertEquals(ability.getHealEffect().getValue(), mEffectValue);
    	
    	ability.addEffect(damage);
    	Assert.assertEquals(ability.getDamageEffect(), damage);
    	Assert.assertEquals(ability.getDamageEffect().getTarget(), mEffectTarget);
    	Assert.assertEquals(ability.getDamageEffect().getValue(), mEffectValue);
    	
    	ability.addEffect(applyStatus);
    	Assert.assertEquals(ability.getApplyStatusEffect(), applyStatus);
    	Assert.assertEquals(ability.getApplyStatusEffect().getTarget(), mEffectTarget);
    	Assert.assertEquals(ability.getApplyStatusEffect().getStatus(), mEffectStatus);    
    	
    	// change effects... check that effects in abilities are unaffected
    	heal = new Heal(mEffectTarget, mEffectValueDifferent);
    	Assert.assertNotEquals(ability.getHealEffect(), heal);
    	
    	damage = new Damage(mEffectTarget, mEffectValueDifferent);
    	Assert.assertNotEquals(ability.getDamageEffect(), damage);
    	
    	applyStatus = new ApplyStatus(mEffectTarget, mEffectStatusDifferent);
    	Assert.assertNotEquals(ability.getApplyStatusEffect(), applyStatus);
    }

}

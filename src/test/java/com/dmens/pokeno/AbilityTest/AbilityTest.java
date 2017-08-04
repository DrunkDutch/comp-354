package com.dmens.pokeno.AbilityTest;

import static org.junit.Assert.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.Arrays;

import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.effect.ApplyStatus;
import com.dmens.pokeno.effect.Damage;
import com.dmens.pokeno.effect.DrawCard;
import com.dmens.pokeno.effect.Heal;
import com.dmens.pokeno.effect.Swap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
*
* @author James
*/

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class AbilityTest {
	
	 static String mAbilityName = "Ability";
	 static String mEffectTarget = "opponent-active";
	 static String mEffectTargetDifferent = "your-active";
	 static String mEffectValue = "20";
	 static String mEffectValueDifferent = "10";
	 static String mEffectStatus = "asleep";
	 static String mEffectStatusDifferent = "poisoned";
	 static String mEffectDestination = "choice:your-bench";

	 @Before
	 public void setup(){
		//Mocks
		PowerMockito.mockStatic(GameController.class);
	 }

    @Test
    public void abilityTest(){
        
    	Ability ability = new Ability(mAbilityName);
    	Assert.assertEquals(ability.getName(), mAbilityName);
    	
    	// Stand alone Effects
    	Heal heal = new Heal(mEffectTarget, mEffectValue);
    	Assert.assertEquals(heal.getTarget(), mEffectTarget);
    	Assert.assertEquals(heal.getValue(), Integer.parseInt(mEffectValue));
    	
    	Damage damage = new Damage(mEffectTarget, mEffectValue);
    	Assert.assertEquals(damage.getTarget(), mEffectTarget);
    	Assert.assertEquals(damage.getValue(), Integer.parseInt(mEffectValue));
    	
    	ApplyStatus applyStatus = new ApplyStatus(mEffectTarget, mEffectStatus);
    	Assert.assertEquals(applyStatus.getTarget(), mEffectTarget);
    	Assert.assertEquals(applyStatus.getStatus(), mEffectStatus);
    	
    	DrawCard drawCard = new DrawCard(mEffectValue, mEffectTarget);
    	Assert.assertEquals(drawCard.getTarget(), mEffectTarget);
    	Assert.assertEquals(drawCard.getValue(), Integer.parseInt(mEffectValue));
    	
    	Swap swap = new Swap(mEffectTarget, mEffectDestination);
    	Assert.assertEquals(swap.getTarget(), mEffectTarget);
    	Assert.assertEquals(swap.getDestination(), mEffectDestination);
    	
    	// Add each Effect to the Ability
    	ability.addEffect(heal);
    	Assert.assertEquals(ability.getHealEffect(), heal);
    	Assert.assertEquals(ability.getHealEffect().getTarget(), mEffectTarget);
    	Assert.assertEquals(ability.getHealEffect().getValue(), Integer.parseInt(mEffectValue));
    	
    	ability.addEffect(damage);
    	Assert.assertEquals(ability.getDamageEffect(), damage);
    	Assert.assertEquals(ability.getDamageEffect().getTarget(), mEffectTarget);
    	Assert.assertEquals(ability.getDamageEffect().getValue(), Integer.parseInt(mEffectValue));
    	
    	ability.addEffect(applyStatus);
    	Assert.assertEquals(ability.getApplyStatusEffect(), applyStatus);
    	Assert.assertEquals(ability.getApplyStatusEffect().getTarget(), mEffectTarget);
    	Assert.assertEquals(ability.getApplyStatusEffect().getStatus(), mEffectStatus);    
    	
    	ability.addEffect(drawCard);
    	Assert.assertEquals(ability.getDrawCardEffect(), drawCard);
    	Assert.assertEquals(ability.getDrawCardEffect().getTarget(), mEffectTarget);
    	Assert.assertEquals(ability.getDrawCardEffect().getValue(), Integer.parseInt(mEffectValue));
    	
    	ability.addEffect(swap);
    	Assert.assertEquals(ability.getSwapEffect(), swap);
    	Assert.assertEquals(ability.getSwapEffect().getTarget(), mEffectTarget);
    	Assert.assertEquals(ability.getSwapEffect().getDestination(), mEffectDestination);
    	
    	// change effects... check that effects in abilities are unaffected
    	heal = new Heal(mEffectTarget, mEffectValueDifferent);
    	Assert.assertNotEquals(ability.getHealEffect(), heal);
    	
    	damage = new Damage(mEffectTarget, mEffectValueDifferent);
    	Assert.assertNotEquals(ability.getDamageEffect(), damage);
    	
    	applyStatus = new ApplyStatus(mEffectTarget, mEffectStatusDifferent);
    	Assert.assertNotEquals(ability.getApplyStatusEffect(), applyStatus);
    	
    	drawCard = new DrawCard(mEffectValueDifferent, mEffectTarget);
    	Assert.assertNotEquals(ability.getDrawCardEffect(), drawCard);
    	
    	swap = new Swap(mEffectTargetDifferent, mEffectDestination);
    	Assert.assertNotEquals(ability.getSwapEffect(), swap);
    }
}

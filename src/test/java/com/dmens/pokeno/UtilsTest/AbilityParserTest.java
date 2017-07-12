package com.dmens.pokeno.UtilsTest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.utils.AbilityParser;

public class AbilityParserTest {
	private static String damageAbilityString = "Bite:dam:target:opponent-active:40";

	@Test
	public void testCreateDamageAbility() {
		Ability a = AbilityParser.getAbilityFromString(damageAbilityString);
		assertEquals(a.getName(), "Bite");
		assertEquals(a.getDamageEffect().getTarget(), "opponent-active");
		assertEquals(a.getDamageEffect().getValue(), 40);
	}

}

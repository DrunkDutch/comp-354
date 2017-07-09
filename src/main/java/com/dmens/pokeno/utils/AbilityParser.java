package com.dmens.pokeno.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.effect.*;

public class AbilityParser {
	private static final Logger LOG = LogManager.getLogger(AbilityParser.class);
	
	public static Ability getAbilityFromString(String abilityInformation){
    	int indexName = abilityInformation.indexOf(":");
    	Ability ability = new Ability(abilityInformation.substring(0,indexName));
    	String restStr = abilityInformation.substring(indexName + 1, abilityInformation.length());
    	List<String> effects = Arrays.asList(restStr.split(","));
    	
    	effects.forEach(effect ->{
    		LOG.debug(effect);
    		ability.addEffect(ParseEfect(effect));
    	});
    	
    	return ability;
	}
	

	private static Effect ParseEfect(String effect)
	{
		Stack<String> effectStack = new Stack<String>();
		String[] parsedString = effect.split(":");
		for (int i = parsedString.length -1; i >= 0; i--) 
			effectStack.add(parsedString[i]);
		switch(EffectTypes.valueOf(effectStack.pop().toUpperCase())){
		case DAM:
			return getDamageEffect(effectStack);
		case HEAL:
			return getHealEffect(effectStack);
		case APPLYSTAT:
			return getApplyStatusEffect(effectStack);
		case DRAW:
			return getDrawCardEffect(effectStack);
		default:
			return null;
		}
	}
	
	private static String getTarget(Stack<String> effectStack){
		effectStack.pop();	// target
		String target = effectStack.pop();
		if(target.contains("choice")) {
			target = effectStack.pop();
			if(target.equals("your")) {
				return target;
			} else {
				return null;
			}
		}
		return target;
	}
	
	private static Effect getDamageEffect(Stack<String> effectStack){
		String target = getTarget(effectStack);
		if(target == null)
			return null;
		// TODo Count multiplier
		if(effectStack.peek().contains("count"))
			return null;
		int damage = Integer.parseInt(effectStack.pop());
		return new Damage(target, damage); 
	}
	
	private static Effect getHealEffect(Stack<String> effectStack){
		String target = getTarget(effectStack);
		if(target == null)
			return null;
		int healValue = Integer.parseInt(effectStack.pop());
		return new Heal(target, healValue); 
	}
	
	private static Effect getApplyStatusEffect(Stack<String> effectStack){
		String status = getStatus(effectStack);
		String target = effectStack.pop();
		return new ApplyStatus(target, status); 
	}
	
	private static Effect getDrawCardEffect(Stack<String> effectStack){
		int value = 0;
		String target = "";
		
		if(effectStack.size() == 2) {
			target = effectStack.pop();
			value = Integer.parseInt(effectStack.pop());

		} else if(effectStack.size() == 1) {
			target = "self";
			value = Integer.parseInt(effectStack.pop());
		}

		return new DrawCard(value, target); 
	}
	
	private static String getStatus(Stack<String> effectStack){
		effectStack.pop();	// status
		return effectStack.pop();
	}
	
	

}

package com.dmens.pokeno.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.effect.ApplyStatus;
import com.dmens.pokeno.effect.Condition;
import com.dmens.pokeno.effect.Damage;
import com.dmens.pokeno.effect.Deenergize;
import com.dmens.pokeno.effect.DrawCard;
import com.dmens.pokeno.effect.Effect;
import com.dmens.pokeno.effect.EffectTypes;
import com.dmens.pokeno.effect.Heal;
import com.dmens.pokeno.effect.Search;
import com.dmens.pokeno.effect.Swap;
import com.dmens.pokeno.effect.condition.AbilityCondition;
import com.dmens.pokeno.effect.condition.ConditionTypes;
import com.dmens.pokeno.effect.condition.FlipCondition;

public class AbilityParser {
	private static final Logger LOG = LogManager.getLogger(AbilityParser.class);
	
	public static Ability getAbilityFromString(String abilityInformation){
		LOG.debug(abilityInformation);
    	int indexName = abilityInformation.indexOf(":");
    	Ability ability = new Ability(abilityInformation.substring(0,indexName));
    	
    	String restStr = abilityInformation.substring(indexName + 1, abilityInformation.length());
    	// Divide effects separated by comma
    	Pattern p = Pattern.compile("(.+?(\\(.*?\\))*?)(?:,|$)");
    	Matcher m = p.matcher(restStr);
    	List<String> effects = new LinkedList<String>();
    	while(m.find())
    	{
    	  String token = m.group( 1 );
    	  effects.add(token);
    	}
    	effects.forEach(effect ->{
    		LOG.debug("-- "+effect);
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
		switch(EffectTypes.valueOf(effectStack.pop().toUpperCase()))
		{
			case COND:
				return getConditionEffect(effectStack);
			case DAM:
				return getDamageEffect(effectStack);
			case HEAL:
				return getHealEffect(effectStack);
			case APPLYSTAT:
				return getApplyStatusEffect(effectStack);
			case DRAW:
				return getDrawCardEffect(effectStack);
			case SWAP:
				return getSwapEffect(effectStack);
			case DEENERGIZE:
				return getDeenergizeEffect(effectStack);
			case SEARCH:
				return getSearchEffect(effectStack);
			default:
				return null;
		}
	}
	
	private static String getTarget(Stack<String> effectStack){
		effectStack.pop();	// target
		String target = effectStack.pop();
		if(target.contains("choice")) {
			target += ":"+effectStack.pop();
		}
		return target;
	}
	
	private static Condition getConditionEffect(Stack<String> effectStack){
		Condition cond = null;
		String type = effectStack.pop().toUpperCase().replace("(", "");
		if(type.contains("COUNT"))
			return null;
		switch(ConditionTypes.valueOf(type)){
		case FLIP:
			cond = new FlipCondition();
			break;
		case HEALED:
			return null;
		case ABILITY:
			cond = new AbilityCondition();
			String conditionAbilities = effectStack.pop();
			for(int i = 0; !effectStack.peek().contains("("); i++)
				conditionAbilities += ":"+effectStack.pop();
			((AbilityCondition)cond).setEffectCondition(ParseEfect(conditionAbilities));
			break;
		default:
			return null;
		}
		String conditionAbilities = effectStack.pop();
		int size = effectStack.size();
		for(int i = 0; i < size && !effectStack.peek().equals("else"); i++)
			conditionAbilities += ":"+effectStack.pop();
		conditionAbilities = conditionAbilities.replaceAll("[()]", "");
		// Divide effects separated by comma
    	Pattern p = Pattern.compile("(.+?(\\(.*?\\))*?)(?:,|$)");
    	Matcher m = p.matcher(conditionAbilities);
    	List<String> effects = new LinkedList<String>();
    	while(m.find())
    		effects.add(m.group( 1 ));
    	for(String effect : effects)
    		cond.addEffectTrue(ParseEfect(effect));
		if(!effectStack.isEmpty()){
			effectStack.pop();	// else
			conditionAbilities = effectStack.pop();
			size = effectStack.size();
			for(int i = 0; i < size; i++)
				conditionAbilities += ":"+effectStack.pop();
			conditionAbilities = conditionAbilities.replaceAll("[()]", "");
			// Divide effects separated by comma
	    	p = Pattern.compile("(.+?(\\(.*?\\))*?)(?:,|$)");
	    	m = p.matcher(conditionAbilities);
	    	effects = new LinkedList<String>();
	    	while(m.find())
	    	  effects.add(m.group( 1 ));
	    	for(String effect : effects)
	    		cond.addEffectFalse(ParseEfect(effect));
		}
		return cond;
	}
	
	private static Effect getDamageEffect(Stack<String> effectStack){
		String target = getTarget(effectStack);
		if(target == null)
			return null;
		// Count multiplier
		String countInfo = "";
		if(effectStack.peek().contains("count")) {
			while(!effectStack.isEmpty()) {
				String s = effectStack.pop();
				if(s.contains(")")) {
					// the end of count, append the last string and break out of the loop
					countInfo += s;
					break;
				}
				// reconstruct the count info
				countInfo += (s + ":");
			}
		}
		
		int damage = 0;
		if(countInfo != "") {
			String[] parsedCountInfo = countInfo.split("\\*");
			try {
				damage = Integer.parseInt(parsedCountInfo[0]);
				countInfo = parsedCountInfo[1];
			} catch(NumberFormatException ex) {
				damage = Integer.parseInt(parsedCountInfo[1]);
				countInfo = parsedCountInfo[0];
			}
			
			countInfo = countInfo.substring(countInfo.indexOf("(")+1,countInfo.indexOf(")"));

		} else {
			damage = Integer.parseInt(effectStack.pop());
		}
		return new Damage(target, damage, countInfo); 
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
	

	private static Effect getSwapEffect(Stack<String> effectStack) {
		effectStack.pop();    // source
		String swapSource = effectStack.pop();
		effectStack.pop();    // destination;
		String swapDestination = effectStack.pop();
		while (!effectStack.isEmpty()) {
			String s = effectStack.pop();
			swapDestination += (":" + s);
		}
		return new Swap(swapSource, swapDestination);
	}


	private static Effect getDeenergizeEffect(Stack<String> effectStack){
		int amount = 0;
		String target = "";
		effectStack.pop();	// target
		
		if(effectStack.size() == 2) {
			target = effectStack.pop();
			amount = Integer.parseInt(effectStack.pop());
			LOG.debug("Simple Deenergize Effect parsed");
		}
		
		return new Deenergize(amount, target);

	}
	
	private static String getStatus(Stack<String> effectStack){
		effectStack.pop();	// status
		return effectStack.pop();
	}
	
	private static String getConditionType(Stack<String> effectStack){
		effectStack.pop();	// cond
		return effectStack.pop();
	}
	
	private static Effect getSearchEffect(Stack<String> effectStack){
		Search searchEffect = new Search();
		String target = getTarget(effectStack);
		searchEffect.setTarget(target);
		String source = getSearchSource(effectStack);
		searchEffect.setSource(source);
		String filter = getSearchFilter(effectStack);
		searchEffect.setFilter(filter);
		String amount = getAmount(effectStack);
		searchEffect.setAmount(amount);
		return searchEffect;
	}
	
	private static String getSearchSource(Stack<String> effectStack){
		effectStack.pop();	// source
		return effectStack.pop();
	}
	
	private static String getSearchFilter(Stack<String> effectStack){
		if(effectStack.size() == 1)
			return "";	// No filter
		effectStack.pop();	// filter
		String filter = effectStack.pop();
		if(filter.equals("pokemon") || filter.equals("energy") || filter.equals("trainer")){
			if(effectStack.peek().equalsIgnoreCase("cat")){
				effectStack.pop();	// cat
				filter += ":" + effectStack.pop();	// card category
			}
		}else
			filter += ":" + effectStack.pop();	// card category
		return filter;
	}
	
	private static String getAmount(Stack<String> effectStack){
		return effectStack.pop();
	}
}

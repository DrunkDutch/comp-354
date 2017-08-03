package com.dmens.pokeno.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.condition.Condition;
import com.dmens.pokeno.condition.ConditionTypes;
import com.dmens.pokeno.condition.Flip;
import com.dmens.pokeno.condition.Healed;
import com.dmens.pokeno.effect.ApplyStatus;
import com.dmens.pokeno.effect.Damage;
import com.dmens.pokeno.effect.DeStat;
import com.dmens.pokeno.effect.DeckEffect;
import com.dmens.pokeno.effect.Deenergize;
import com.dmens.pokeno.effect.DrawCard;
import com.dmens.pokeno.effect.Effect;
import com.dmens.pokeno.effect.EffectTypes;
import com.dmens.pokeno.effect.Heal;
import com.dmens.pokeno.effect.Reenergize;
import com.dmens.pokeno.effect.ShuffleDeck;
import com.dmens.pokeno.effect.Search;
import com.dmens.pokeno.effect.Swap;

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
		
		Condition effectCondition = null;
		
		// If there is a condition, parse it
		if(EffectTypes.valueOf(effectStack.peek().toUpperCase()) == EffectTypes.COND)
		{
			effectCondition = getCondition(effectStack);
		}
		if (effectStack.isEmpty())
			return null;
		switch(EffectTypes.valueOf(effectStack.pop().toUpperCase()))
		{
			case DAM:
				return getDamageEffect(effectStack, effectCondition);
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
			case REENERGIZE:
				return getReenergizeEffect(effectStack);
			case SHUFFLE:
				return getShuffleDeckEffect(effectStack);
			case DECK:
				return getDeckEffect(effectStack);
			case DESTAT:
				return getDestatEffect(effectStack);
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
	
	private static Effect getDamageEffect(Stack<String> effectStack, Condition effectCondition){
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
		return new Damage(target, damage, effectCondition, countInfo); 
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
	
	private static Effect getDeckEffect(Stack<String> effectStack){
		effectStack.pop();	// target
		String target = effectStack.pop();
		effectStack.pop();	//destination
		String destination = effectStack.pop();
		String origin = "";
		if (effectStack.peek().contains("bottom")) {
			destination += ":" + effectStack.pop();
		}
		if (effectStack.peek().contains("count")) {
			origin = effectStack.pop();
		}
		//TODO - it might be "choice" instead of "count"
		
		//LOG.debug("Simple Shuffle Effect parsed");
		return new DeckEffect(target, origin, destination);
	}
	
	private static Effect getShuffleDeckEffect(Stack<String> effectStack){
		//TODO - conditionals need to be added (here and the obj)
		String target = "";
		effectStack.pop();	// target
		target = effectStack.pop();
		LOG.debug("Simple Shuffle Effect parsed");
		return new ShuffleDeck(target);
	}
	
	private static Effect getDestatEffect(Stack<String> effectStack) {
		effectStack.pop(); //target
		String target = effectStack.pop();
		return new DeStat(target);
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
		String countInfo = "";
		effectStack.pop();	// target
		
		System.out.println("Stack Size: " + effectStack.size());
			target = effectStack.pop();
			if (effectStack.size() == 1)
				amount = Integer.parseInt(effectStack.pop());
			else
			{
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
				if (countInfo != "")
					countInfo = countInfo.substring(countInfo.indexOf("(")+1,countInfo.indexOf(")"));
			}
			LOG.debug("Deenergize Effect parsed");
		return new Deenergize(amount, target, null, countInfo);

	}
	
	private static Effect getReenergizeEffect(Stack<String> effectStack){
		int amountS = 0;
		int amountD = 0;
		String source = "";
		String destination = "";
		
		effectStack.pop();	// target
		effectStack.pop();	// choice
		source = effectStack.pop();
		amountS = Integer.parseInt(effectStack.pop());
		
		effectStack.pop();	// target
		effectStack.pop();	// choice
		destination = effectStack.pop();
		amountD = Integer.parseInt(effectStack.pop());
		
		// Source and Destination amounts should be the same
		assert(amountS == amountD);
		
		return  new Reenergize(amountS, source, destination, null);
	}
	
	private static Condition getCondition(Stack<String> effectStack) {
		
		String cond = getConditionType(effectStack);
		
		if(cond.contains("count("))	// cannot parse the count(target yet
			cond = "count";
		
		switch(ConditionTypes.valueOf(cond.toUpperCase()))
		{
			case FLIP:
				// some flip conditions are proceeded by a pair of parenthesis
				if(effectStack.peek().contains("("))
				{
					while(!effectStack.pop().contains(")"));
					break;
				}
				else
				{
					LOG.debug("Simple Flip Condition parsed");
					return new Flip();
				}
			case HEALED:
				String target = getTarget(effectStack);
				return new Healed(target);
			case COUNT:
				while(!effectStack.pop().contains(")"));
				return null;
			default:
				return null;
		}
		return null;
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

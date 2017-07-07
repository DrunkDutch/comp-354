package com.dmens.pokeno.Ability;

import java.util.HashMap;

import com.dmens.pokeno.card.EnergyTypes;

public class AbilityCost {
	private Ability ability;
	private HashMap<EnergyTypes, Integer> costs;
	
	public AbilityCost(Ability ability){
		this.ability= ability;
		costs = new HashMap<EnergyTypes, Integer>();
	}
	
	public void addCost(EnergyTypes energy, int cost){
		if(costs.containsKey(energy))
			costs.replace(energy, costs.get(energy), costs.get(energy)+1);
		else
			costs.put(energy, cost);
	}

	public AbilityCost() {
		costs = new HashMap<EnergyTypes, Integer>();
	}

	public Ability getAbility() {
		return ability;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}

	public HashMap<EnergyTypes, Integer> getCosts() {
		return costs;
	}

	public void setCosts(HashMap<EnergyTypes, Integer> costs) {
		this.costs = costs;
	}
	
	public String showCosts(){
		StringBuilder sb = new StringBuilder();
		costs.forEach((energy, cost) ->{
			sb.append(energy + ": " + cost + "\n");
		});
		return sb.toString();
	}
}
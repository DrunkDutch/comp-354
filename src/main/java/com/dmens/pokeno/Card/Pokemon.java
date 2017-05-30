package com.dmens.pokeno.Card;

import java.util.ArrayList;
import com.dmens.pokeno.Ability.Ability;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Pokemon extends Card {

    private static final Logger LOG = LogManager.getLogger(Pokemon.class);

    private int HP;
    private int damage;
    private ArrayList<EnergyCard> attachedEnergy;
	private ArrayList<String> mCategories;
	private ArrayList<Ability> mAbilities;
	private int retreatCost;
	private String mBasePokemonName;
    private boolean poisoned;
    private boolean confused;
    private boolean paralyzed;
    private boolean sleep;

    private Pokemon(){}
    
	public Pokemon(String name, ArrayList<String> categories, int initialHP, int retreatCost, ArrayList<Ability> abilities){
		super(name);
	}
	
	public void AddCategory(String category)
	{
		this.mCategories.add(category);
	}
	
	public void AddAbility(Ability ability)
	{
		this.mAbilities.add(ability);
	}
	
	public String toString()
	{
		return String.format("|POKEMON CARD|\n|%s|\n", this.getName());
	}
	
	public void addDamage(int damage){}

	public void removeDamage(int damage){}

	public void addEnergy(EnergyCard energy){}

	public void removeEnergy(ArrayList<EnergyCard> energy){}

	public void setBasePokemonName(String basePokemonName) {
		assert basePokemonName != null;
		mBasePokemonName = basePokemonName;
	}
	
    public void setPoisoned(boolean poisoned) {
        this.poisoned = poisoned;
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
    }

    public void setParalyzed(boolean paralyzed) {
        this.paralyzed = paralyzed;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public int getHP() {
        return HP;
    }

    public int getDamage() {
        return damage;
    }

    public ArrayList<EnergyCard> getAttachedEnergy() {
        return attachedEnergy;
    }

    public ArrayList<String> getmCategories() {
        return mCategories;
    }

    public ArrayList<Ability> getmAbilities() {
        return mAbilities;
    }

    public int getRetreatCost() {
        return retreatCost;
    }

    public String getBasePokemonName() {
        return mBasePokemonName;
    }

    public boolean isPoisoned() {
        return poisoned;
    }

    public boolean isConfused() {
        return confused;
    }

    public boolean isParalyzed() {
        return paralyzed;
    }

    public boolean isSleep() {
        return sleep;
    }

    public void evolvePokemon(Pokemon basePokemon){
	    this.damage = basePokemon.getDamage();
    }
}

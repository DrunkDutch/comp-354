package com.dmens.pokeno.Card;

import java.util.ArrayList;
import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.Utils.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Pokemon extends Card {

    private static final Logger LOG = LogManager.getLogger(Pokemon.class);

    private int mHP;
    private int mDamage;
    private ArrayList<EnergyCard> mAttachedEnergy;
	private ArrayList<String> mCategories;
	private ArrayList<Ability> mAbilities;
    private ArrayList<Tuple<Ability, ArrayList<Integer>>> mAbilitiesAndCost;
	private int mRetreatCost;
	private String mBasePokemonName;
    private boolean mPoisoned;
    private boolean mConfused;
    private boolean mParalyzed;
    private boolean mSleep;

    private Pokemon(){}
    
	public Pokemon(String name, ArrayList<String> categories, int initialHP, int retreatCost, ArrayList<Ability> abilities){
		super(name);
		mCategories = categories;
		mHP = initialHP;
		mRetreatCost = retreatCost;
		mAbilities = abilities;
	}

	//This constructor is just here as a place holder until the parser is written to handle the new ability format
    private Pokemon(String name, ArrayList<String> categories, int initialHP, Integer retreatCost, ArrayList<Tuple<Ability, ArrayList<Integer>>> abilities){
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
        this.mPoisoned = poisoned;
    }

    public void setConfused(boolean confused) {
        this.mConfused = confused;
    }

    public void setParalyzed(boolean paralyzed) {
        this.mParalyzed = paralyzed;
    }

    public void setSleep(boolean sleep) {
        this.mSleep = sleep;
    }

    public int getHP() {
        return mHP;
    }

    public int getDamage() {
        return mDamage;
    }

    public ArrayList<EnergyCard> getAttachedEnergy() {
        return mAttachedEnergy;
    }

    public ArrayList<String> getCategories() {
        return mCategories;
    }

    public ArrayList<Ability> getAbilities() {
        return mAbilities;
    }

    public int getRetreatCost() {
        return mRetreatCost;
    }

    public String getBasePokemonName() {
        return mBasePokemonName;
    }

    public boolean isPoisoned() {
        return mPoisoned;
    }

    public boolean isConfused() {
        return mConfused;
    }

    public boolean isParalyzed() {
        return mParalyzed;
    }

    public boolean isSleep() {
        return mSleep;
    }

    public void evolvePokemon(Pokemon basePokemon){
	    this.mDamage = basePokemon.getDamage();
    }
}

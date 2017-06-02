package com.dmens.pokeno.Card;

import java.util.ArrayList;
import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.Driver.Driver;
import com.dmens.pokeno.Effect.Effect;
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
    private ArrayList<Tuple<Ability, String, Integer>> mAbilitiesAndCost;
	private int mRetreatCost;
	private String mBasePokemonName;
    private boolean mPoisoned;
    private boolean mConfused;
    private boolean mParalyzed;
    private boolean mSleep;
    
    public Pokemon(String name, ArrayList<String> categories, int initialHP, Integer retreatCost){
        super(name);
        mCategories = categories;
        mHP = initialHP;
        mRetreatCost = retreatCost;
        mAbilities = new ArrayList<Ability>();
        mAttachedEnergy = new ArrayList<EnergyCard>();
        mAbilitiesAndCost = new ArrayList<Tuple<Ability, String, Integer>>();
    }
	
	public void AddCategory(String category)
	{
		this.mCategories.add(category);
	}
	
	public void AddAbilityAndCost(Tuple<Ability, String, Integer> tuple)
	{
		this.mAbilitiesAndCost.add(tuple);
	}

	public String toString()
	{
		StringBuilder abilitiesAsList = new StringBuilder();
		for (Tuple<Ability, String, Integer> tuple: mAbilitiesAndCost)
		{
			abilitiesAsList.append(String.format("--T:%s C:%d\t",  tuple.y, tuple.z) + tuple.x.toString() + "\n");
		}
		
		return String.format("%s:\t\tNAME: %s\n%s", Pokemon.class, this.getName(), abilitiesAsList.toString());
	}
	
	public boolean addDamage(int damage)
        {
            mDamage += damage;
            /*if (mDamage >= mHP)
            {
                return true;
            }*/
            //if damage > hp -> "faint"
            return false;
        }

	public void removeDamage(int damage){}

	public void addEnergy(EnergyCard energy){
		mAttachedEnergy.add(energy);
	}

	public void removeEnergy(ArrayList<EnergyCard> energy){
		
	}

	public void setBasePokemonName(String basePokemonName) {
		assert basePokemonName != null;
		mBasePokemonName = basePokemonName;
	}
	
        public boolean useAbility(int ability, Pokemon target)
        {
            //TODO - if we have enough energy
            Ability a = mAbilities.get(ability);
            target.addDamage(a.getDamageEffect().getValue());
            return true;
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

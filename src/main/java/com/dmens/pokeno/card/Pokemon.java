package com.dmens.pokeno.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.ability.AbilityCost;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.effect.*;
import com.dmens.pokeno.services.CountService;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import com.dmens.pokeno.utils.Randomizer;
import com.dmens.pokeno.condition.*;

public class Pokemon extends Card {

    private static final Logger LOG = LogManager.getLogger(Pokemon.class);

    private int mHP;
    private int mDamage;
    private ArrayList<EnergyCard> mAttachedEnergy;
	private String mCategory;
	private String mPokemonType;
    private ArrayList<AbilityCost> mAbilitiesAndCost;
	private int mRetreatCost;
    private boolean mPoisoned;
    private boolean mParalyzed;
    private boolean mSleep;
    private boolean mStuck;
    private boolean mHealed;
    
    // Stage-one attributes
    private Pokemon mBaseCardReference;
    private String mBasePokemonName;
    
    public Pokemon(String name){
    	super(name);
    	mAttachedEnergy = new ArrayList<EnergyCard>();
        mAbilitiesAndCost = new ArrayList<AbilityCost>();
        mRetreatCost = -1;
    }
    
    public Pokemon(String name, String category, int initialHP, Integer retreatCost){
        super(name);
        mCategory = category;
        mHP = initialHP;
        mRetreatCost = retreatCost;
        mAttachedEnergy = new ArrayList<EnergyCard>();
        mAbilitiesAndCost = new ArrayList<AbilityCost>();
    }

	public void setHP(int mHP) {
		this.mHP = mHP;
	}

	public String getPokemonType() {
		return mPokemonType;
	}

	public void setPokemonType(String mType) {
		this.mPokemonType = mType;
	}

	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public void setmRetreatCost(int mRetreatCost) {
		this.mRetreatCost = mRetreatCost;
	}
	
	public void AddAbilityAndCost(AbilityCost abilityCost)
	{
		this.mAbilitiesAndCost.add(abilityCost);
	}

	public int getDamageCounter() {
		return this.mDamage / 10;
	}
	
	public String toString()
	{
		StringBuilder abilitiesAsList = new StringBuilder();		
		return String.format("%s\n%s", this.getName(), abilitiesAsList.toString());
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
	
	public boolean isFainted(){
		return (mDamage >= mHP);
	}

	public void removeDamage(int damageToRemove) {
		if(damageToRemove < 0) {
			return;
		}
		
		if(damageToRemove > mDamage) {
			mDamage = 0;
			setHealed(true);
			return;
		}
		mDamage -= damageToRemove;
		setHealed(true);
		LOG.debug("here");
		TargetServiceHandler.getInstance().getService().getPlayer("your-active").updateActivePokemonOnBoard();
	}

	public void addEnergy(EnergyCard energy){
		mAttachedEnergy.add(energy);
	}

	public void removeEnergy(ArrayList<EnergyCard> energy){
		
	}

	public void setBasePokemonName(String basePokemonName) {
		mBasePokemonName = basePokemonName;
	}
	
    public boolean useAbility(int ability, Pokemon target)
    {
        if (mAbilitiesAndCost.size() <= ability || target == null)
            return false;
        Ability a = mAbilitiesAndCost.get(ability).getAbility();//mAbilities.get(ability);
        HashMap <EnergyTypes, Integer> cost = mAbilitiesAndCost.get(ability).getCosts();
        
        // Print Energy Cost
        /*for (EnergyTypes e : cost.keySet())
        {
        	System.out.println(e.name() + ": " + cost.get(e));
        }*/
        
        ArrayList<Integer> energyCounts = getAttachedEnergyList();
        int remainingEnergyCount = 0;
        for (int count : energyCounts)
        {
            remainingEnergyCount += count;
        }
        
        boolean hasEnoughEnergy = true;
        if(cost.containsKey(EnergyTypes.FIGHT))
        {
            if (cost.get(EnergyTypes.FIGHT) <= energyCounts.get(0))
                remainingEnergyCount -= cost.get(EnergyTypes.FIGHT);
            else
                hasEnoughEnergy = false;
        }
        if(cost.containsKey(EnergyTypes.LIGHTNING))
        {
            if (cost.get(EnergyTypes.LIGHTNING) <= energyCounts.get(1))
                remainingEnergyCount -= cost.get(EnergyTypes.LIGHTNING);
            else
                hasEnoughEnergy = false;
        }

        if(cost.containsKey(EnergyTypes.PSYCHIC))
        {
            if (cost.get(EnergyTypes.PSYCHIC) <= energyCounts.get(2))
                remainingEnergyCount -= cost.get(EnergyTypes.PSYCHIC);
            else
                hasEnoughEnergy = false;
        }
        if(cost.containsKey(EnergyTypes.WATER))
        {
            if (cost.get(EnergyTypes.WATER) <= energyCounts.get(3))
                remainingEnergyCount -= cost.get(EnergyTypes.WATER);
            else
                hasEnoughEnergy = false;
        }
        if (cost.containsKey(EnergyTypes.COLORLESS) && cost.get(EnergyTypes.COLORLESS) > remainingEnergyCount)
            hasEnoughEnergy = false;
        
        if (hasEnoughEnergy)
        {
        	// iterate through all effect of an ability
        	a.getEffects().forEach(effect ->
        	{
        		boolean proceedWithAttack = true;
       
        		// Does the condition allow you to use the attack (flipped in your favor)
        		if(effect.hasCondition())
        		{
        			if(effect.getCondition() instanceof Flip)
        			{
        				if(Randomizer.Instance().getFiftyPercentChance())
    					{
    						proceedWithAttack = false;
    						displayMessage(target.getName() + " avoided the attack!");
    					}
        			}
        			else if(effect.getCondition() instanceof Healed)
        			{
        				if(!isHealed())
        					proceedWithAttack = false;
        			}
        		}
  
        		if(proceedWithAttack)
        		{
        			if(effect instanceof Deenergize)
        			{
        				effect.execute();
        			}
        			else if(effect instanceof DrawCard)
        			{
        				effect.execute();
        			}
        			else if(effect instanceof ApplyStatus)
            		{
            			ApplyStatus as = (ApplyStatus) effect;
            			String status = as.getStatus();
            			
            			//TODO check target
                		// target should be used instead of !GameController.getIsHomePlayerPlaying()
                		if(status.compareTo("paralyzed") == 0)
            			{
            				if (!target.isParalyzed())
							{
								target.setParalyzed(true);
								LOG.debug((!GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + target.getName() + " has been set to Paralyzed.");
								displayMessage(target.getName() + " has been paralyzed!");
								GameController.board.addStatus(0, !GameController.getIsHomePlayerPlaying());
							}
							else
							{
            					displayMessage("Target is already paralyzed");
							}
            			}
                		else if (status.compareTo("asleep") == 0)
                		{
                			if (!target.isSleep())
							{
								target.setSleep(true);
								LOG.debug((!GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + target.getName() + " has been set to Sleep.");
								displayMessage(target.getName() + " has fallen asleep!");
								GameController.board.addStatus(1, !GameController.getIsHomePlayerPlaying());
							}
							else
							{
                				displayMessage("Target is already asleep");
							}
						}
                		else if (status.compareTo("stuck") == 0 && !target.isStuck())
                		{
                			target.setStuck(true);
                			LOG.debug((!GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + target.getName() + " has been set to Stuck.");
                			displayMessage(target.getName() + " is now stuck!");
                			GameController.board.addStatus(2, !GameController.getIsHomePlayerPlaying());
                		}
                		else if (status.compareTo("poisoned") == 0 && !target.isPoisoned())
                		{
                			target.setPoisoned(true);
                			LOG.debug((!GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + target.getName() + " has been set to Poisoned.");
                			displayMessage(target.getName() + " is now poisoned!");
                			GameController.board.addStatus(3, !GameController.getIsHomePlayerPlaying());
                		}
            		}
            		else if (effect instanceof Damage)
                	{
            			Damage dam = (Damage) effect;
            			dam.execute();
                	}
            		else if (effect instanceof Heal)
            		{
            			Heal h = (Heal) effect;
            			
            			String targetCheck = h.getTarget();
            			
            			if(targetCheck.contentEquals("your-active"))
            			{
            				Pokemon youAsTarget = GameController.getIsHomePlayerPlaying() ? GameController.getActivePlayer().getActivePokemon() : GameController.getAIPlayer().getActivePokemon();
            				LOG.debug((GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + youAsTarget.getName() + " has been healed by " + h.getValue() + ".");
            				youAsTarget.removeDamage(h.getValue());
            			}
            		}
        		}
        	});
        	
        return true;
        }
        return false;
    }
    
    public boolean removeSingleEnergy(EnergyTypes type)
	{
		System.out.println(type);
		for (int i = 0; i < mAttachedEnergy.size(); i++)
		{
			if (mAttachedEnergy.get(i).getCategory() == type)
			{
				mAttachedEnergy.remove(i);
				return true;
			}
		}
		return false;
	}
    
    public void setPoisoned(boolean poisoned) {
        this.mPoisoned = poisoned;
    }

    public void setParalyzed(boolean paralyzed) {
        this.mParalyzed = paralyzed;
    }

    public void setSleep(boolean sleep) {
        this.mSleep = sleep;
    }
    
    public void setStuck(boolean stuck) {
    	this.mStuck = stuck;
    }
    
    public void setHealed(boolean healed) {
    	this.mHealed = healed;
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
    public int getRetreatCost() {
    	// Evolved Pokemon can get retreat cost from base or define it
    	if(this.isEvolvedCategory() && this.mRetreatCost == -1)
    		return ((Pokemon)((CardsDatabase)CardsDatabase.getInstance()).queryByName(this.mBasePokemonName)).getRetreatCost();
        return mRetreatCost;
    }

    public String getBasePokemonName() {
        return mBasePokemonName;
    }

    public boolean isPoisoned() {
        return mPoisoned;
    }

    public boolean isParalyzed() {
        return mParalyzed;
    }

    public boolean isSleep() {
        return mSleep;
    }
    
    public boolean isStuck() {
    	return mStuck;
    }
    
    public boolean isHealed() {
    	return mHealed;
    }
    
    public boolean isEvolvedCategory(){
    	if(this.mCategory.equalsIgnoreCase("stage-one") || this.mCategory.equalsIgnoreCase("stage-two"))
    		return true;
    	return false;
    }
    /**
     * 
     * @param basePokemon base type pokemon to evolve from
     * @return True if evolution was successful, false otherwise
     */
    public boolean evolvePokemon(Pokemon basePokemon){
    	if(basePokemon.getName().equalsIgnoreCase(this.getBasePokemonName())){
 		    this.mDamage = basePokemon.getDamage();
		    // transfer energy
		    transferEnergy(basePokemon);
		    //  keep base reference for discard
		    mBaseCardReference = basePokemon;
		    return true;
    	}else{
    		return false;
    	}
    }
    
    public Pokemon getBaseCardReference(){
    	return mBaseCardReference;
    }
    
    private void transferEnergy(Pokemon base){
    	this.mAttachedEnergy = base.getAttachedEnergy();
    }
    
    public ArrayList<AbilityCost> getAbilitiesAndCost(){
        return mAbilitiesAndCost;
    }
    
    public Map<EnergyTypes, Integer> getTotalEnergyNeeds(){
    	Map<EnergyTypes, Integer> totalCosts = new HashMap<EnergyTypes, Integer>();
    	this.mAbilitiesAndCost.forEach(abilityCost ->{
    		abilityCost.getCosts().forEach((energy, cost) ->{
    			if(totalCosts.containsKey(energy))
    				totalCosts.put(energy, cost + totalCosts.get(energy));
    			else
    				totalCosts.put(energy, cost);
    		});
    	});
    	this.mAttachedEnergy.forEach(energyAttached ->{
    		if(totalCosts.containsKey(energyAttached.getCategory())){
    			totalCosts.put(energyAttached.getCategory(), totalCosts.get(energyAttached.getCategory()) - 1);
    		}else if(totalCosts.containsKey(EnergyTypes.COLORLESS)){
    			totalCosts.put(EnergyTypes.COLORLESS, totalCosts.get(EnergyTypes.COLORLESS) - 1);
    		}
    	});
    	return totalCosts;
    }
    
    public Map<EnergyTypes, Integer> getMapOfAttachedEnergies(){
        Map<EnergyTypes, Integer> energies = new HashMap<EnergyTypes, Integer>();
        this.mAttachedEnergy.forEach(energyAttached ->{
    		if(energies.containsKey(energyAttached.getCategory())){
    			energies.put(energyAttached.getCategory(), energies.get(energyAttached.getCategory()) + 1);
    		}else{
    			energies.put(energyAttached.getCategory(), 1);
    		}
    	});
        return energies;
    }
    
    public ArrayList<Integer> getAttachedEnergyList(){
    	Map<EnergyTypes, Integer> energies = getMapOfAttachedEnergies();
        ArrayList<Integer> energyList = new ArrayList<Integer>(5);
        energyList.add(0);energyList.add(0);energyList.add(0);energyList.add(0);energyList.add(0);
    	energies.forEach((energyType, amount) -> {
    		switch(energyType){
    		case FIGHT:
                    energyList.set(0, amount);
                    break;
    		case LIGHTNING:
                    energyList.set(1, amount);
                    break;
    		case PSYCHIC:
                    energyList.set(2, amount);
                    break;
    		case WATER:
                    energyList.set(3, amount);
                    break;
    		case COLORLESS:
                    energyList.set(4, amount);
                    break;
    		case FIRE:
                    break;
    		case GRASS:
                    break;
    		}
    	});
        return energyList;
    }
	public void displayMessage(String message){
		GameController.displayMessage(message);
	}
    @Override
	public boolean isType(CardTypes c) {
		return (c == CardTypes.POKEMON) ? true : false;
	}

	@Override
	public CardTypes getType() {
		return CardTypes.POKEMON;
	}
	
	@Override
	public Card copy() {
		Pokemon p = new Pokemon(this.getName(), this.mCategory, this.getHP(), this.getRetreatCost());
		p.setBasePokemonName(this.getBasePokemonName());
		this.getAbilitiesAndCost().forEach(abilityCost->{
			p.AddAbilityAndCost(abilityCost);
		});
		return p;
	}
}

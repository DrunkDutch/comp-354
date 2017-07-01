package com.dmens.pokeno.Card;

import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.Effect.Effect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TrainerCard extends Card {

    private static final Logger LOG = LogManager.getLogger(TrainerCard.class);

	private String mCategory;
	private Ability mAbility;

	public TrainerCard(String name, String category, Ability ability)
	{
		super(name);
		this.mCategory = category;
		this.mAbility = ability;
	}
	
	public String getCategory() {
		return this.mCategory;
	}
	
	public Ability getAbility() {
		return this.mAbility;
	}

	public String toString()
	{
		return String.format("%s:\t\tNAME: %s\t\tCAT:%s\n%s", TrainerCard.class, this.getName(), this.mCategory, mAbility);
	}
	
	@Override
	public boolean isType(CardTypes c) {
		return (c == CardTypes.TRAINER) ? true : false;
	}
	
	public void use() {
		mAbility.performAbility();
	}

	@Override
	public CardTypes getType() {
		return CardTypes.TRAINER;
	}

	@Override
	public Card copy() {
		return new TrainerCard(this.getName(), this.getCategory(), this.getAbility());
	}
}

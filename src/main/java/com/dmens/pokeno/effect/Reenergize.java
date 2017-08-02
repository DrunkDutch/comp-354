package com.dmens.pokeno.effect;

import com.dmens.pokeno.condition.Condition;

/**
 * 	reenergize:[target:source]:[target:destination]:[amount]
 *	move energy from one pokemon to another, [amount] times
 * @author James
 *
 */
public class Reenergize extends Effect {

	private int mAmount;
	private String mSource;
	private String mDestination;
	
	/*
	 * Constructor
	 * 
	 * @param		target			String.
	 * @param		destination		String.
	 */
	public Reenergize(int amt, String src, String dst, Condition con) {
		super("", con);
		this.mAmount = amt;
		this.mSource = src;
		this.mDestination = dst;
	}
	
	public int getAmount() {
		return this.mAmount;
	}

	@Override
	public void execute() {

	}

	@Override
	public String toString() {
		return String.format("Reenergize: Src: %s, Dst: %s, Amt: %d", this.mSource, this.mDestination, this.mAmount);
	}
}

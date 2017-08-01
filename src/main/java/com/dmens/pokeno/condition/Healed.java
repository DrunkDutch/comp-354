package com.dmens.pokeno.condition;

public class Healed implements Condition {

	private String mName;
	private String mTarget;
	
	public Healed(String tar) {
		this.mName = "Healed";
		this.mTarget = tar;
	}
	
	@Override
	public String toString()
	{
		return String.format("CONDITION: %s - target: %s", this.mName, this.mTarget);
	}
}

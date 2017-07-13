package com.dmens.pokeno.condition;


public class Flip implements Condition {

	private String mName;
	
	public Flip() {
		this.mName = "Flip";
	}
	
	@Override
	public String toString()
	{
		return String.format("CONDITION: %s", this.mName);
	}
}

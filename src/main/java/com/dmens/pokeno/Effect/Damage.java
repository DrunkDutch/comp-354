package com.dmens.pokeno.Effect;

public class Damage implements Effect {

	private int mValue;
	private String mTarget;
	
	public Damage(String tar, int val)
	{
		this.mTarget = tar;
		this.mValue = val;		
	}

	@Override
	public void Execute() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GetInformation()
	{
		// TODO Auto-generated method stub
		
	}	
}
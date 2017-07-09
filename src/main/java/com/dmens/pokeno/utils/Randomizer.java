package com.dmens.pokeno.utils;

import java.util.ArrayList;
import java.util.Collections;

/* 
 * we need Randomizer anyways and sure
 * https://stackoverflow.com/questions/15285643/how-to-get-a-50-50-chance-in-random-generator/15285659
 */
public class Randomizer {
	
	private static ArrayList<Integer> mfifty;
	
	private static Randomizer instance = null;
	private static boolean mIntialized = false;

	private Randomizer() {}
	
	public static Randomizer Instance( ) {
		if(instance == null)
		{
			instance = new Randomizer();
		}
		
		return instance;
	}
	
	private void initialize()
	{
		mfifty = new ArrayList<Integer>();
		for(int i = 0; i < 50; i++)
		{
			mfifty.add(0);
			mfifty.add(1);
		}
		
		mIntialized = true;
	}
	
	public boolean getFiftyPercentChance()
	{
		if(!mIntialized)
			initialize();
		
		Collections.shuffle(mfifty);
		
		return (mfifty.get(0) == 0) ? true : false;
	}
	
	//TODO multi-flip methods
	
	//TODO: more random num shenanigans goes here
}
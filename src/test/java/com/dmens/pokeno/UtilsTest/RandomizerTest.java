package com.dmens.pokeno.UtilsTest;

import org.junit.Assert;
import org.junit.Test;

import com.dmens.pokeno.utils.Randomizer;

/**
*
* @author James
*/
public class RandomizerTest {
	
	static int mDesiredResults = 50;
	static double mThreshold = 0.10;
	static int mFlips = mDesiredResults * 2;
	
	//TODO static int mFlipRounds = 10;

	@Test
	public void Randomizer50Test() {
		
		Randomizer rando = null;
		rando = Randomizer.Instance();
		Assert.assertNotEquals(rando, null);
		
		int headsCount = 0;
		int tailsCount = 0;
		
		//TODO? loop this again and take an average?!
		
		for(int i = 0; i < mFlips; i++)
		{
			if(rando.getFiftyPercentChance())
				headsCount++;
			else
				tailsCount++;
		}
		
		// Check that there's nothing savage...
		Assert.assertEquals(headsCount + tailsCount , mFlips);
		
		// acceptable range is within 10 of desired results
		System.out.println("RandomizerTest-Heads: " + headsCount);
		System.out.println("RandomizerTest-Tails: " + tailsCount);
		Assert.assertTrue((mDesiredResults - (int) (mFlips * mThreshold )) <= tailsCount && tailsCount <= (mDesiredResults + (int) (mFlips * mThreshold )));
		Assert.assertTrue((mDesiredResults - (int) (mFlips * mThreshold )) <= headsCount && headsCount <= (mDesiredResults + (int) (mFlips * mThreshold )));
	}

}
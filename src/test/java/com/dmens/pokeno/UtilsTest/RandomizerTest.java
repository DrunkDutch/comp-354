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
	static double mThreshold = 5;
	static int mFlips = mDesiredResults * 2;
	
	static int mFlipRounds = 10;

	@Test
	public void Randomizer50Test() {
		
		Randomizer rando = null;
		rando = Randomizer.Instance();
		Assert.assertNotEquals(rando, null);
		
		
		float headsAvg = 0;
		float tailsAvg = 0;
		
		for(int j = 0; j < mFlipRounds; j++)
		{
			int headsCount = 0;
			int tailsCount = 0;
			
			for(int i = 0; i < mFlips; i++)
			{
				if(rando.getFiftyPercentChance())
					headsCount++;
				else
					tailsCount++;
			}
			headsAvg += headsCount;
			tailsAvg += tailsCount;
			
			// Check that there's nothing savage...
			Assert.assertEquals(headsCount + tailsCount , mFlips);
		}
		
		headsAvg /= mFlipRounds;
		tailsAvg /= mFlipRounds;
		
		
		// Acceptable range is within 5 of desired results
		System.out.println("RandomizerTest-HeadsAvg: " + headsAvg);
		System.out.println("RandomizerTest-TailsAvg: " + tailsAvg);
		Assert.assertTrue((mDesiredResults - mThreshold) <= headsAvg && headsAvg <= (mDesiredResults + mThreshold));
		Assert.assertTrue((mDesiredResults - mThreshold) <= tailsAvg && tailsAvg <= (mDesiredResults + mThreshold));
	}
}
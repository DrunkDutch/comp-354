package com.dmens.pokeno.effect;

import com.dmens.pokeno.services.CountService;

public class EffectAmount {
	private String mAmount;
	public EffectAmount(String amount){	
		this.mAmount = amount;
	}
	
	public int eval(){
		try{
			int value = Integer.parseInt(mAmount);
			return value;
		}catch(Exception e){
			final String[] SUPPORTED_OPPERATIONS = {"+", "-", "*", "/"};
			String amount = this.mAmount;
			for(int i = 0; i < SUPPORTED_OPPERATIONS.length; i++){
				String opperation = SUPPORTED_OPPERATIONS[i];
				String[] binary = amount.split(opperation);
				if(binary.length == 2){
					int[] binaryValues = new int[2];
					for(int j = 0; j < SUPPORTED_OPPERATIONS.length; j++){
						String part = binary[j];
						if(part.contains("count") || part.contains("COUNT"))
							binaryValues[j] = CountService.getInstance().getCount(part.trim());
						else
							binaryValues[j] = Integer.parseInt(part.trim());
					}
					switch(i){
						case 0:
							return binaryValues[0] + binaryValues[1];
						case 1:
							return binaryValues[0] - binaryValues[1];
						case 2:
							return binaryValues[0] * binaryValues[1];
						case 3:
							return binaryValues[0] / binaryValues[1];
					}
				}
			}
			if(amount.contains("count") || amount.contains("COUNT"))
				return CountService.getInstance().getCount(amount);
			else
				return 0;
		}
	}

}

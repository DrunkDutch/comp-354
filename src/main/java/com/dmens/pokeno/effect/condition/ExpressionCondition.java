package com.dmens.pokeno.effect.condition;

import com.dmens.pokeno.effect.Condition;
import com.dmens.pokeno.services.CountService;

public class ExpressionCondition extends Condition {
	private String mCondition;
	
	public void setCondition(String condition){
		this.mCondition = condition;
	}
	public void execute(){
		if(eval())
			execute(true);
		else
			execute(false);
	}
	
	public boolean eval(){
		final String[] SUPPORTED_OPPERATIONS = {"<", ">"};
		String condition = this.mCondition;
		for(int i = 0; i < SUPPORTED_OPPERATIONS.length; i++){
			String opperation = SUPPORTED_OPPERATIONS[i];
			String[] binary = condition.split(opperation);
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
					return binaryValues[0] < binaryValues[1];
				case 1:
					return binaryValues[0] > binaryValues[1];
				default:
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.dmens.pokeno.database;

import java.util.ArrayList;
import java.util.List;

import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.utils.AbilityParser;
import com.dmens.pokeno.utils.FileUtils;

public class AbilitiesDatabase extends Database<Ability>{
	private static Database<Ability> database;
	
	public static Database<Ability> getInstance(){
		if(database == null)
			database = new AbilitiesDatabase();
		return database;
	}
	
	private AbilitiesDatabase(){
		db = new ArrayList<Ability>();
	}

	public void initialize(String ablitiesFilePath){
		List<String> list = FileUtils.getFileContentsAsList(ablitiesFilePath);
		list.forEach(line -> {
			System.out.println(line);
			db.add(AbilityParser.getAbilityFromString(line));
		});
	}
}

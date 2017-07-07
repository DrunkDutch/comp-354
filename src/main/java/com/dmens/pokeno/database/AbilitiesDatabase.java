package com.dmens.pokeno.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.utils.AbilityParser;
import com.dmens.pokeno.utils.FileUtils;

public class AbilitiesDatabase extends Database<Ability>{
	private static Database<Ability> database;
	
	private static final Logger LOG = LogManager.getLogger(AbilitiesDatabase.class);
	
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
			LOG.trace("Abilities DB: "+line);
			db.add(AbilityParser.getAbilityFromString(line));
		});
	}
}

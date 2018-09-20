package com.piggest.minecraft.bukkit.myad;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

public class Language extends Myad_component {
	private HashMap<String, String> language;

	public Language(Myad myad) {
		super(myad);
		this.language = new HashMap<String, String>();
	}

	public String get(String key) {
		return language.get(key);
	}

	public void load_language(ConfigurationSection lang_config) {
		Set<String> lang_key_set = lang_config.getKeys(false);
		for (String key : lang_key_set) {
			this.language.put(key, lang_config.getString(key));
		}
		myad.getLogger().info("加载语言配置");
	}
}

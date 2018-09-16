package com.piggest.minecraft.bukkit.myad;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

public class Ad_publisher {
	private HashSet<Ad> ad_set;
	private Myad myad = null;
	
	public Myad get_plugin() {
		return this.myad;
	}
	
	public Ad_publisher(Myad myad) {
		this.myad = myad;
		this.ad_set = new HashSet<Ad>();
	}

	public void publish(String str) {
		this.myad.getServer().broadcastMessage(str);
	}

	public void stop() {
		for (Ad ad : ad_set) {
			ad.stop();
		}
		save_ads();
	}
	
	public void load_ads(ConfigurationSection ads) {
		Set<String> player_name_set = ads.getKeys(false);
		for(String player_name : player_name_set) {
			ConfigurationSection ad_config = ads.getConfigurationSection(player_name);
			Ad ad = new Ad(this);
			ad.set_player_name(player_name);
			ad.set_cycle(ad_config.getInt("cycle"));
			ad.set_last_times(ad_config.getInt("last_times"));
			ad.set_contents(ad_config.getString("contents"));
			this.ad_set.add(ad);
		}
	}
	
	public void save_ads() {
		MemoryConfiguration ads = new MemoryConfiguration();
		for(Ad ad:ad_set) {
			MemoryConfiguration ad_config = new MemoryConfiguration();
			ad_config.set("cycle", ad.get_cycle());
			ad_config.set("last_times", ad.get_last_times());
			ad_config.set("contents", ad.get_contents());
			ads.set(ad.get_player_name(), ad_config);
		}
		myad.getConfig().set("ads", ads);
		myad.saveConfig();
	}
	
	public void start() {
		for (Ad ad : ad_set) {
			ad.start();
		}
	}
	
	public void add_ad(Ad ad){
		this.ad_set.add(ad);
	}
}

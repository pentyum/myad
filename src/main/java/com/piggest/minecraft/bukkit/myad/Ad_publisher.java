package com.piggest.minecraft.bukkit.myad;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

public class Ad_publisher extends Myad_component {
	private HashSet<Ad> ad_set;

	public Ad_publisher(Myad myad) {
		super(myad);
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
		for (String player_name : player_name_set) {
			ConfigurationSection ad_config = ads.getConfigurationSection(player_name);
			Ad ad = new Ad(this);
			ad.set_player_name(player_name);
			ad.set_cycle(ad_config.getInt("cycle"));
			ad.set_last_times(ad_config.getInt("last_times"));
			ad.set_contents(ad_config.getString("contents"));
			this.ad_set.add(ad);
		}
		myad.getLogger().info("加载广告配置");
	}

	public void save_ads() {
		MemoryConfiguration ads = new MemoryConfiguration();
		for (Ad ad : ad_set) {
			MemoryConfiguration ad_config = new MemoryConfiguration();
			ad_config.set("cycle", ad.get_cycle());
			ad_config.set("last_times", ad.get_last_times());
			ad_config.set("contents", ad.get_contents());
			ads.set(ad.get_player_name(), ad_config);
		}
		myad.getConfig().set("ads", ads);
		myad.getLogger().info("保存广告配置");
		myad.saveConfig();
	}

	public void start() {
		for (Ad ad : ad_set) {
			ad.start();
		}
	}

	public String get_all_info() {
		String info = "查看所有广告信息";
		for (Ad ad : ad_set) {
			info += "\n----------------\n";
			info += ad.toString();
		}
		return info;
	}

	public Ad get_ad_by_player_name(String player_name) {
		for (Ad ad : ad_set) {
			if (ad.get_player_name().equalsIgnoreCase(player_name)) {
				return ad;
			}
		}
		return null;
	}

	public boolean add_ad(Ad ad) {
		if (get_ad_by_player_name(ad.get_player_name()) == null) {
			myad.getLogger().info(
					"已添加" + ad.get_player_name() + "的广告(周期" + ad.get_cycle() + "秒，剩余次数" + ad.get_last_times() + ")");
			this.ad_set.add(ad);
			return true;
		} else {
			return false;
		}
	}
}

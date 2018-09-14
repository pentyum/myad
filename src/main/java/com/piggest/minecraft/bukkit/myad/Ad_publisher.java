package com.piggest.minecraft.bukkit.myad;

import java.util.HashSet;

public class Ad_publisher {
	private HashSet<Ad> ad_set;
	private Myad myad = null;

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

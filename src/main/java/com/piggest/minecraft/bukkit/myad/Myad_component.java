package com.piggest.minecraft.bukkit.myad;

public abstract class Myad_component {
	protected Myad myad = null;

	public Myad get_plugin() {
		return this.myad;
	}

	public Myad_component(Myad myad) {
		this.myad = myad;
	}

}

package com.piggest.minecraft.bukkit.myad;

public enum Action {
	apply, info, contents, cycle, extend, list, forbidden;

	public static Action parse_action(String action_str) {
		for (Action action : Action.values()) {
			if (action == Action.forbidden) {
				continue;
			}
			if (action.name().equalsIgnoreCase(action_str)) {
				return action;
			}
		}
		return null;
	}

}

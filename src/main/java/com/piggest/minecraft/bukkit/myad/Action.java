package com.piggest.minecraft.bukkit.myad;

public enum Action {
	apply, info, contents, cycle, extend, list, forbidden;

	public static Action parse_action(String[] args) {
		if (args.length == 0) {
			return null;
		}
		for (Action action : Action.values()) {
			if (action == Action.forbidden) {
				continue;
			}
			if (action.name().equalsIgnoreCase(args[0])) {
				return action;
			}
		}
		return null;
	}

}

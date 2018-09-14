package com.piggest.minecraft.bukkit.myad;

public class Ad {
	private String player_name;
	private String contents;
	private int cycle;
	private boolean available = false;

	public static int parse_cycle(String cycle_string) {
		int length = cycle_string.length();
		String number = cycle_string.substring(0, length - 1);
		String unit = cycle_string.substring(length - 1, length);
		int cycle = 0;
		try {
			cycle = Integer.parseInt(number);
		} catch (Exception e) {
			return 0;
		}
		if (unit.equals("m")) {
			cycle = cycle * 60;
		} else if (unit.equals("h")) {
			cycle = cycle * 3600;
		}
		return cycle;
	}
}

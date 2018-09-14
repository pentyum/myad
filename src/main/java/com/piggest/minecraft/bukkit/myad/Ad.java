package com.piggest.minecraft.bukkit.myad;

public class Ad implements Runnable {
	private String player_name;
	private String contents;
	private int cycle;
	private Thread ad_thread = null;
	private Myad myad = null;

	public Ad(Myad myad) {
		this.myad = myad;
	}

	public Ad_publisher get_publisher() {
		return this.myad.get_publisher();
	}

	public void set_player_name(String player_name) {
		this.player_name = player_name;
	}

	public void set_contents(String contents) {
		this.contents = contents;
	}

	public void set_cycle(int cycle) {
		this.cycle = cycle;
	}

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

	public void run() {
		try {
			while (true) {
				this.publish();
				Thread.sleep(1000 * cycle);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void publish() {
		this.get_publisher().publish("[AD:" + this.player_name + "] " + this.contents);
	}

	public void start() {
		if (ad_thread == null) {
			myad.getLogger().info("启动广告发布线程");
			ad_thread = new Thread(this);
			ad_thread.start();
		}
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		myad.getLogger().info("停止广告发布线程");
		ad_thread.stop();
	}
}

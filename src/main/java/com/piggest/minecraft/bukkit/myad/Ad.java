package com.piggest.minecraft.bukkit.myad;

public class Ad implements Runnable {
	private String player_name;
	private String contents;
	private int cycle;
	private Thread ad_thread = null;
	private int last_times = 0;
	private Ad_publisher publisher = null;

	public Ad(Ad_publisher publisher) {
		this.publisher = publisher;
	}

	public Ad_publisher get_publisher() {
		return this.publisher;
	}

	public void set_player_name(String player_name) {
		this.player_name = player_name;
	}

	public String get_player_name() {
		return this.player_name;
	}

	public void set_contents(String contents) {
		this.contents = contents;
	}

	public String get_contents() {
		return this.contents;
	}

	public void set_cycle(int cycle) {
		this.cycle = cycle;
	}

	public int get_cycle() {
		return this.cycle;
	}

	public void set_last_times(int times) {
		this.last_times = times;
	}

	public int get_last_times() {
		return this.last_times;
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
				if (this.last_times <= 0) {
					break;
				}
				this.publish();
				this.last_times--;
				Thread.sleep(1000 * cycle);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		publisher.get_plugin().getLogger().info("广告次数已经用尽");
		publisher.get_plugin().getLogger().info("停止广告发布线程");
	}

	public void publish() {
		this.get_publisher().publish("[AD:" + this.player_name + "] " + this.contents);
	}

	public void start() {
		if (ad_thread == null) {
			publisher.get_plugin().getLogger().info("启动广告发布线程");
			ad_thread = new Thread(this);
			ad_thread.start();
		}
	}

	public void stop() {
		publisher.get_plugin().getLogger().info("停止广告发布线程");
		ad_thread.interrupt();
	}
}

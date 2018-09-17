package com.piggest.minecraft.bukkit.myad;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Myad extends JavaPlugin {
	private Economy economy = null;

	private FileConfiguration config = null;
	private int price = 0;
	private Ad_publisher publisher = null;

	private boolean initVault() {
		boolean hasNull = false;
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			if ((economy = economyProvider.getProvider()) == null) {
				hasNull = true;
			}
		}
		return !hasNull;
	}

	public Ad_publisher get_publisher() {
		return this.publisher;
	}

	public int get_price() {
		return this.price;
	}

	public ConfigurationSection get_ads() {
		return this.config.getConfigurationSection("ads");
	}

	public boolean check_out(CommandSender sender, int times) {
		if (!sender.hasPermission("myad.free")) {
			int total_price = this.price * times;
			Player player = (Player) sender;
			if (economy.has(player, total_price)) {
				economy.withdrawPlayer(player, total_price);
				player.sendMessage("已扣除" + total_price);
				return true;
			} else {
				player.sendMessage("你的金钱不够");
				return false;
			}
		}
		return true;
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		config = getConfig();
		if (!initVault()) {
			getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
			return;
		}
		this.price = config.getInt("price");
		getLogger().info("启动广告发布器");
		this.publisher = new Ad_publisher(this);
		this.publisher.load_ads(this.get_ads());
		this.publisher.start();
	}

	@Override
	public void onDisable() {
		getLogger().info("停止广告发布器");
		publisher.stop();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int cycle = 1;
		int times = 1;
		if (cmd.getName().equalsIgnoreCase("myad")) {
			if (args.length == 0) {
				sender.sendMessage("/myad apply|cycle|contents|extend");
				return true;
			}
			if (args[0].equalsIgnoreCase("apply")) { // 申请广告
				if (sender.hasPermission("myad.apply")) {
					if (args.length != 4) {
						sender.sendMessage("/myad apply <周期[h|m]> <次数> <内容>");
						return true;
					}
					try {
						times = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage("次数必须是整数");
						return true;
					}
					if ((cycle = Ad.parse_cycle(args[1])) == 0) {
						sender.sendMessage("周期格式不正确");
						return true;
					}
					if (check_out(sender, times) == false) {
						return true;
					}
					Ad ad = new Ad(this.publisher);
					ad.set_player_name(sender.getName());
					if (this.publisher.add_ad(ad)) {
						ad.set_contents(args[3]);
						ad.set_cycle(cycle);
						ad.set_last_times(times);
						ad.start();
						sender.sendMessage("广告申请成功");
					} else {
						sender.sendMessage(
								"你已经申请过广告了，输入/myad cycle <周期> 来调整周期，输入/myad contents <内容> 来调整内容，输入/myad extend <次数> 来增加次数");
					}
				} else {
					sender.sendMessage("你没有权限申请广告");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("cycle")) {
				if (args.length != 2) {
					sender.sendMessage("/myad cycle <周期[h|m]>");
					return true;
				}
				if (sender.hasPermission("myad.modify")) {
					if ((cycle = Ad.parse_cycle(args[1])) == 0) {
						sender.sendMessage("周期格式不正确");
						return true;
					}
					Ad ad = publisher.get_ad_by_player_name(sender.getName());
					if (ad == null) {
						sender.sendMessage("你还没有发布广告");
						return true;
					}
					ad.set_cycle(cycle);
					sender.sendMessage("周期设置成功");
				} else {
					sender.sendMessage("你没有权限修改广告");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("contents")) {
				if (args.length != 2) {
					sender.sendMessage("/myad contents <内容>");
					return true;
				}
				if (sender.hasPermission("myad.modify")) {
					Ad ad = publisher.get_ad_by_player_name(sender.getName());
					if (ad == null) {
						sender.sendMessage("你还没有发布广告");
						return true;
					}
					ad.set_contents(args[1]);
					sender.sendMessage("内容修改成功");
				} else {
					sender.sendMessage("你没有权限修改广告");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("extend")) {
				if (args.length != 2) {
					sender.sendMessage("/myad extend <次数>");
					return true;
				}
				if (sender.hasPermission("myad.modify")) {
					try {
						times = Integer.parseInt(args[1]);
					} catch (Exception e) {
						sender.sendMessage("次数必须是整数");
						return true;
					}
					Ad ad = publisher.get_ad_by_player_name(sender.getName());
					if (ad == null) {
						sender.sendMessage("你还没有发布广告");
						return true;
					}
					if (check_out(sender, times) == false) {
						return true;
					}
					int current_last_times = ad.get_last_times();
					ad.set_last_times(current_last_times + times);
					sender.sendMessage("次数增加成功");
					if (current_last_times == 0) {
						ad.start();
						sender.sendMessage("你的广告已经重新启动");
					}
				} else {
					sender.sendMessage("你没有权限修改广告");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("info")) {
				if (sender.hasPermission("myad.info")) {

				} else {
					sender.sendMessage("你没有权限查看广告列表");
				}
				return true;
			}
		}
		return false;
	}
}

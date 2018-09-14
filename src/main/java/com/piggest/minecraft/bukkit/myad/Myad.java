package com.piggest.minecraft.bukkit.myad;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Myad extends JavaPlugin {
	private boolean use_vault = true;
	private Economy economy = null;

	private FileConfiguration config = null;

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

	@Override
	public void onEnable() {
		saveDefaultConfig();
		config = getConfig();
		use_vault = config.getBoolean("use-vault");
		if (use_vault == true) {
			getLogger().info("使用Vault");
			if (!initVault()) {
				getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
				return;
			}
		} else {
			getLogger().info("不使用Vault");
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int cycle = 1;
		int times = 1;
		int base_price = 100;
		int total_price = 0;
		if (cmd.getName().equalsIgnoreCase("myad")) {
			if (args[0].equalsIgnoreCase("apply")) { // 申请广告
				if (sender.hasPermission("myad.apply")) {
					try {
						times = Integer.parseInt(args[2]);
					} catch (Exception e) {
						sender.sendMessage("次数必须是整数");
						return true;
					}
					if((cycle = Ad.parse_cycle(args[1]))==0){
						sender.sendMessage("周期格式不正确");
						return true;
					}
					total_price = base_price*times;
					if (!sender.hasPermission("myad.free")){
						Player player = (Player)sender;
						if (economy.has(player, total_price)) {
							economy.withdrawPlayer(player, total_price);
							player.sendMessage("已扣除" + total_price);
						} else {
							player.sendMessage("你的金钱不够");
							return true;
						}
					}
					
					sender.sendMessage("广告申请成功");
				} else {
					sender.sendMessage("你没有权限申请广告");
				}
				return true;
			}
		}
		return false;
	}
}

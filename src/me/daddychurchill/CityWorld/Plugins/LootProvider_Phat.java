package me.daddychurchill.CityWorld.Plugins;

import java.util.Random;

import me.daddychurchill.CityWorld.WorldGenerator;

import org.bukkit.inventory.ItemStack;

public class LootProvider_Phat extends LootProvider {
	
	public final static String chestInSewers = "CityWorld_Chest_Sewer";
	public final static String chestInMines = "CityWorld_Chest_Mine";
	public final static String chestInBunkers = "CityWorld_Chest_Bunker";
	public final static String chestInBankVault = "CityWorld_Chest_BankVault";
	public final static String chestInStorageShed = "CityWorld_Chest_StorageShed";
	
	@Override
	public ItemStack[] getItems(WorldGenerator generator, Random random, LootLocation lootLocation) {
		
		// which mix?
		switch (lootLocation) {
		case BUNKER:
			return getItemsByName(generator, chestInBunkers);
		case MINE:
			return getItemsByName(generator, chestInMines);
		case BANKVAULT:
			return getItemsByName(generator, chestInBankVault);
		case STORAGESHED:
			return getItemsByName(generator, chestInStorageShed);
		default: //case SEWER:
			return getItemsByName(generator, chestInSewers);
		}
	}

	public ItemStack[] getItemsByName(WorldGenerator generator, String name) {

		//https://github.com/Codisimus/PhatLoots
		
//		PhatLoot phatLoot;
//		if (!PhatLoots.hasPhatLoot(name)) {
//        	PhatLoots.addPhatLoot(new PhatLoot(name));        	
//        }
//		phatLoot = PhatLoots.getPhatLoot(name);
//		
//		phatLoot.chests.add(new PhatLootChest(block));
//		phatLoot.save();
		return null;
		
	}

//	private static String name = "PhatLoots";
	public static LootProvider loadPhatLoots() {

//		PhatLoots phatLoots = null;
//
//		PluginManager pm = Bukkit.getServer().getPluginManager();
//
//		try {
//			phatLoots = (PhatLoots) pm.getPlugin(name);
//		} catch (Exception ex) {
//			CityWorld.log.info(String.format("[CityWorld][LootProvider] Bad Version %s.", name));
//		}
//
//		if (phatLoots == null)
			return null;
//
//		CityWorld.log.info(String.format("[CityWorld][LootProvider] Found %s.", name));
//		
//		try {
//
//			if (!pm.isPluginEnabled(phatLoots)) {
//				CityWorld.log.info(String.format("[CityWorld][LootProvider] Enabling %s.", name));
//				pm.enablePlugin(phatLoots);
//			}
//			CityWorld.log.info(String.format("[CityWorld][LootProvider] %s Enabled.", name));
//			
//			return new LootProvider_PhatLoots();
//			
//		} catch (Exception ex) {
//			CityWorld.log.info(String.format("[CityWorld][LootProvider] Failed to enable %s.", name));
//			CityWorld.log.info(ex.getStackTrace().toString());
//			return null;
//		}
	}
}

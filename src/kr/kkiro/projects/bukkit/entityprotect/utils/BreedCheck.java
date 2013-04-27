package kr.kkiro.projects.bukkit.entityprotect.utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class BreedCheck {
	public static boolean wolfCheck(EntityType entity, Material item) {
		if(!entity.equals(EntityType.WOLF)) return false;
		if(item.equals(Material.RAW_BEEF)) return true;
		if(item.equals(Material.COOKED_BEEF)) return true;
		if(item.equals(Material.RAW_CHICKEN)) return true;
		if(item.equals(Material.COOKED_CHICKEN)) return true;
		if(item.equals(Material.PORK)) return true;
		if(item.equals(Material.GRILLED_PORK)) return true;
		if(item.equals(Material.ROTTEN_FLESH)) return true;
		return false;
	}
	
	public static boolean pigCheck(EntityType entity, Material item) {
		return entity.equals(EntityType.PIG) && item.equals(Material.CARROT_ITEM);
	}
	
	public static boolean chickenCheck(EntityType entity, Material item) {
		if(!entity.equals(EntityType.CHICKEN)) return false;
		if(item.equals(Material.SEEDS)) return true;
		if(item.equals(Material.MELON_SEEDS)) return true;
		if(item.equals(Material.PUMPKIN_SEEDS)) return true;
		if(item.equals(Material.NETHER_WARTS)) return true;
		return false;
	}
	
	public static boolean cowCheck(EntityType entity, Material item) {
		return entity.equals(EntityType.COW) && item.equals(Material.WHEAT);
	}
	
	public static boolean sheepCheck(EntityType entity, Material item) {
		return entity.equals(EntityType.SHEEP) && item.equals(Material.WHEAT);
	}
	
	public static boolean mushroomcowCheck(EntityType entity, Material item) {
		return entity.equals(EntityType.MUSHROOM_COW) && item.equals(Material.WHEAT);
	}
	
	public static boolean ozelotCheck(EntityType entity, Material item) {
		return entity.equals(EntityType.OCELOT) && item.equals(Material.RAW_FISH);
	}
	
	public static boolean check(EntityType entity, Material item) {
		if(wolfCheck(entity, item)) return true;
		if(pigCheck(entity, item)) return true;
		if(cowCheck(entity, item)) return true;
		if(sheepCheck(entity, item)) return true;
		if(mushroomcowCheck(entity, item)) return true;
		if(ozelotCheck(entity, item)) return true;
		if(chickenCheck(entity, item)) return true;
		return false;
	}
}

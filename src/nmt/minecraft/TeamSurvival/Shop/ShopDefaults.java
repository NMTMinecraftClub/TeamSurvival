package nmt.minecraft.TeamSurvival.Shop;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public enum ShopDefaults {
	
	/*
	sharpness 1 books  - 75
	unbreaking 1 - 150
	knockback 1 - 50
	smite 1 - 50
	fire aspect/fire 1 - 100
	power 1 - 75
	infinity bow enchantment - 200
	 */
	WOOD_SWORD(Material.WOOD_SWORD, 1, 20),
	STONE_SWORD(Material.STONE_SWORD, 1, 75),
	GOLD_SWORD(Material.GOLD_SWORD, 1, 75),
	IRON_SWORD(Material.IRON_SWORD, 1, 200),
	DIAMOND_SWORD(Material.DIAMOND_SWORD, 1, 350),
	BEEF(Material.COOKED_BEEF, 5, 50),
	APPLES(Material.APPLE, 20, 50),
	BOW(Material.BOW, 1, 100),
	ARROWS(Material.ARROW, 10, 50),
	SHARPNESS(EnchantedBook.SHARPNESS.getEnchantedBook(), 75),
	UNBREAKING(EnchantedBook.UNBREAKING.getEnchantedBook(), 150),
	KNOCKBACK(EnchantedBook.KNOCKBACK.getEnchantedBook(), 50),
	SMITE(EnchantedBook.SMITE.getEnchantedBook(), 50),
	FIREASPECT(EnchantedBook.FIREASPECT.getEnchantedBook(), 100),
	FLAME(EnchantedBook.FLAME.getEnchantedBook(), 100),
	POWER(EnchantedBook.POWER.getEnchantedBook(), 75),
	INFINITY(EnchantedBook.INFINITY.getEnchantedBook(), 200),
	POTIONHEALTH(new PotionData(PotionType.INSTANT_HEAL, false, false), 50),
	POTIONHEALTH2(new PotionData(PotionType.INSTANT_HEAL, false, true), 100),
	POTIONREGEN(new PotionData(PotionType.REGEN, false, false), 100),
	POTIONREGEN2(new PotionData(PotionType.REGEN, false, true), 200),
	POTIONSWIFT(new PotionData(PotionType.SPEED, false, false), 50),
	POTIONSTRENGTH(new PotionData(PotionType.SPEED, false, true), 350);
	
	private ItemStack item;
	
	private int cost;
	
	private ShopDefaults(Material type, int amount, int cost) {
		this.item = new ItemStack(type, amount);
		this.cost = cost;
	}
	
	private ShopDefaults(ItemStack item, int cost) {
		this.item = item;
		this.cost = cost;
	}
	
	private ShopDefaults(PotionData data, int cost) {
		this.item = new ItemStack(Material.POTION);
		((PotionMeta)this.item.getItemMeta()).setBasePotionData(data);
		this.cost = cost;
	}

	public ItemStack getItem() {
		return item;
	}

	public int getCost() {
		return cost;
	}
	
	public ShopItem getShopItem() {
		return new ShopItem(item, cost);
	}
	
	private enum EnchantedBook {
			
		SHARPNESS(Enchantment.DAMAGE_ALL),
		UNBREAKING(Enchantment.DURABILITY),
		KNOCKBACK(Enchantment.KNOCKBACK),
		SMITE(Enchantment.DAMAGE_UNDEAD),
		FIREASPECT(Enchantment.FIRE_ASPECT),
		FLAME(Enchantment.ARROW_FIRE),
		POWER(Enchantment.ARROW_DAMAGE),
		INFINITY(Enchantment.ARROW_INFINITE);
		
		private Enchantment enchantment;
		
		private EnchantedBook(Enchantment enchantment) {
			this.enchantment = enchantment;
		}
		
		public ItemStack getEnchantedBook() {
			ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
			meta.addStoredEnchant(enchantment, 1, true);
			book.setItemMeta(meta);
			
			return book;
		}
	}
	
}

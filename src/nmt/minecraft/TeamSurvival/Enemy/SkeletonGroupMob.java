package nmt.minecraft.TeamSurvival.Enemy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * A mob that consists of a member of a hardcore skeleton group -- a boss
 * @author Skyler
 *
 */
public class SkeletonGroupMob extends Mob {
	
	private static final double maxHealth = 30.0;
	
	private static final String skeletonName = "Skeletal Boss";
	
	private static enum defaultEquipment {
		
		SWORD(new ItemStack(Material.DIAMOND_SWORD), Enchantment.KNOCKBACK),
		HELMET(new ItemStack(Material.CHAINMAIL_HELMET)),
		CHESTPLATE(new ItemStack(Material.IRON_CHESTPLATE)),
		LEGGINGS(new ItemStack(Material.IRON_CHESTPLATE)),
		BOOTS(new ItemStack(Material.IRON_BOOTS), Enchantment.PROTECTION_FALL);
		
		private ItemStack item;
		
		private defaultEquipment(ItemStack item) {
			this.item = item;
		}
		
		private defaultEquipment(ItemStack item, Enchantment ... enchants) {
			for (Enchantment e : enchants) {
				item.addUnsafeEnchantment(e, 1);
			}
			
			this.item = item;
		}
		
		public ItemStack getItem() {
			return item;
		}
		
	}
	
	public SkeletonGroupMob() {
		super("",0); //junk
	}
	
	@Override
	public LivingEntity SpawnEntity(Location loc) {
		World world = loc.getWorld();
		Skeleton skeleton = (Skeleton) world.spawnEntity(loc, EntityType.SKELETON);
		
		skeleton.setSkeletonType(SkeletonType.WITHER);
		skeleton.setMaxHealth(SkeletonGroupMob.maxHealth);
		
		EntityEquipment equips = skeleton.getEquipment();
		
		equips.setItemInMainHand(defaultEquipment.SWORD.getItem());
		equips.setChestplate(defaultEquipment.CHESTPLATE.getItem());
		equips.setHelmet(defaultEquipment.HELMET.getItem());
		equips.setLeggings(defaultEquipment.LEGGINGS.getItem());
		equips.setBoots(defaultEquipment.BOOTS.getItem());
		
		skeleton.setCustomName(skeletonName);
		skeleton.setCustomNameVisible(true);
		
		
		return skeleton;
	}
}

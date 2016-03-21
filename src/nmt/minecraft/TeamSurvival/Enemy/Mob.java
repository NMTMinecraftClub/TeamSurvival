package nmt.minecraft.TeamSurvival.Enemy;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

import nmt.minecraft.TeamSurvival.Session.GameSession;

/**
 * Holds information about a possible mob spawn, and spawns it when willed.<br />
 * The information about equipment and health is stored here so that random-per-mob
 * spawns are do-able. Also contains data about what chances a mob has to spawn with
 * armor and enchantments
 * @author Herobrine
 * @author Franz Chavez
 * @author James Pelster
 * @author Stephanie
 * 
 */
public class Mob {
	/**
	 * Wave at which it is possible to get mobs with diamond equipment
	 */
	private static final int diamondWave=9;
	private static final int ironWave=6;
	private static final int goldWave=4;
	private static final int leatherWave=3;
	
	private final int diamondChance=10;//10% chance for diamond
	private final int ironChance=20;
	private final int goldChance=25; //
	private final int leatherChance=50;//50% of mobs have leather armor
	
	/**
	 * Keep track of what type of entity to spawn
	 */
	private String type;
	private static Double[] chances;
	private ItemStack[] armor;
	private ItemStack weapon;
	
	private int waveNumber;
	
	private static Random rGen=null;
	
	
	private enum level{
		DIAMOND,
		IRON,
		GOLD,
		LEATHER,
		NONE;
	}
	
	private Mob(){
		type = "";
		armor = new ItemStack[4];
		waveNumber=0;
		if(rGen==null){
			rGen = new Random(System.currentTimeMillis());//seed the generator
		}
	}
	
	/**
	 * 
	 * @param type
	 * @param waveNumber
	 */
	public Mob(String type, int waveNumber){
		this();
		this.waveNumber = waveNumber;
		this.type = type;
		this.generateEnchChance(GameSession.defaultWaveCount);
		this.generateEquipment();
		this.generateEnchantments();
	}
	
	/**
	 * Generates random equipment for this Mob.
	 * @return true on success, false if it could not be generated
	 */
	private void generateEquipment(){
		//generate helmet
		switch(genEquipLevel()){
		case DIAMOND:
			armor[0] = new ItemStack(Material.DIAMOND_HELMET);
			break;
		case IRON:
			armor[0] = new ItemStack(Material.IRON_HELMET);
			break;
		case GOLD:
			armor[0] = new ItemStack(Material.GOLD_HELMET);
			break;
		case LEATHER:
			armor[0] = new ItemStack(Material.LEATHER_HELMET);
			break;
		case NONE:
			armor[0] = null;
		}
		
		//generate Chestplate
		switch(genEquipLevel()){
		case DIAMOND:
			armor[1] = new ItemStack(Material.DIAMOND_CHESTPLATE);
			break;
		case IRON:
			armor[1] = new ItemStack(Material.IRON_CHESTPLATE);
			break;
		case GOLD:
			armor[1] = new ItemStack(Material.GOLD_CHESTPLATE);
			break;
		case LEATHER:
			armor[1] = new ItemStack(Material.LEATHER_CHESTPLATE);
			break;
		case NONE:
			armor[1] = null;
		}
		
		//generate Leggings
		switch(genEquipLevel()){
		case DIAMOND:
			armor[2] = new ItemStack(Material.DIAMOND_LEGGINGS);
			break;
		case IRON:
			armor[2] = new ItemStack(Material.IRON_LEGGINGS);
			break;
		case GOLD:
			armor[2] = new ItemStack(Material.GOLD_LEGGINGS);
			break;
		case LEATHER:
			armor[2] = new ItemStack(Material.LEATHER_LEGGINGS);
			break;
		case NONE:
			armor[2] = null;
		}
		
		//generate Leggings
		switch(genEquipLevel()){
		case DIAMOND:
			armor[3] = new ItemStack(Material.DIAMOND_BOOTS);
			break;
		case IRON:
			armor[3] = new ItemStack(Material.IRON_BOOTS);
			break;
		case GOLD:
			armor[3] = new ItemStack(Material.GOLD_BOOTS);
			break;
		case LEATHER:
			armor[3] = new ItemStack(Material.LEATHER_BOOTS);
			break;
		case NONE:
			armor[3] = null;
		}
		
		if(this.type.equals(EntityType.SKELETON) && rGen.nextInt()%2 == 1){
			//this skeleton will have a bow
			weapon = new ItemStack(Material.BOW);
			return;
		}
		
		//generate sword
		switch(genEquipLevel()){
		case DIAMOND:
			weapon = new ItemStack(Material.DIAMOND_SWORD);
			break;
		case IRON:
			weapon = new ItemStack(Material.IRON_SWORD);
			break;
		case GOLD:
			weapon = new ItemStack(Material.STONE_SWORD);
			break;
		case LEATHER:
			weapon = new ItemStack(Material.WOOD_SWORD);
			break;
		case NONE:
			weapon = null;
		}
		return;
	}
	
	private level genEquipLevel(){
		//*
		int rNumber = rGen.nextInt(100);
		switch(waveNumber){
		case diamondWave:
			if(rNumber < diamondChance && waveNumber >= diamondWave){
				return level.DIAMOND;
			}else{
				rNumber = rGen.nextInt(100); //may need to get rid of this
			}
			//purposefully missing the break;
		case ironWave:
			if(rNumber < ironChance && waveNumber >= ironWave){
				return level.IRON;
			}else{
				rNumber=rGen.nextInt(100);
			}
		case goldWave:
			if(rNumber < goldChance && waveNumber >= goldWave){
				return level.GOLD;
			}else{
				rNumber=rGen.nextInt();
			}
		case leatherWave:
			if(rNumber < leatherChance && waveNumber >= leatherWave){
				return level.LEATHER;
			}
		default:
			return level.NONE;
		}
		//*/
	}
	
	/**
	 * This method will calculate all the equipment that will recieve enchantments
	 */
	private void generateEnchantments(){
		int numEnchant=0;
		//enchant the weapon
		if(canEnchant(numEnchant) && enchantWeapon()){
			numEnchant++;
		}
		
		//enchant the helm
		if(canEnchant(numEnchant) && enchantArmor(0)){
			numEnchant++;
		}
		
		//enchant the chest
		if(canEnchant(numEnchant) && enchantArmor(1)){
			numEnchant++;
		}
		
		//enchant the legs
		if(canEnchant(numEnchant) && enchantArmor(2)){
			numEnchant++;
		}
		
		//enchant the boots
		if(canEnchant(numEnchant) &&  enchantArmor(3)){
			numEnchant++;
		}
	}
	
	/**
	 * This method actually adds the enchantment to the itemstack for the armor
	 * @param index the index of the piece of armor to enchant
	 * @return true if it worked, false otherwise
	 */
	private boolean enchantArmor(int index) {
		//make sure the armor exists
		if(armor[index] == null){
			return false;
		}
		
		switch(rGen.nextInt(5)){
		case 0:
			//fire protection
			armor[index].addEnchantment(Enchantment.PROTECTION_FIRE, 1);
			if(rGen.nextInt()%100 < 90){
				break;
			}
		case 1:
			//blast protection
			armor[index].addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1);
			if(rGen.nextInt()%100 < 90){
				break;
			}
		case 2:
			//projectile protection
			armor[index].addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
			if(rGen.nextInt()%100 < 90){
				break;
			}
		case 3:
			//protection
			armor[index].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			if(rGen.nextInt()%100 < 90){
				break;
			}
		case 4:
		//thorns
			armor[index].addEnchantment(Enchantment.THORNS, 1);
		}
		return false;
	}
	
	/**
	 * This method adds the enchantment to the itemstack for the weapon
	 * @return true if it worked, false if there was no weapon to enchant
	 */
	private boolean enchantWeapon() {
		//make sure the weapon exists
		if(weapon == null) {
			return false;
		}
		
		if(weapon.getData().getItemType().equals(Material.BOW)) {
			//its a bow
			switch(rGen.nextInt(3)){
			case 0://power
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
				if(rGen.nextInt()%100 < 95){
					break;
				}
			case 1://punch
				weapon.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
				if(rGen.nextInt()%100 < 95){
					break;
				}
			case 2://fire aspect
				weapon.addEnchantment(Enchantment.ARROW_FIRE, 1);
			}
		} else {
			//its a sword
			
			//knockback
			//fire
			switch(rGen.nextInt(3)){
			case 0://sharpness
				weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				if(rGen.nextInt()%100 < 95){
					break;
				}
			case 1://punch
				weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
				if(rGen.nextInt()%100 < 95){
					break;
				}
			case 2://fire aspect
				weapon.addEnchantment(Enchantment.FIRE_ASPECT, 1);
			}
			
		}
		
		return true;
	}

	/**
	 * Checks to see if it is ok to enchant a piece of equipment
	 * @param currentEnchants the number of enchants currently on all equipment
	 * @return true if it is ok to enchant, false otherwise
	 */
	private boolean canEnchant(int currentEnchants){
		boolean base = rGen.nextDouble() < chances[this.waveNumber-1];
		boolean mod = rGen.nextInt(100) < 50-(currentEnchants*20);
		return base && mod;
	}
	
	private void generateEnchChance(int totalWaves){
		if(chances != null){
			return;
		}
		double max = (double)2/3;
		double pivotPoint = 7;
		chances = new Double[totalWaves];
		for(int i=0; i<2; i++){
			chances[i] = 0.0;
		}
		for(int wave=2; wave<totalWaves; wave++) {
			double exp = -1 * rGen.nextDouble() * (wave-pivotPoint);
			double percentEnch = max /(1 + Math.pow(Math.E, exp));
			
			chances[wave] = percentEnch;
		}
		
		System.out.println("Chances: ");
		for(int i = 0; i < totalWaves; i++) {
			System.out.println("  Wave: " + i + "    %: " + chances[i]);
		}
	}
	
	public LivingEntity spawnMob(Location spawnLocation) {
		LivingEntity mob;
		
		if(type.equalsIgnoreCase("jockey")) {
			LivingEntity jockeyVehicle = (LivingEntity)spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SPIDER);
			mob = (LivingEntity)spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
			jockeyVehicle.setPassenger(mob);
		} else {
			mob = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.valueOf(type));
		}
		
		return null;
	}
	
	public LivingEntity SpawnEntity(Location loc) {
		LivingEntity spawnedEntity;
		if(type.equalsIgnoreCase("Jockey")) {
			LivingEntity jockeyVehicle = (LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.SPIDER);
			spawnedEntity = (LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
			jockeyVehicle.setPassenger(spawnedEntity);
		} else if(type.equalsIgnoreCase("Wither_Skeleton")) {
			spawnedEntity = (LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
			((Skeleton)spawnedEntity).setSkeletonType(SkeletonType.WITHER);
		} else if(type.equalsIgnoreCase("Creeper") & rGen.nextInt(50) == 49) {
			spawnedEntity = (LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
		} else {
			spawnedEntity = (LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.valueOf(type.toUpperCase()));
			spawnedEntity.setCanPickupItems(false);
		}
		
		//set items for spawned entity and return it
		if(armor[0]!= null)//set helm
			spawnedEntity.getEquipment().setHelmet(armor[0]);
		if(armor[1]!= null)//chest
			spawnedEntity.getEquipment().setChestplate(armor[1]);
		if(armor[2]!= null)//legs
			spawnedEntity.getEquipment().setLeggings(armor[2]);
		if(armor[3]!= null)//boots
			spawnedEntity.getEquipment().setBoots(armor[3]);
		if(weapon != null)
			spawnedEntity.getEquipment().setItemInMainHand(weapon);
		
		return spawnedEntity;
	}
	
	/**
	 * Returns a clone of the wave. 
	 */
	@Override
	public Mob clone(){
		Mob newMob = new Mob();
		
		newMob.type=this.type;
		
		for(int i = 0; i < this.armor.length; i++){
			if(this.armor[i] != null){
				newMob.armor[i] = this.armor[i].clone();
			} else {
				newMob.armor[i] = null;
			}
		}
		
		if(this.weapon != null)
			newMob.weapon = this.weapon.clone();
		
		newMob.waveNumber = this.waveNumber;
		
		
		return newMob;
	}
}
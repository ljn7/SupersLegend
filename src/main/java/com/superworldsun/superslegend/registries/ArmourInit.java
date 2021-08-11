package com.superworldsun.superslegend.registries;

import com.superworldsun.superslegend.SupersLegendMain;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum ArmourInit implements IArmorMaterial
{
	//armor										boots, legs, chest, helm
	kokiri		("kokiri", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	zora		("zora", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	goron		("goron", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	purple		("purple", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	magic		("magic", 0, new int[] 			{1, 1, 1, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	herosnew	("herosnew", 0, new int[] 		{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	twilight	("twilight", 0, new int[] 		{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	wind		("wind", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	lobster		("lobster", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	engineer	("engineer", 0, new int[] 		{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	nswitch		("nswitch", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	hyrule		("hyrule", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	wild		("wild", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	minish		("minish", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	sky			("sky", 0, new int[] 				{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	legend		("legend", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	dark		("dark", 0, new int[] 			{1, 2, 3, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	zoraarmor	("zoraarmor", 0, new int[] 		{1, 2, 3, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	flamebreaker("flamebreaker", 0, new int[] 	{1, 2, 3, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	ancient		("ancient", 0, new int[] 			{1, 3, 5, 1}, 0, null, "item.armor.equip_leather", 3.0f),
	barbarian	("barbarian", 0, new int[] 		{1, 1, 2, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	climbing	("climbing", 0, new int[] 		{1, 2, 3, 1}, 0, null, "item.armor.equip_leather", 0.0f),
	flippers	("flippers", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	rocscape	("rocscape", 0, new int[] 		{0, 0, 2, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	hoverboots	("hoverboots", 0, new int[] 		{1, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	ironboots	("ironboots", 0, new int[] 		{1, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	pegasusboots("pegasusboots", 0, new int[] 	{1, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	//masks
	postmanshat("postmanshat", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	allnightmask("allnightmask", 0, new int[] 	{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	blastmask("blastmask", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	stonemask("stonemask", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	greatfairymask("greatfairymask", 0, new int[] {0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	dekumask("dekumask", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	keatonmask("keatonmask", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	bremenmask("bremenmask", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	bunnyhood("bunnyhood", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	dongerosmask("dongerosmask", 0, new int[] 	{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	maskofscents("maskofscents", 0, new int[] 	{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	goronmask("goronmask", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	romanismask("romanismask", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	troupeleadersmask("troupeleadersmask", 0, new int[] {0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	kafeismask("kafeismask", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	couplesmask("couplesmask", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	maskoftruth("maskoftruth", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	zoramask("zoramask", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	kamarosmask("kamarosmask", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	gibdomask("gibdomask", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	garosmask("garosmask", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	captainshat("captainshat", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	gnathat("gnathat", 0, new int[] 				{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	giantsmask("giantsmask", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	fiercedeitysmask("fiercedeitysmask", 0, new int[] {0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	majorasmask("majorasmask", 0, new int[] 		{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	hawkeye("hawkeye", 0, new int[] 				{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	moonmask("moonmask", 0, new int[] 			{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f),
	sunmask("sunmask", 0, new int[] 				{0, 0, 0, 0}, 0, null, "item.armor.equip_leather", 0.0f);
	
	private static final int[] MAX_DAMAGE_ARRAY = { 13, 15, 16, 11 };
	private String name, equipSound;
	private int durability, enchantability;
	private Item repairItem;
	private int[] damageReductionAmounts;
	private float toughness;
	
	private ArmourInit(String name, int durability, int[] damageReductionAmounts, int enchantability, Item repairItem, String equipSound, float toughness)
	{
		this.name = name;
		this.equipSound = equipSound;
		this.durability = durability;
		this.enchantability = enchantability;
		this.repairItem = repairItem;
		this.damageReductionAmounts = damageReductionAmounts;
		this.toughness = toughness;
	}
	
	@Override
	public int getDefenseForSlot(EquipmentSlotType slot)
	{
		return this.damageReductionAmounts[slot.getIndex()];
	}
	
	@Override
	public int getDurabilityForSlot(EquipmentSlotType slot)
	{
		return MAX_DAMAGE_ARRAY[slot.getIndex()] * this.durability;
	}
	
	@Override
	public int getEnchantmentValue()
	{
		return this.enchantability;
	}
	
	@Override
	public String getName()
	{
		return SupersLegendMain.MOD_ID + ":" + this.name;
	}
	
	@Override
	public Ingredient getRepairIngredient()
	{
		return Ingredient.of(this.repairItem);
	}
	
	@Override
	public SoundEvent getEquipSound()
	{
		return new SoundEvent(new ResourceLocation(equipSound));
	}
	
	@Override
	public float getToughness()
	{
		return this.toughness;
	}
	
	@Override
	public float getKnockbackResistance()
	{
		return 0;
	}	
}

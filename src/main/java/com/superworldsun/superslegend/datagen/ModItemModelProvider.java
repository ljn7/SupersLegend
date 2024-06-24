package com.superworldsun.superslegend.datagen;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.BlockInit;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SupersLegendMain.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemInit.DARK_ORE);
        SwordItem(ItemInit.DARK_SWORD);
        PickaxeItem(ItemInit.DARK_PICKAXE);
        AxeItem(ItemInit.DARK_AXE);
        ShovelItem(ItemInit.DARK_SHOVEL);
        HoeItem(ItemInit.DARK_HOE);
        simpleBlockItem(BlockInit.DUNGEON_DOOR);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(SupersLegendMain.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder SwordItem(RegistryObject<SwordItem> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(SupersLegendMain.MOD_ID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder PickaxeItem(RegistryObject<PickaxeItem> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(SupersLegendMain.MOD_ID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder AxeItem(RegistryObject<AxeItem> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(SupersLegendMain.MOD_ID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder ShovelItem(RegistryObject<ShovelItem> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(SupersLegendMain.MOD_ID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder HoeItem(RegistryObject<HoeItem> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(SupersLegendMain.MOD_ID,"item/" + item.getId().getPath()));
    }


    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(SupersLegendMain.MOD_ID,"item/" + item.getId().getPath()));
    }
}
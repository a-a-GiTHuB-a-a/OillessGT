package com.daboxen.oillessgt;

import com.daboxen.oillessgt.additions.ProgressionPatches;
import com.daboxen.oillessgt.block.OillessBlocks;
import com.daboxen.oillessgt.config.OillessConfiguration;
import com.daboxen.oillessgt.data.OillessDatagen;
import com.daboxen.oillessgt.item.LampCasingBlockItem;
import com.daboxen.oillessgt.item.LampCasingItemOverlayRenderer;
import com.daboxen.oillessgt.machines.OillessMachines;
import com.daboxen.oillessgt.recipe.OillessRecipeConditions;
import com.daboxen.oillessgt.recipe.OillessRecipeTypes;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.sound.SoundEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(OillessGTMod.MOD_ID)
@SuppressWarnings("removal")
public class OillessGTMod {

    public static final String MOD_ID = "oillessgt";
    public static final Logger OILLESS_LOGGER = LogManager.getLogger();
    public static GTRegistrate OILLESS_REGISTRATE = GTRegistrate.create(OillessGTMod.MOD_ID);

    public OillessGTMod() {
        OillessDatagen.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        OillessConfiguration.init();
        OillessBlocks.init();

        modEventBus.addListener(this::addMaterials);
        modEventBus.addListener(this::modifyMaterials);

        modEventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        modEventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
        modEventBus.addGenericListener(SoundEntry.class, this::registerSounds);
        modEventBus.addGenericListener(RecipeConditionType.class, this::registerRecipeConditions);

        // Most other events are fired on Forge's bus.
        // If we want to use annotations to register event listeners,
        // we need to register our object like this!
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OILLESS_LOGGER.info("Hello from common setup! This is *after* registries are done, so we can do this:");
            OILLESS_LOGGER.info("Look, I found a {}!", Items.DIAMOND);
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        OILLESS_LOGGER.info("Hey, we're on Minecraft version {}!", Minecraft.getInstance().getLaunchedVersion());
    }

    @SubscribeEvent
    public void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof LampCasingBlockItem) {
                event.register(item, LampCasingItemOverlayRenderer.INSTANCE);
            }
        }
    }

    /**
     * Create a ResourceLocation in the format "modid:path"
     *
     * @param path
     * @return ResourceLocation with the namespace of your mod
     */
    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    /**
     * You will also need this for registering custom materials
     * Call init() from your Material class(es) here
     * 
     * @param event
     */
    private void addMaterials(MaterialEvent event) {
        ProgressionPatches.registerMaterials();
    }

    /**
     * (Optional) Used to modify pre-existing materials from GregTech
     * 
     * @param event
     */
    private void modifyMaterials(PostMaterialEvent event) {
        ProgressionPatches.modifyMaterials();
    }

    /**
     * Used to register your own new RecipeTypes.
     * Call init() from your RecipeType class(es) here
     * 
     * @param event
     */
    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        OillessRecipeTypes.init();
    }

    private void registerRecipeConditions(GTCEuAPI.RegisterEvent<ResourceLocation, RecipeConditionType<?>> event) {
        OillessRecipeConditions.init();
    }

    /**
     * Used to register your own new machines.
     * Call init() from your Machine class(es) here
     * 
     * @param event
     */
    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        OillessMachines.init(event);
    }

    /**
     * Used to register your own new sounds
     * Call init from your Sound class(es) here
     * 
     * @param event
     */
    public void registerSounds(GTCEuAPI.RegisterEvent<ResourceLocation, SoundEntry> event) {
        // CustomSounds.init();
    }
}

package com.cyberspectraa.cyberclasses;

import com.cyberspectraa.cyberclasses.network.CyberClassesNetwork;
import com.cyberspectraa.cyberclasses.registry.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(CyberClasses.MOD_ID)
public final class CyberClasses {
    public static final String MOD_ID = "cyberclasses";

    public CyberClasses() {
        CyberClassesNetwork.init();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::addCreativeBooks);

        MinecraftForge.EVENT_BUS.register(
            com.cyberspectraa.cyberclasses.event.ClassBookLootEvents.class
        );

        MinecraftForge.EVENT_BUS.register(
            com.cyberspectraa.cyberclasses.event.ClassSelectionEvents.class
        );
        MinecraftForge.EVENT_BUS.register(
            com.cyberspectraa.cyberclasses.event.ClassRestrictionEvents.class
        );
        MinecraftForge.EVENT_BUS.register(
            com.cyberspectraa.cyberclasses.command.CyberClassCommands.class
        );

        if (ModList.get().isLoaded("irons_spellbooks")) {
            MinecraftForge.EVENT_BUS.register(
                com.cyberspectraa.cyberclasses.event.IronSpellsRestrictionEvents.class
            );
        }
    }

    private void addCreativeBooks(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.COMMON_CLASS_BOOK);
            event.accept(ModItems.UNCOMMON_CLASS_BOOK);
            event.accept(ModItems.RARE_CLASS_BOOK);
            event.accept(ModItems.LEGENDARY_CLASS_BOOK);
        }
    }
}

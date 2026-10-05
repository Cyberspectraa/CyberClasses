package com.cyberspectraa.cyberclasses;

import com.cyberspectraa.cyberclasses.network.CyberClassesNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(CyberClasses.MOD_ID)
public final class CyberClasses {
    public static final String MOD_ID = "cyberclasses";

    public CyberClasses() {
        CyberClassesNetwork.init();

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
}

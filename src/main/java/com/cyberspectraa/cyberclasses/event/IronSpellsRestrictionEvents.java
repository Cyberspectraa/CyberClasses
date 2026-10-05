package com.cyberspectraa.cyberclasses.event;

import com.cyberspectraa.cyberclasses.restriction.ClassRestrictionEngine;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public final class IronSpellsRestrictionEvents {
    private IronSpellsRestrictionEvents() {
    }

    @SubscribeEvent
    public static void onSpellPreCast(SpellPreCastEvent event) {
        if (!ModList.get().isLoaded("irons_spellbooks")
                || !(event.getEntity() instanceof ServerPlayer player)
                || ClassRestrictionEngine.mayCastMagic(player)) {
            return;
        }

        event.setCanceled(true);
        ClassRestrictionEvents.deny(player, "magic");
    }
}

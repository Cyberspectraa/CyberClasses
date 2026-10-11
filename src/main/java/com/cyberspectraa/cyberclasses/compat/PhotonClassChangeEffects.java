package com.cyberspectraa.cyberclasses.compat;

import com.cyberspectraa.cyberclasses.item.ClassBookItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.fml.ModList;

import java.util.Locale;

/**
 * Server-side, optional Photon VFX on successful class transformation.
 * Reuses the currently deployed CyberServer summoning FX at a smaller
 * scale, so clients with the existing resource pack see a true Photon
 * animation without adding a hard Photon dependency to CyberClasses.
 *
 * No change is made to CyberServer or its arrival effects.
 */
public final class PhotonClassChangeEffects {
    private static final String PHOTON_FX = "cyberserver:summoning";

    private PhotonClassChangeEffects() {}

    public static void play(ServerPlayer player, ClassBookItem.Tier tier) {
        ServerLevel level = player.serverLevel();
        boolean photonPlayed = false;
        if (ModList.get().isLoaded("photon") && ModList.get().isLoaded("cyberserver")) {
            MinecraftServer server = level.getServer();
            double scale = switch (tier) {
                case COMMON -> 0.23D;
                case UNCOMMON -> 0.28D;
                case RARE -> 0.34D;
                case LEGENDARY -> 0.43D;
            };
            int x = player.blockPosition().getX();
            int y = player.blockPosition().getY();
            int z = player.blockPosition().getZ();
            String command = String.format(Locale.ROOT,
                    "photon fx %s block %d %d %d 0 -0.5 0 0 0 0 %.3f %.3f %.3f 0 false true false",
                    PHOTON_FX, x, y, z, scale, scale, scale);
            CommandSourceStack source = server.createCommandSourceStack()
                    .withLevel(level).withPosition(player.position())
                    .withPermission(4).withSuppressedOutput();
            try {
                photonPlayed = server.getCommands().performPrefixedCommand(source, command) > 0;
            } catch (RuntimeException error) {
                org.slf4j.LoggerFactory.getLogger(PhotonClassChangeEffects.class).warn("Photon class-change effect unavailable; using vanilla particles", error);
            }
        }

        // Distinct, modest particles are retained even when Photon succeeds,
        // providing an immediate visual confirmation on every client.
        int count = photonPlayed ? 12 : 42;
        level.sendParticles(ParticleTypes.ENCHANT,
                player.getX(), player.getY() + 1.0D, player.getZ(),
                count, 0.55D, 0.85D, 0.55D, 0.13D);
        level.sendParticles(tier == ClassBookItem.Tier.LEGENDARY
                        ? ParticleTypes.TOTEM_OF_UNDYING : ParticleTypes.END_ROD,
                player.getX(), player.getY() + 0.9D, player.getZ(),
                photonPlayed ? 8 : 26, 0.45D, 0.7D, 0.45D, 0.07D);
        level.playSound(null, player.blockPosition(),
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F,
                tier == ClassBookItem.Tier.LEGENDARY ? 0.75F : 1.15F);
        level.playSound(null, player.blockPosition(),
                SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.75F, 1.25F);
    }
}

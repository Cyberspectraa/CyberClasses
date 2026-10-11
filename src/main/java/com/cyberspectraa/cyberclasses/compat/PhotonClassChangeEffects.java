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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Dedicated, short-lived Photon 1.x effect bound to a specific PLAYER,
 * never to a block or position. Photon follows the entity each frame.
 *
 * The effect is generated into CyberClasses.jar under
 * assets/cyberclasses/fx/class_transformation.fx. No extra resource pack
 * or CyberServer dependency is needed. Other clients receive it through
 * Photon's existing entity-effect network packet.
 */
public final class PhotonClassChangeEffects {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotonClassChangeEffects.class);
    private static final String EFFECT_ID = "cyberclasses:class_transformation";

    private PhotonClassChangeEffects() {}

    public static void play(ServerPlayer player, ClassBookItem.Tier tier) {
        ServerLevel level = player.serverLevel();
        boolean photonPlayed = false;

        if (ModList.get().isLoaded("photon")) {
            // Standard Minecraft names have no selector-control characters.
            // Still validate before building a privileged server command.
            String name = player.getGameProfile().getName();
            if (name.matches("[A-Za-z0-9_]{1,16}")) {
                MinecraftServer server = level.getServer();
                float scale = switch (tier) {
                    case COMMON -> 0.78F;
                    case UNCOMMON -> 0.90F;
                    case RARE -> 1.02F;
                    case LEGENDARY -> 1.18F;
                };
                // The root is relative to the player's eye; a -1.05 Y offset
                // puts the centre at torso height. 'none' keeps its axis
                // upright as the player turns while still following movement.
                String command = String.format(Locale.ROOT,
                        "photon fx %s entity @a[name=%s] 0 -1.05 0 0 0 0 %.2f %.2f %.2f 0 false false none",
                        EFFECT_ID, name, scale, scale, scale);
                CommandSourceStack source = server.createCommandSourceStack()
                        .withLevel(level)
                        .withPosition(player.position())
                        .withPermission(4)
                        .withSuppressedOutput();
                try {
                    photonPlayed = server.getCommands().performPrefixedCommand(source, command) > 0;
                } catch (RuntimeException exception) {
                    LOGGER.warn("Photon class-change effect failed; using vanilla fallback.", exception);
                }
            }
        }

        // Subtle immediate confirmation while Photon starts; a larger fallback
        // ensures non-Photon clients still see a class change.
        int sparks = photonPlayed ? 5 : 32;
        level.sendParticles(ParticleTypes.ENCHANT,
                player.getX(), player.getY() + 1.0D, player.getZ(),
                sparks, 0.42D, 0.67D, 0.42D, 0.08D);
        if (!photonPlayed) {
            level.sendParticles(tier == ClassBookItem.Tier.LEGENDARY
                            ? ParticleTypes.TOTEM_OF_UNDYING : ParticleTypes.END_ROD,
                    player.getX(), player.getY() + 0.95D, player.getZ(),
                    22, 0.38D, 0.75D, 0.38D, 0.065D);
        }
        level.playSound(null, player.blockPosition(),
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS,
                0.95F, tier == ClassBookItem.Tier.LEGENDARY ? 0.75F : 1.13F);
        level.playSound(null, player.blockPosition(),
                SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.72F, 1.27F);
    }
}

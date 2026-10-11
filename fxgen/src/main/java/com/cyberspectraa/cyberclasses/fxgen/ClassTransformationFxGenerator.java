package com.cyberspectraa.cyberclasses.fxgen;

import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.photon.integration.PhotonLDLibPlugin;
import com.lowdragmc.photon.client.gameobject.emitter.data.EmissionSetting;
import com.lowdragmc.photon.client.gameobject.emitter.data.RendererSetting;
import com.lowdragmc.photon.client.gameobject.emitter.data.material.TextureMaterial;
import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunction3;
import com.lowdragmc.photon.client.gameobject.emitter.data.shape.Circle;
import com.lowdragmc.photon.client.gameobject.emitter.data.shape.Dot;
import com.lowdragmc.photon.client.gameobject.emitter.data.shape.Sphere;
import com.lowdragmc.photon.client.gameobject.emitter.particle.ParticleEmitter;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Standalone build-time export of a custom Photon 1.x effect.
 *
 * Runtime anchoring is supplied by Photon's EntityEffectExecutor.
 * Root origin is at the player's waist: Java passes a -1.05 eye offset.
 * Nothing here reuses the CyberServer summoning ritual.
 */
public final class ClassTransformationFxGenerator {
    private static final ResourceLocation RING =
            new ResourceLocation("photon", "textures/particle/ring.png");
    private static final ResourceLocation SMOKE =
            new ResourceLocation("photon", "textures/particle/smoke.png");

    private ClassTransformationFxGenerator() {}

    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Expected output .fx path");
        File output = new File(args[0]);
        if (output.getParentFile() != null && !output.getParentFile().exists()
                && !output.getParentFile().mkdirs()) {
            throw new IllegalStateException("Cannot create effect output folder");
        }

        SharedConstants.tryDetectVersion();
        var initialized = Bootstrap.class.getDeclaredField("isBootstrapped");
        initialized.setAccessible(true);
        initialized.setBoolean(null, true);
        TypedPayloadRegistries.init();
        TypedPayloadRegistries.register(NbtTagPayload.class, NbtTagPayload::new,
                PhotonLDLibPlugin.NUMBER_FUNCTION_ACCESSOR, 1000);
        TypedPayloadRegistries.register(NbtTagPayload.class, NbtTagPayload::new,
                PhotonLDLibPlugin.NUMBER_FUNCTION3_ACCESSOR, 1000);
        TypedPayloadRegistries.register(NbtTagPayload.class, NbtTagPayload::new,
                PhotonLDLibPlugin.SHAPE_ACCESSOR, 1000);
        TypedPayloadRegistries.postInit();

        FX fx = create();
        CompoundTag wrapper = new CompoundTag();
        wrapper.put("fx", fx.serializeNBT());
        wrapper.putInt("_version", 1);
        try (FileOutputStream stream = new FileOutputStream(output)) {
            NbtIo.writeCompressed(wrapper, stream);
        }
        System.out.println("Generated CyberClasses Photon entity FX: " +
                output.getAbsolutePath() + " (" + output.length() + " bytes)");
    }

    private static FX create() {
        FX fx = new FX();
        // Two small orbiting-looking energy bands; all positions are relative
        // to the moving player's body, never to a floor block.
        fx.getMainFX().objects().add(halo("lower_arcane_halo",
                0, 18, 0.28F, 1.25F, 0xD58C6FFF, 0.0F));
        fx.getMainFX().objects().add(halo("upper_rune_halo",
                6, 20, 1.08F, 1.00F, 0xD9E8CCFF, 67.0F));
        fx.getMainFX().objects().add(halo("closing_blessing",
                25, 12, 0.68F, 1.65F, 0xCFFFDA85, 135.0F));

        fx.getMainFX().objects().add(orbitMotes());
        fx.getMainFX().objects().add(risingEssence());
        fx.getMainFX().objects().add(renewalBurst());
        return fx;
    }

    private static ParticleEmitter halo(String name, int at, int life,
            float height, float width, int color, float rotation) {
        ParticleEmitter emitter = base(name, RING, color);
        emitter.config.setDuration(48);
        emitter.config.setStartLifetime(NumberFunction.constant(life));
        emitter.config.setStartSpeed(NumberFunction.constant(0F));
        emitter.config.setStartSize(new NumberFunction3(width, width, width));
        emitter.config.setStartRotation(new NumberFunction3(0F, 0F, rotation));
        emitter.config.shape.setShape(new Dot());
        emitter.config.shape.setPosition(new NumberFunction3(0F, height, 0F));
        emitter.config.renderer.setRenderMode(RendererSetting.Particle.Mode.Horizontal);
        burst(emitter, at, 1, 1, 1);
        return emitter;
    }

    private static ParticleEmitter orbitMotes() {
        ParticleEmitter emitter = base("orbiting_stars", RING, 0xE5B89BFF);
        emitter.config.setDuration(46);
        emitter.config.setStartLifetime(NumberFunction.constant(15));
        emitter.config.setStartSpeed(NumberFunction.constant(0.16F));
        emitter.config.setStartSize(new NumberFunction3(0.09F, 0.09F, 0.09F));
        Circle circle = new Circle();
        circle.setRadius(0.62F);
        circle.setRadiusThickness(0.12F);
        emitter.config.shape.setShape(circle);
        emitter.config.shape.setPosition(new NumberFunction3(0F, 0.63F, 0F));
        burst(emitter, 2, 8, 3, 9);
        return emitter;
    }

    private static ParticleEmitter risingEssence() {
        ParticleEmitter emitter = base("rising_class_essence", SMOKE, 0xBDBEA4FF);
        emitter.config.setDuration(49);
        emitter.config.setStartLifetime(NumberFunction.constant(22));
        emitter.config.setStartSpeed(NumberFunction.constant(0.85F));
        emitter.config.setStartSize(new NumberFunction3(0.15F, 0.23F, 0.15F));
        Sphere sphere = new Sphere();
        sphere.setRadius(0.45F);
        sphere.setRadiusThickness(0.5F);
        emitter.config.shape.setShape(sphere);
        emitter.config.shape.setPosition(new NumberFunction3(0F, 0.8F, 0F));
        burst(emitter, 3, 7, 4, 9);
        return emitter;
    }

    private static ParticleEmitter renewalBurst() {
        ParticleEmitter emitter = base("class_renewal_sparks", RING, 0xE9FFF0B8);
        emitter.config.setDuration(49);
        emitter.config.setStartLifetime(NumberFunction.constant(16));
        emitter.config.setStartSpeed(NumberFunction.constant(1.5F));
        emitter.config.setStartSize(new NumberFunction3(0.10F, 0.10F, 0.10F));
        Sphere sphere = new Sphere();
        sphere.setRadius(0.34F);
        sphere.setRadiusThickness(1.0F);
        emitter.config.shape.setShape(sphere);
        emitter.config.shape.setPosition(new NumberFunction3(0F, 0.82F, 0F));
        burst(emitter, 26, 30, 1, 1);
        return emitter;
    }

    private static ParticleEmitter base(String name, ResourceLocation image, int tint) {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.setName(name);
        emitter.config.setLooping(false);
        emitter.config.setMaxParticles(128);
        emitter.config.setStartColor(NumberFunction.color(tint));
        emitter.config.emission.setEmissionRate(NumberFunction.constant(0F));
        emitter.config.renderer.setBloomEffect(false);
        emitter.config.renderer.getCull().setEnable(false);
        emitter.config.lights.setEnable(true);
        emitter.config.material.setMaterial(new TextureMaterial(image));
        emitter.config.material.setCull(false);
        emitter.config.material.setDepthMask(false);
        emitter.config.material.setDepthTest(true);
        emitter.config.material.getBlendMode().setEnableBlend(true);
        emitter.config.material.getBlendMode().setSrcColorFactor(SourceFactor.SRC_ALPHA);
        emitter.config.material.getBlendMode().setDstColorFactor(DestFactor.ONE);
        emitter.config.material.getBlendMode().setSrcAlphaFactor(SourceFactor.ONE);
        emitter.config.material.getBlendMode().setDstAlphaFactor(DestFactor.ONE);
        return emitter;
    }

    private static void burst(ParticleEmitter emitter, int at, int count,
            int cycles, int interval) {
        EmissionSetting.Burst burst = new EmissionSetting.Burst();
        burst.time = at;
        burst.setCount(NumberFunction.constant(count));
        burst.cycles = cycles;
        burst.interval = interval;
        burst.probability = 1F;
        emitter.config.emission.getBursts().add(burst);
    }
}

package com.cyberspectraa.cyberclasses.registry;

import com.cyberspectraa.cyberclasses.CyberClasses;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;

/** Dedicated CyberClasses inventory page: all four class-book rarities. */
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CyberClasses.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CYBER_CLASSES =
            TABS.register("cyberclasses", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.cyberclasses"))
                            .icon(() -> new ItemStack(ModItems.RARE_CLASS_BOOK.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.COMMON_CLASS_BOOK.get());
                                output.accept(ModItems.UNCOMMON_CLASS_BOOK.get());
                                output.accept(ModItems.RARE_CLASS_BOOK.get());
                                output.accept(ModItems.LEGENDARY_CLASS_BOOK.get());
                            })
                            .build());

    private ModCreativeTabs() {}
}

package com.tterrag.blur.core;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import cpw.mods.fml.relauncher.FMLLaunchHandler;

@LateMixin
public final class LateMixinLoader implements ILateMixinLoader {
    @Override public String getMixinConfig() { return "mixins.blur.late.json"; }
    @Override public List<String> getMixins(Set<String> loadedMods) {
        return FMLLaunchHandler.side().isClient() && loadedMods.contains("modularui2")
            ? Collections.singletonList("MixinModularDarkBackground") : Collections.emptyList();
    }
}

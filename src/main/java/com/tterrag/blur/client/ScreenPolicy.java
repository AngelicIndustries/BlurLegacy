package com.tterrag.blur.client;

import com.tterrag.blur.config.BlurConfig;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;

public final class ScreenPolicy {
    private ScreenPolicy() {}
    public static boolean matches(Class<?> screen, String[] names) {
        for (Class<?> type = screen; type != null; type = type.getSuperclass()) {
            for (String name : names) if (type.getName().equals(name)) return true;
        }
        return false;
    }
    public static boolean enabled(GuiScreen screen, boolean world) {
        if (!BlurConfig.enabled || screen == null || matches(screen.getClass(), BlurConfig.exclusions)) return false;
        if (!world) return screen instanceof GuiMainMenu && BlurConfig.title;
        if (matches(screen.getClass(), BlurConfig.inclusions)) return true;
        if (screen instanceof GuiChat) return false;
        if (screen instanceof GuiGameOver) return BlurConfig.death;
        if (screen instanceof GuiScreenBook) return BlurConfig.books;
        if (screen instanceof GuiEditSign) return BlurConfig.signs;
        if (screen instanceof GuiCommandBlock) return BlurConfig.commandBlocks;
        if (screen instanceof GuiContainer) return BlurConfig.containers;
        return true;
    }
}

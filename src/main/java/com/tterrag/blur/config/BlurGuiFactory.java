package com.tterrag.blur.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public final class BlurGuiFactory implements IModGuiFactory {
    @Override public void initialize(Minecraft minecraft) {}
    @Override public Class<? extends GuiScreen> mainConfigGuiClass() { return Screen.class; }
    @Override public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
    @Override public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) { return null; }
    public static final class Screen extends GuiConfig {
        public Screen(GuiScreen parent) { super(parent, elements(), "blur", false, false, "Blur Legacy"); }
        private static List<IConfigElement> elements() {
            List<IConfigElement> result = new ArrayList<>();
            for (String category : new String[] { "general", "screens", "style", "performance" }) {
                result.add(new ConfigElement(BlurConfig.file.getCategory(category)));
            }
            return result;
        }
    }
}

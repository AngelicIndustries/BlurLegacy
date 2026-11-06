package com.tterrag.blur.client;

import com.tterrag.blur.CommonProxy;
import com.tterrag.blur.config.BlurConfig;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public final class ClientProxy extends CommonProxy {
    @Override public void preInit(FMLPreInitializationEvent event) {
        BlurConfig.load(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
            .registerReloadListener(manager -> BlurRenderer.reset());
    }
    @SubscribeEvent public void onFrame(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) BlurRenderer.beginFrame();
    }
    @SubscribeEvent public void onConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
        if ("blur".equals(event.modID)) { BlurConfig.sync(); BlurRenderer.reset(); }
    }
    /**
     * The gradient goes behind every blurred screen, not only those that draw the vanilla background: books, for one,
     * don't. A screen that does finds it already drawn this frame.
     */
    @SubscribeEvent public void onScreenBackground(GuiScreenEvent.DrawScreenEvent.Pre event) {
        BlurRenderer.background(event.gui);
    }
    @SubscribeEvent public void onScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (BlurConfig.showScreenID) Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(
            event.gui.getClass().getName(), 2, 2, 0x80DFFF);
    }
}

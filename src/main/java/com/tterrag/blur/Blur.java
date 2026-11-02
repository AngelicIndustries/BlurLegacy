package com.tterrag.blur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Blur.MODID, name = "Blur Legacy", version = Tags.VERSION,
    acceptedMinecraftVersions = "[1.7.10]", acceptableRemoteVersions = "*",
    dependencies = "required-after:unimixins@[0.3.1,);",
    guiFactory = "com.tterrag.blur.config.BlurGuiFactory",
    customProperties = {
        @Mod.CustomProperty(k = "license", v = "MIT"),
        @Mod.CustomProperty(k = "issueTrackerUrl", v = "https://github.com/AngelicIndustries/BlurLegacy/issues"),
        @Mod.CustomProperty(k = "iconFile", v = "assets/blur/logo.png"),
        @Mod.CustomProperty(k = "backgroundFile", v = "assets/blur/background.png") })
public final class Blur {
    public static final String MODID = "blur";
    public static final Logger LOG = LogManager.getLogger("Blur");
    @SidedProxy(clientSide = "com.tterrag.blur.client.ClientProxy", serverSide = "com.tterrag.blur.CommonProxy")
    public static CommonProxy proxy;
    @Mod.EventHandler public void preInit(FMLPreInitializationEvent event) { proxy.preInit(event); }
}

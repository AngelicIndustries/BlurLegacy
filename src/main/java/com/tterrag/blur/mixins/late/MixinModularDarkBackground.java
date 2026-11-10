package com.tterrag.blur.mixins.late;

import com.tterrag.blur.client.BlurRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * ModularUI 2 screens skip vanilla's background in a world and darken the screen themselves, over the gradient Blur
 * draws: Blur's gradient takes the place of their darkening as it does of vanilla's background.
 */
@Pseudo
@Mixin(targets = "com.cleanroommc.modularui.screen.ClientScreenHandler", remap = false)
public abstract class MixinModularDarkBackground {
    @Inject(method = "drawDarkBackground", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void blur$background(GuiScreen screen, int tint, CallbackInfo ci) {
        if (BlurRenderer.background(screen)) ci.cancel();
    }
}

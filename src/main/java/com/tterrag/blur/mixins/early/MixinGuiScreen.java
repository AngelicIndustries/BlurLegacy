package com.tterrag.blur.mixins.early;

import com.tterrag.blur.client.BlurRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {
    @Inject(method = "drawWorldBackground", at = @At("HEAD"), cancellable = true, require = 1)
    private void blur$background(int tint, CallbackInfo ci) {
        if (BlurRenderer.background((GuiScreen) (Object) this)) ci.cancel();
    }
}

package com.tterrag.blur.mixins.early;

import com.tterrag.blur.client.BlurRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu {
    @Inject(method = "drawScreen", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/gui/GuiMainMenu;renderSkybox(IIF)V", shift = At.Shift.AFTER), require = 1)
    private void blur$title(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlurRenderer.renderTitle((GuiScreen) (Object) this);
    }
}

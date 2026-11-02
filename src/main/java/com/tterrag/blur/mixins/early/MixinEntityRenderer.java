package com.tterrag.blur.mixins.early;

import com.tterrag.blur.client.BlurRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Inject(method = "updateCameraAndRender", at = @At(value = "FIELD",
        target = "Lnet/minecraft/client/renderer/EntityRenderer;renderEndNanoTime:J",
        opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER), require = 1)
    private void blur$afterWorld(float partialTicks, CallbackInfo ci) { BlurRenderer.renderWorld(); }
}

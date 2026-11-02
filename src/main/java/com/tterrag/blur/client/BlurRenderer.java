package com.tterrag.blur.client;

import java.awt.Color;
import com.tterrag.blur.Blur;
import com.tterrag.blur.animation.Transition;
import com.tterrag.blur.config.BlurConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public final class BlurRenderer extends Gui {
    private static final BlurRenderer GUI = new BlurRenderer();
    private static final BlurPipeline PIPELINE = new BlurPipeline();
    private static final Transition BLUR = new Transition();
    private static final Transition BACKGROUND = new Transition();
    private static boolean failed, rendered, gradientRendered;
    private static Object world;
    private BlurRenderer() {}

    public static void beginFrame() {
        Minecraft mc = Minecraft.getMinecraft();
        rendered = gradientRendered = false;
        if (world != mc.theWorld || !BlurConfig.enabled) {
            reset();
            world = mc.theWorld;
        }
        GuiScreen screen = mc.currentScreen;
        boolean enabled = ScreenPolicy.enabled(screen, mc.theWorld != null);
        boolean background = enabled && (!(screen instanceof GuiMainMenu) || BlurConfig.darkenTitle);
        float target = enabled ? 1 : 0;
        if (enabled && screen instanceof GuiContainer && BlurConfig.reduceInGameBlur) target = 0.5F;
        long now = System.nanoTime();
        BLUR.update(target, now, BlurConfig.fadeIn, BlurConfig.fadeOut, BlurConfig.blurCurve);
        BACKGROUND.update(background && BlurConfig.useGradient ? 1 : 0, now,
            BlurConfig.fadeIn, BlurConfig.fadeOut, BlurConfig.backgroundCurve);
    }

    /** Called after world post-processing and before the HUD, including during the closing fade. */
    public static void renderWorld() {
        renderBlur();
        if (Minecraft.getMinecraft().currentScreen == null) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.entityRenderer.setupOverlayRendering();
            ScaledResolution size = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            gradient(size.getScaledWidth(), size.getScaledHeight());
        }
    }

    public static void renderTitle(GuiScreen screen) {
        if (!ScreenPolicy.enabled(screen, false)) return;
        renderBlur();
        if (BlurConfig.darkenTitle) gradient(screen.width, screen.height);
    }

    private static void renderBlur() {
        if (rendered || failed || !BlurConfig.enabled || !OpenGlHelper.shadersSupported || !OpenGlHelper.isFramebufferEnabled()) return;
        rendered = true;
        float radius = BlurConfig.radius * BLUR.value();
        if (radius < 0.01F) { PIPELINE.close(); return; }
        try {
            PIPELINE.render(radius, BlurPipeline.scaleFor(BlurConfig.radius, BlurConfig.downsample));
        } catch (Exception error) {
            failed = true;
            PIPELINE.close();
            Blur.LOG.error("Blur rendering failed; keeping menu backgrounds. Reload resources or change Blur settings to retry.", error);
        }
    }

    public static boolean background(GuiScreen screen) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!BlurConfig.useGradient || !ScreenPolicy.enabled(screen, mc.theWorld != null) || mc.theWorld == null) return false;
        gradient(screen.width, screen.height);
        return true;
    }

    private static void gradient(int width, int height) {
        if (gradientRendered || !BlurConfig.enabled || !BlurConfig.useGradient || BACKGROUND.value() < 0.001F) return;
        gradientRendered = true;
        int mode = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        try {
            GL11.glTranslatef(width / 2F, height / 2F, 0);
            GL11.glRotatef(BlurConfig.rotation, 0, 0, 1);
            float diagonal = (float) Math.hypot(width, height);
            float scale = diagonal / Math.max(1, Math.min(width, height));
            GL11.glScalef(scale, scale, 1);
            GUI.drawGradientRect(-width / 2 - 1, -height / 2 - 1, width / 2 + 1, height / 2 + 1, color(false), color(true));
        } finally {
            GL11.glPopMatrix();
            GL11.glPopAttrib();
            GL11.glMatrixMode(mode);
        }
    }

    private static int color(boolean second) {
        int argb = second ? BlurConfig.gradientEnd : BlurConfig.gradientStart;
        int rgb = argb & 0xFFFFFF;
        if (BlurConfig.rainbow) {
            float hue = (System.nanoTime() % 10_000_000_000L) / 10_000_000_000F;
            rgb = Color.HSBtoRGB(hue + (second ? 0.15F : 0), 0.6F, 0.8F) & 0xFFFFFF;
        }
        return Math.round((argb >>> 24) * BACKGROUND.value()) << 24 | rgb;
    }

    public static void reset() {
        PIPELINE.close();
        BLUR.reset();
        BACKGROUND.reset();
        failed = false;
    }
}

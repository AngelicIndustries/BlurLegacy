package com.tterrag.blur.config;

import static org.junit.Assert.*;
import org.junit.Test;

public class BlurConfigTest {
    @Test public void migratesExistingConfigWithoutChangingOldSettings() throws Exception {
        java.io.File file = java.io.File.createTempFile("blur-migration", ".cfg");
        java.lang.reflect.Field home = cpw.mods.fml.relauncher.FMLInjectionData.class.getDeclaredField("minecraftHome");
        home.setAccessible(true);
        Object previousHome = home.get(null);
        home.set(null, file.getParentFile());
        try {
            java.nio.file.Files.write(file.toPath(), ("general {\n I:radius=23\n I:fadeTime=0\n S:gradientStartColor=CC123456\n"
                + " S:guiExclusions <\n example.LegacyScreen\n >\n}\n").getBytes(java.nio.charset.StandardCharsets.UTF_8));
            BlurConfig.load(file);
            assertEquals(23, BlurConfig.radius);
            assertEquals(0, BlurConfig.fadeIn);
            assertEquals(0xCC123456, BlurConfig.gradientStart);
            assertArrayEquals(new String[] { "example.LegacyScreen" }, BlurConfig.exclusions);
            assertTrue(BlurConfig.enabled);
            assertEquals(200, BlurConfig.fadeOut);
        } finally { home.set(null, previousHome); java.nio.file.Files.deleteIfExists(file.toPath()); }
    }
    @Test public void acceptsOldUnsignedArgbColors() {
        assertEquals(0xFF804020, BlurConfig.parseColor("FF804020"));
        assertEquals(0x75000000, BlurConfig.parseColor(" #75000000 "));
        assertEquals(0x00804020, BlurConfig.parseColor("0x00804020"));
    }
    @Test(expected = IllegalArgumentException.class) public void rejectsMalformedColors() { BlurConfig.parseColor("red"); }
    @Test(expected = IllegalArgumentException.class) public void rejectsTruncatedColors() { BlurConfig.parseColor("FFFFFF"); }
}

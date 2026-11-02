package com.tterrag.blur.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ScreenPolicyTest {
    static class Base {}
    static class Child extends Base {}
    @Test public void exclusionsCoverSubclassesWithoutLoadingNamedMods() {
        assertTrue(ScreenPolicy.matches(Child.class, new String[] { Base.class.getName() }));
        assertFalse(ScreenPolicy.matches(Base.class, new String[] { Child.class.getName() }));
        assertFalse(ScreenPolicy.matches(Child.class, new String[] { "missing.mod.Screen" }));
    }
    @Test public void automaticDownsamplingHasFullResolutionFallback() {
        assertEquals(1, BlurPipeline.scaleFor(2, 0));
        assertEquals(2, BlurPipeline.scaleFor(12, 0));
        assertEquals(4, BlurPipeline.scaleFor(100, 0));
        assertEquals(1, BlurPipeline.scaleFor(100, 1));
    }
}

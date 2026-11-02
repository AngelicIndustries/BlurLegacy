package com.tterrag.blur.animation;

import static org.junit.Assert.*;
import org.junit.Test;
import com.tterrag.blur.animation.Transition.Easing;

public class TransitionTest {
    @Test public void nestedMenusDoNotRestartFade() {
        Transition t = new Transition();
        assertEquals(0, t.update(1, 0, 200, 200, Easing.LINEAR), 0);
        assertEquals(0.5F, t.update(1, 100_000_000, 200, 200, Easing.LINEAR), 0.001F);
        assertEquals(1, t.update(1, 200_000_000, 200, 200, Easing.LINEAR), 0);
    }
    @Test public void rapidReversalStartsAtCurrentValue() {
        Transition t = new Transition();
        t.update(1, 0, 200, 200, Easing.LINEAR);
        assertEquals(0.5F, t.update(0, 100_000_000, 200, 200, Easing.LINEAR), 0.001F);
        assertEquals(0.25F, t.update(1, 200_000_000, 200, 200, Easing.LINEAR), 0.001F);
        assertEquals(0.625F, t.update(1, 300_000_000, 200, 200, Easing.LINEAR), 0.001F);
    }
    @Test public void zeroDurationIsInstantAndFinite() {
        Transition t = new Transition();
        assertEquals(1, t.update(1, 0, 0, 0, Easing.SINE), 0);
        assertEquals(0, t.update(0, 0, 0, 0, Easing.SINE), 0);
    }
    @Test public void reducedStrengthTransitionsWithoutReset() {
        Transition t = new Transition();
        t.update(1, 0, 100, 100, Easing.LINEAR);
        assertEquals(1, t.update(0.5F, 100_000_000, 100, 100, Easing.LINEAR), 0);
        assertEquals(0.75F, t.update(0.5F, 150_000_000, 100, 100, Easing.LINEAR), 0);
        assertEquals(0.5F, t.update(0.5F, 200_000_000, 100, 100, Easing.LINEAR), 0);
    }
    @Test public void allCurvesStayBoundedAndFinishAfterLongPause() {
        for (Easing curve : Easing.values()) {
            Transition t = new Transition();
            t.update(1, 0, 200, 200, curve);
            for (int i = 0; i < 200; i++) {
                float v = t.update(1, i * 1_000_000L, 200, 200, curve);
                assertTrue(v >= 0 && v <= 1);
            }
            assertEquals(1, t.update(1, 10_000_000_000L, 200, 200, curve), 0);
            t.reset();
            assertEquals(0, t.value(), 0);
        }
    }
}

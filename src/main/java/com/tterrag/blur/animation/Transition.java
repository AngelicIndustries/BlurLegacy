package com.tterrag.blur.animation;

/** Wall-clock transitions keep moving while singleplayer is paused. */
public final class Transition {
    public enum Easing {
        LINEAR, SINE, QUAD, CUBIC;
        public float apply(float x) {
            switch (this) {
                case SINE: return (float) ((1 - Math.cos(Math.PI * x)) / 2);
                case QUAD: return x * x;
                case CUBIC: return x * x * (3 - 2 * x);
                default: return x;
            }
        }
    }
    private float start, target, value;
    private long started;
    public float update(float next, long now, int fadeIn, int fadeOut, Easing easing) {
        advance(now, fadeIn, fadeOut, easing);
        if (next != target) {
            start = value;
            target = next;
            started = now;
        }
        advance(now, fadeIn, fadeOut, easing);
        return value;
    }
    private void advance(long now, int fadeIn, int fadeOut, Easing easing) {
        int duration = target > start ? fadeIn : fadeOut;
        float elapsed = duration == 0 ? 1 : Math.min(1, Math.max(0, (now - started) / (duration * 1_000_000F)));
        value = start + (target - start) * easing.apply(elapsed);
    }
    public float value() { return value; }
    public void reset() { start = target = value = 0; started = 0; }
}

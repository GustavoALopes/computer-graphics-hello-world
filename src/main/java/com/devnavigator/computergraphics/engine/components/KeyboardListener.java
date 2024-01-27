package com.devnavigator.computergraphics.engine.components;

import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class KeyboardListener {

    private static Map<Integer, Boolean> keys = new ConcurrentHashMap<>();

    private static EnumMap<Key, Integer> converterKeys = new EnumMap<>(Map.of(
            Key.A, GLFW.GLFW_KEY_A,
            Key.W, GLFW.GLFW_KEY_W,
            Key.S, GLFW.GLFW_KEY_S,
            Key.D, GLFW.GLFW_KEY_D,
            Key.Q, GLFW.GLFW_KEY_Q,
            Key.E, GLFW.GLFW_KEY_E
    ));

    public KeyboardListener() {
    }

    public boolean isKeyDown(
        final Key key
    ) {
        final var glfwKey = converterKeys.get(key);
        return Optional.ofNullable(keys.get(glfwKey))
                .map(val -> val.booleanValue())
                .orElse(false);
    }

    public void move(final long windowId, final int key, final int scanCode, final int action, final int mods) {
        if(action == GLFW.GLFW_PRESS) {
            keys.put(key, true);
        } else if (action == GLFW.GLFW_RELEASE) {
            keys.put(key, false);
        }
    }

    public void bindWindow(final Window window) {
        window.addCallbackListener(this::move);
    }

    public enum Key {
        A(GLFW.GLFW_KEY_A), W(GLFW.GLFW_KEY_W), S(GLFW.GLFW_KEY_S), D(GLFW.GLFW_KEY_D), Q(GLFW.GLFW_KEY_Q), E(GLFW.GLFW_KEY_E);

        private int value;

        Key(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

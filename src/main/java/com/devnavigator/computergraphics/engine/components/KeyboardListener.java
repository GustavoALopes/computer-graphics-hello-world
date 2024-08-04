package com.devnavigator.computergraphics.engine.components;

import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class KeyboardListener {

    private static Map<Integer, Boolean> keys = new ConcurrentHashMap<>();

    private static EnumMap<Key, Integer> converterKeys;

    public KeyboardListener() {
        final var mapper = new HashMap<Key, Integer>(128);
        mapper.put(Key.A, GLFW.GLFW_KEY_A);
        mapper.put(Key.W, GLFW.GLFW_KEY_W);
        mapper.put(Key.S, GLFW.GLFW_KEY_S);
        mapper.put(Key.D, GLFW.GLFW_KEY_D);
        mapper.put(Key.Q, GLFW.GLFW_KEY_Q);
        mapper.put(Key.E, GLFW.GLFW_KEY_E);
        mapper.put(Key.NUM_KP_4, GLFW.GLFW_KEY_KP_4);
        mapper.put(Key.NUM_KP_6, GLFW.GLFW_KEY_KP_6);
        mapper.put(Key.NUM_KP_8, GLFW.GLFW_KEY_KP_8);
        mapper.put(Key.NUM_KP_5, GLFW.GLFW_KEY_KP_5);
        mapper.put(Key.NUM_KP_7, GLFW.GLFW_KEY_KP_7);
        mapper.put(Key.NUM_KP_9, GLFW.GLFW_KEY_KP_9);
        mapper.put(Key.SPACE, GLFW.GLFW_KEY_SPACE);

        converterKeys = new EnumMap<>(mapper);
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
        window.addKeyboardCallbackListener(this::move);
    }

    public enum Key {
        A(GLFW.GLFW_KEY_A), W(GLFW.GLFW_KEY_W), S(GLFW.GLFW_KEY_S), D(GLFW.GLFW_KEY_D), Q(GLFW.GLFW_KEY_Q), E(GLFW.GLFW_KEY_E),
        NUM_KP_4(GLFW.GLFW_KEY_KP_4),
        NUM_KP_6(GLFW.GLFW_KEY_KP_6),
        NUM_KP_8(GLFW.GLFW_KEY_KP_8),
        NUM_KP_5(GLFW.GLFW_KEY_KP_5),
        NUM_KP_7(GLFW.GLFW_KEY_KP_7),
        NUM_KP_9(GLFW.GLFW_KEY_KP_9),
        SPACE(GLFW.GLFW_KEY_SPACE);

        private int value;

        Key(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

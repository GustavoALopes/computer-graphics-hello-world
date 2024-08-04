package com.devnavigator.computergraphics.engine.components;

import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MouseListener {

    private static double scrollOffSetX;

    private static double scrollOffSetY;

    private static double x;

    private static double dx;

    private static double y;

    private static double dy;

    private static final ConcurrentMap<Integer, Boolean> state = new ConcurrentHashMap<>(3);

    static {
        state.put(Buttons.Right.value, false);
        state.put(Buttons.Left.value, false);
        state.put(Buttons.Scroll.value, false);
    }


    public static void bindWindow(final Window window) {
        window.addMouseCallbackListener(
                MouseListener::buttonsUpdate,
                MouseListener::scrollUpdate,
                MouseListener::cursorUpdate
        );
    }

    public static void reset() {
        scrollOffSetY = 0;
        scrollOffSetX = 0;
        dy = 0;
        dx = 0;
    }

    private static void cursorUpdate(
            final long windowId,
            final double xpos,
            final double ypos
    ) {
        dx = (xpos - x);
        dy = (ypos - y);
        x = xpos;
        y = ypos;
    }

    private static void scrollUpdate(
            final long windowId,
            final double xOffset,
            final double yOffset
    ) {
        System.out.printf("Scroll values are x: %s and y: %s \n", xOffset, yOffset);
        scrollOffSetX = xOffset;
        scrollOffSetY = yOffset;
    }

    private static void buttonsUpdate(
            final long windowId,
            final int button,
            final int action,
            final int mods
    ) {
       if(action == GLFW.GLFW_PRESS) {
           state.put(button, true);
       } else if(action == GLFW.GLFW_RELEASE) {
           state.put(button, false);
       }
    }

    public static boolean isDown(final Buttons button) {
        return state.get(button.value);
    }

    public static double getScrollOffSet() {
        return scrollOffSetY;
    }

    public static double getDY() {
        return dy;
    }

    public static double getDX() {
        return dx;
    }


    enum Buttons {
        Right(GLFW.GLFW_MOUSE_BUTTON_2),
        Left(GLFW.GLFW_MOUSE_BUTTON_1),
        Scroll(GLFW.GLFW_MOUSE_BUTTON_3);

        private final int value;

        Buttons(int value) {
            this.value = value;
        }
    }

}

package com.kennethfei.sorting_visualizer.internal;

import static imgui.flag.ImGuiConfigFlags.DockingEnable;
import static imgui.flag.ImGuiConfigFlags.NavEnableKeyboard;
import static imgui.flag.ImGuiConfigFlags.ViewportsEnable;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class ImGuiUtil {
    public static ImGuiImplGlfw imGuiImplGlfw;
    public static ImGuiImplGl3 imGuiImplGl3;
    public static ImGuiIO io;

    public static void initialize(Window window) {
        imGuiImplGl3 = new ImGuiImplGl3();
        imGuiImplGlfw = new ImGuiImplGlfw();

        ImGui.createContext();
        io = ImGui.getIO();
        io.setConfigFlags(
                io.getConfigFlags() | NavEnableKeyboard | ViewportsEnable);
        ImGui.styleColorsDark();

        imGuiImplGlfw = new ImGuiImplGlfw();
        imGuiImplGlfw.init(window.getNativeWindow(), true);
        imGuiImplGl3 = new ImGuiImplGl3();
        imGuiImplGl3.init("#version 330 core");
    }
}


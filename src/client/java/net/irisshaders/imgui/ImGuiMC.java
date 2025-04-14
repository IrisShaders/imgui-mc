package net.irisshaders.imgui;

//? if =1.21.5 {
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.GlTexture;
//?}
import com.mojang.blaze3d.systems.RenderSystem;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.internal.ImGuiContext;
import net.irisshaders.imgui.window.ImGuiImplGl3;
import net.irisshaders.imgui.window.ImGuiImplGlfw;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.*;

public class ImGuiMC {
    private static final ImGuiMC INSTANCE = new ImGuiMC();
    private static final boolean DEBUG = false;
    private ImGuiContext context;

    public static ImGuiMC getInstance() {
        return INSTANCE;
    }

    private ImGuiImplGl3 glAccessor;
    private ImGuiImplGlfw glWindow;

    private String tryLoadFromClasspath(final String fullLibName) {
        if (DEBUG) {
            System.out.println("Loading " + fullLibName);
        }

        try (InputStream is = ImGuiMC.class.getResourceAsStream("/" + fullLibName)) {
            if (is == null) {
                throw new IllegalStateException("Could not find library " + fullLibName + " in classpath");
            }

            final Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("imguimc");
            if (!Files.exists(tmpDir)) {
                Files.createDirectories(tmpDir);
            }

            final Path libBin = tmpDir.resolve(fullLibName);

            try {
                Files.copy(is, libBin, StandardCopyOption.REPLACE_EXISTING);
            } catch (AccessDeniedException e) {
                if (!Files.exists(libBin)) {
                    throw e;
                }
            }

            return libBin.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadLibrary() {
        final boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");
        final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
        final String libPrefix;
        final String libSuffix;

        if (isWin) {
            libPrefix = "";
            libSuffix = ".dll";
        } else if (isMac) {
            libPrefix = "lib";
            libSuffix = ".dylib";
        } else {
            libPrefix = "lib";
            libSuffix = ".so";
        }

        String libName = libPrefix + "imgui-java64" + libSuffix;

        System.load(tryLoadFromClasspath(libName));
    }

    @ApiStatus.Internal
    public void onRendererInit(long window) {
        loadLibrary();

        this.context = ImGui.createContext(false);
        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard | ImGuiConfigFlags.ViewportsEnable);
        this.glAccessor = new ImGuiImplGl3();
        this.glWindow = new ImGuiImplGlfw();
        this.glAccessor.init();
        this.glWindow.init(window, false);

        afterPollEvents(window);
    }

    public void afterPollEvents(long l) {
        glAccessor.newFrame();
        glWindow.newFrame();
        ImGui.newFrame();
    }

    public void draw() {
        //? if =1.21.5 {
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, ((GlTexture) Minecraft.getInstance().getMainRenderTarget().getColorTexture()).getFbo(((GlDevice) RenderSystem.getDevice()).directStateAccess(), null));
        //?} else {
        /*Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        *///?}
        ImGui.render();
        glAccessor.renderDrawData(ImGui.getDrawData());

        ImGui.updatePlatformWindows();
        ImGui.renderPlatformWindowsDefault();
        GLFW.glfwMakeContextCurrent(Minecraft.getInstance().getWindow().getWindow());
    }

    public void shutdown() {
        glAccessor.shutdown();
        glWindow.shutdown();
        ImGui.destroyContext();
    }

    public void onMouseMove(long window, double mouseX, double mouseY) {
        glWindow.cursorPosCallback(window, mouseX, mouseY);
    }

    public void onMouseScroll(long window, double scrollX, double scrollY) {
        glWindow.scrollCallback(window, scrollX, scrollY);
    }

    public void onMouseButton(long window, int button, int action, int mods) {
        glWindow.mouseButtonCallback(window, button, action, mods);
    }

    public void monitorCallback(long window, int event) {
        glWindow.monitorCallback(window, event);
    }

    public void cursorEnterCallback(long window, boolean entered) {
        glWindow.cursorEnterCallback(window, entered);
    }

    public void windowFocusCallback(long window, boolean focused) {
        glWindow.windowFocusCallback(window, focused);
    }

    public void onKeyPress(long window, int keycode, int scancode, int action, int mods) {
        glWindow.keyCallback(window, keycode, scancode, action, mods);
    }

    public void onCharTyped(long window, int chara, int j) {
        glWindow.charCallback(window, chara);
    }
}

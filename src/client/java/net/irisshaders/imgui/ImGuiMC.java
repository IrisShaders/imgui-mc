package net.irisshaders.imgui;

//? if >=1.21.5 {
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
    private ImGuiImplGl3 glAccessor;
    private ImGuiImplGlfw glWindow;
    private boolean drawing;

    public static ImGuiMC getInstance() {
        return INSTANCE;
    }

    /**
     * Force sets the context. Usually, you want {@link #startDrawing()} instead.
     */
    public static void setContext() {
        ImGui.setCurrentContext(getInstance().context);
    }

    /**
     * If ready to draw, sets the current context.
     *
     * @return If drawing is allowed currently
     */
    public static boolean startDrawing() {
        if (getInstance().context == null) return false;

        ImGui.setCurrentContext(getInstance().context);

        return getInstance().drawing;
    }

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
    }

    public void afterPollEvents(long l) {
        if (drawing) {
            // The last frame never correctly finished. The level may have changed.
            return;
        }

        glAccessor.newFrame();
        glWindow.newFrame();
        ImGui.newFrame();

        drawing = true;
    }

    public void draw() {
        //? if >=1.21.5 {
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, ((GlTexture) Minecraft.getInstance().getMainRenderTarget().getColorTexture()).getFbo(((GlDevice) RenderSystem.getDevice()).directStateAccess(), null));
        //?} else {
        /*Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        *///?}
        ImGui.render();
        drawing = false;

        glAccessor.renderDrawData(ImGui.getDrawData());
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);

        ImGui.updatePlatformWindows();
        ImGui.renderPlatformWindowsDefault();
        //? if >1.21.8 {
        GLFW.glfwMakeContextCurrent(Minecraft.getInstance().getWindow().handle());
        //?} else {
        /*GLFW.glfwMakeContextCurrent(Minecraft.getInstance().getWindow().getWindow());
        *///?}
    }

    private static boolean isGone;

    public void shutdown() {
        if (isGone) {
            throw new IllegalStateException("Cannot shutdown twice!");
        }
        isGone = true;
        setContext();
        glAccessor.shutdown();
        glWindow.shutdown();
        ImGui.destroyContext();
        context = null;
    }

    public void onMouseMove(long window, double mouseX, double mouseY) {
        if (isGone) return;

        setContext();
        glWindow.cursorPosCallback(window, mouseX, mouseY);
    }

    public void onMouseScroll(long window, double scrollX, double scrollY) {
        if (isGone) return;

        setContext();
        glWindow.scrollCallback(window, scrollX, scrollY);
    }

    public void onMouseButton(long window, int button, int action, int mods) {
        if (isGone) return;

        setContext();
        glWindow.mouseButtonCallback(window, button, action, mods);
    }

    public void monitorCallback(long window, int event) {
        if (isGone) return;

        setContext();
        glWindow.monitorCallback(window, event);
    }

    public void cursorEnterCallback(long window, boolean entered) {
        if (isGone) return;

        setContext();
        glWindow.cursorEnterCallback(window, entered);
    }

    public void windowFocusCallback(long window, boolean focused) {
        if (isGone) return;

        setContext();
        glWindow.windowFocusCallback(window, focused);
    }

    public void onKeyPress(long window, int keycode, int scancode, int action, int mods) {
        if (isGone) return;

        setContext();
        glWindow.keyCallback(window, keycode, scancode, action, mods);
    }

    public void recreateFonts() {
        glAccessor.destroyFontsTexture();
        glAccessor.createFontsTexture();
    }

    public void onCharTyped(long window, int chara, int j) {
        glWindow.charCallback(window, chara);
    }
}
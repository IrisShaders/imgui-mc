package net.irisshaders.imgui.mixin;

import com.mojang.blaze3d.platform.Window;
import imgui.ImGui;
import net.irisshaders.imgui.ImGuiMC;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class DrawMixin {
    @Shadow @Final private Window window;

    @Inject(at = @At("HEAD"), method = "runTick", remap = false)
    private void imgui$newFrame(
            boolean bl, CallbackInfo ci
    ) {
        ImGuiMC.getInstance().afterPollEvents(this.window.getWindow());
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = /*? if >= 1.21.5 {*/"Lcom/mojang/blaze3d/platform/Window;isMinimized()Z"/*?} else {*//*"Lcom/mojang/blaze3d/pipeline/RenderTarget;unbindWrite()V"*//*?}*/))
    private void imgui$draw(boolean bl, CallbackInfo ci) {
        if (ImGuiMC.startDrawing()) {
            ImGui.showDemoWindow();
            ImGuiMC.getInstance().draw();
        }
    }

    @Inject(method = "stop", at = @At("HEAD"))
    private void imgui$shutdown(CallbackInfo ci) {
        ImGuiMC.getInstance().shutdown();
    }
}

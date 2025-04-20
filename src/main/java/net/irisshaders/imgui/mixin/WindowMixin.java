package net.irisshaders.imgui.mixin;

import com.mojang.blaze3d.platform.Window;
import net.irisshaders.imgui.ImGuiMC;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
    @Inject(method = "onEnter", at = @At("HEAD"))
    private void imgui$onEnter(long l, boolean bl, CallbackInfo ci) {
        ImGuiMC.getInstance().cursorEnterCallback(l, bl);
    }

    @Inject(method = "onFocus", at = @At("HEAD"))
    private void imgui$onFocus(long l, boolean bl, CallbackInfo ci) {
        ImGuiMC.getInstance().windowFocusCallback(l, bl);
    }
}

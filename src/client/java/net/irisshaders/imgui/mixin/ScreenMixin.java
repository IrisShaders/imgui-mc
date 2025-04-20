package net.irisshaders.imgui.mixin;

import com.mojang.blaze3d.platform.ScreenManager;
import net.irisshaders.imgui.ImGuiMC;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenManager.class)
public class ScreenMixin {
    @Inject(method = "onMonitorChange", at = @At("HEAD"))
    private void imgui$onMonitorChange(long window, int event, CallbackInfo ci) {
        ImGuiMC.getInstance().monitorCallback(window, event);
    }
}

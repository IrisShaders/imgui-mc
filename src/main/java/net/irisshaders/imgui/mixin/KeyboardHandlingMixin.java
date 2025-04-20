package net.irisshaders.imgui.mixin;

import net.irisshaders.imgui.ImGuiMC;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlingMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void imgui$keyPress(long l, int i, int j, int k, int m, CallbackInfo ci) {
        ImGuiMC.getInstance().onKeyPress(l, i, j, k, m);
    }

    @Inject(method = "charTyped", at = @At("HEAD"))
    private void imgui$charTyped(long l, int i, int j, CallbackInfo ci) {
        ImGuiMC.getInstance().onCharTyped(l, i, j);
    }
}

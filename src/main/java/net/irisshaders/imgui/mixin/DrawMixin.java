package net.irisshaders.imgui.mixin;

import imgui.ImGui;
import net.irisshaders.imgui.ImGuiMC;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class DrawMixin {
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = /*? if >= 1.21 {*/"Lnet/minecraft/client/renderer/GameRenderer;render(Lnet/minecraft/client/DeltaTracker;Z)V"/*?} else {*//*"Lnet/minecraft/client/renderer/GameRenderer;render(FJZ)V"*//*?}*/, shift = At.Shift.AFTER))
    private void imgui$draw(boolean bl, CallbackInfo ci) {
        ImGui.showDemoWindow();
        ImGuiMC.getInstance().draw();
    }
}

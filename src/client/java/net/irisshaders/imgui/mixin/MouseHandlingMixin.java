package net.irisshaders.imgui.mixin;

import net.irisshaders.imgui.ImGuiMC;
import net.minecraft.client.MouseHandler;
//? if >1.21.8 {
import net.minecraft.client.input.MouseButtonInfo;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlingMixin {
    @Inject(method = "onMove", at = @At("HEAD"))
    private void imgui$onMove(long window, double mouseX, double mouseY, CallbackInfo ci) {
        ImGuiMC.getInstance().onMouseMove(window, mouseX, mouseY);
    }

    @Inject(method = "onScroll", at = @At("HEAD"))
    private void imgui$onScroll(long window, double scrollX, double scrollY, CallbackInfo ci) {
        ImGuiMC.getInstance().onMouseScroll(window, scrollX, scrollY);
    }

    //? if >1.21.8 {
    @Inject(method = "onButton", at = @At("HEAD"))
    private void imgui$onScroll(long window, MouseButtonInfo button, int action, CallbackInfo ci) {
        ImGuiMC.getInstance().onMouseButton(window, button.button(), action, button.modifiers());
    }
    //?} else {
    /*@Inject(method = "onPress", at = @At("HEAD"))
    private void imgui$onScroll(long window, int button, int action, int mods, CallbackInfo ci) {
        ImGuiMC.getInstance().onMouseButton(window, button, action, mods);
    }
    *///?}
}

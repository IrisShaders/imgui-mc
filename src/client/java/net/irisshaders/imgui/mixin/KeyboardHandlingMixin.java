package net.irisshaders.imgui.mixin;

import net.irisshaders.imgui.ImGuiMC;
import net.minecraft.client.KeyboardHandler;
//? if >1.21.8 {
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlingMixin {
    //? if >1.21.8 {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void imgui$keyPress(long window, int action, KeyEvent keyEvent, CallbackInfo ci) {
        ImGuiMC.getInstance().onKeyPress(window, keyEvent.key(), keyEvent.scancode(), action, keyEvent.modifiers());
    }

    @Inject(method = "charTyped", at = @At("HEAD"))
    private void imgui$charTyped(long l, CharacterEvent characterEvent, CallbackInfo ci) {
        ImGuiMC.getInstance().onCharTyped(l, characterEvent.codepoint(), characterEvent.modifiers());
    }
    //?} else {
    /*@Inject(method = "keyPress", at = @At("HEAD"))
    private void imgui$keyPress(long l, int i, int j, int k, int m, CallbackInfo ci) {
        ImGuiMC.getInstance().onKeyPress(l, i, j, k, m);
    }

    @Inject(method = "charTyped", at = @At("HEAD"))
    private void imgui$charTyped(long l, int i, int j, CallbackInfo ci) {
        ImGuiMC.getInstance().onCharTyped(l, i, j);
    }
    *///?}
}

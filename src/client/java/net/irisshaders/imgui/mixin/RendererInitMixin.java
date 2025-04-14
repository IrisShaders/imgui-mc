package net.irisshaders.imgui.mixin;

//? if >=1.21.5 {
import com.mojang.blaze3d.TracyFrameCapture;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.systems.RenderSystem;
//?} else {
/*import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
*///?}
import net.irisshaders.imgui.ImGuiMC;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(RenderSystem.class)
public class RendererInitMixin {
	@Inject(at = @At("RETURN"), method = "initRenderer", remap = false)
	//? if = 1.21.5 {
	private static void imgui$initRenderer(long window, int i, boolean bl, BiFunction<ResourceLocation, ShaderType, String> biFunction, boolean bl2, CallbackInfo ci) {
		ImGuiMC.getInstance().onRendererInit(window);
	}
	//?} else {
	/*private static void imgui$initRenderer(int i, boolean bl, CallbackInfo ci) {
		ImGuiMC.getInstance().onRendererInit(Minecraft.getInstance().getWindow().getWindow());
	}
	*///?}

	@Inject(at = @At("RETURN"), method = "flipFrame", remap = false)
	private static void imgui$newFrame(
		long l,
		//? if >= 1.21.5
		TracyFrameCapture tracyFrameCapture,
		CallbackInfo ci
	) {
		ImGuiMC.getInstance().afterPollEvents(l);
	}

}
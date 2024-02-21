package useless.btabreeding.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.core.entity.EntityLiving;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.btabreeding.IBreeding;

@Mixin(value = LivingRenderer.class, remap = false)
public abstract class LivingRendererMixin<T extends EntityLiving> extends EntityRenderer<T> {
	@Inject(method = "render(Lnet/minecraft/core/entity/EntityLiving;DDDFF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glScalef(FFF)V", shift = At.Shift.AFTER))
	private void makeSmall(T entity, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci){
		if (entity instanceof IBreeding && ((IBreeding) entity).btabreeding$isBaby()){
			GL11.glScalef(0.66f, 0.66f, 0.66f);
		}
	}
}

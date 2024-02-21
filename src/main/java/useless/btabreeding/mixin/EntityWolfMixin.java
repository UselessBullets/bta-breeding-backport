package useless.btabreeding.mixin;

import net.minecraft.core.entity.animal.EntityWolf;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityWolf.class, remap = false)
public class EntityWolfMixin extends EntityAnimalMixin{
	public EntityWolfMixin(World world) {
		super(world);
	}
	@Override
	public boolean btabreeding$isFoodItem(ItemStack stack) {
		return stack != null && stack.getItem() instanceof ItemFood && ((ItemFood) stack.getItem()).getIsWolfsFavoriteMeat();
	}
}

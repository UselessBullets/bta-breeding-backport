package useless.btabreeding.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.animal.EntityPig;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityPig.class, remap = false)
public class EntityPigMixin extends EntityAnimalMixin {
	public EntityPigMixin(World world) {
		super(world);
	}
	@Override
	public boolean btabreeding$isFoodItem(ItemStack stack) {
		return stack != null && (stack.getItem() == Block.pumpkin.asItem() || stack.getItem() == Block.pumpkinCarvedIdle.asItem());
	}
}

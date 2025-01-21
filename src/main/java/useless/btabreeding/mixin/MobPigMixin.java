package useless.btabreeding.mixin;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.animal.MobPig;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MobPig.class, remap = false)
public class MobPigMixin extends MobAnimalMixin {
	public MobPigMixin(World world) {
		super(world);
	}
	@Override
	public boolean btabreeding$isFoodItem(ItemStack stack) {
		return stack != null && (stack.getItem() == Blocks.PUMPKIN.asItem() || stack.getItem() == Blocks.PUMPKIN_CARVED_IDLE.asItem() || stack.getItem() == Blocks.PUMPKIN_CARVED_ACTIVE.asItem());
	}
}

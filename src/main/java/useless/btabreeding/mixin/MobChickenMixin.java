package useless.btabreeding.mixin;

import net.minecraft.core.entity.animal.MobChicken;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MobChicken.class, remap = false)
public class MobChickenMixin extends MobAnimalMixin {
	public MobChickenMixin(World world) {
		super(world);
	}
	@Override
	public boolean btabreeding$isFoodItem(ItemStack stack) {
		return stack != null && (stack.getItem() == Items.SEEDS_WHEAT || stack.getItem() == Items.SEEDS_PUMPKIN);
	}
}

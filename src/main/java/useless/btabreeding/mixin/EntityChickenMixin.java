package useless.btabreeding.mixin;

import net.minecraft.core.entity.animal.EntityChicken;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityChicken.class, remap = false)
public class EntityChickenMixin extends EntityAnimalMixin {
	public EntityChickenMixin(World world) {
		super(world);
	}
	@Override
	public boolean btabreeding$isFoodItem(ItemStack stack) {
		return stack != null && (stack.getItem() == Item.seedsWheat || stack.getItem() == Item.seedsPumpkin);
	}
}

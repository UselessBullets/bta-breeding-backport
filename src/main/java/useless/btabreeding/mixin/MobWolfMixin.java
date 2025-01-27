package useless.btabreeding.mixin;

import net.minecraft.core.entity.MobPathfinder;
import net.minecraft.core.entity.animal.MobAnimal;
import net.minecraft.core.entity.animal.MobWolf;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import useless.btabreeding.BtaBreeding;
import useless.btabreeding.IBreeding;

import java.util.UUID;

@Mixin(value = MobWolf.class, remap = false)
public abstract class MobWolfMixin extends MobAnimalMixin {
	@Shadow
	public abstract UUID getWolfOwner();

	public MobWolfMixin(World world) {
		super(world);
	}
	@Override
	public boolean btabreeding$isFoodItem(ItemStack stack) {
		return stack != null && stack.getItem() instanceof ItemFood && ((ItemFood) stack.getItem()).getIsWolfsFavoriteMeat();
	}
	@Override
	public void btabreeding$spawnBaby(IBreeding partner) {
		if (!world.isClientSide) {
			// Spawn entity
			MobAnimal entity = (MobAnimal) BtaBreeding.createEntity(this.getClass(), world);
			entity.moveTo(x, y, z, 0, 0.0f);
			entity.spawnInit();

			((MobWolf)entity).setWolfOwner(this.getWolfOwner());
			((MobWolf)entity).setWolfTamed(true);

			((IBreeding) entity).btabreeding$setChildTimer(20 * 60 * 5);

			world.entityJoinedWorld(entity);
			this.btabreeding$setFedTimer(0);
			partner.btabreeding$setFedTimer(0);
			this.btabreeding$setBreedingTimer(20*100);
			partner.btabreeding$setBreedingTimer(20*100);
			this.setTarget(null);
			if (partner instanceof MobPathfinder){
				((MobPathfinder) partner).setTarget(null);
			}
		}
	}

    @Override
    public boolean btabreeding$isBreedable() {
        return getHealth() >= getMaxHealth() && super.btabreeding$isBreedable();
    }
}

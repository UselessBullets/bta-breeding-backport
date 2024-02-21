package useless.btabreeding.mixin;

import net.minecraft.core.entity.EntityPathfinder;
import net.minecraft.core.entity.animal.EntityAnimal;
import net.minecraft.core.entity.animal.EntitySheep;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import useless.btabreeding.BtaBreeding;
import useless.btabreeding.IBreeding;

@Mixin(value = EntitySheep.class, remap = false)
public abstract class EntitySheepMixin extends EntityAnimalMixin {
	@Shadow
	public abstract int getFleeceColor();

	public EntitySheepMixin(World world) {
		super(world);
	}
	@Override
	public void btabreeding$spawnBaby(IBreeding partner) {
		if (!world.isClientSide) {
			// Spawn entity
			EntityAnimal entity = (EntityAnimal) BtaBreeding.createEntity(this.getClass(), world);
			entity.moveTo(x, y, z, 0, 0.0f);
			entity.spawnInit();

			((EntitySheep)entity).setFleeceColor(random.nextInt(2) == 0 ? this.getFleeceColor() : ((EntitySheep)partner).getFleeceColor());

			((IBreeding) entity).btabreeding$setChildTimer(20 * 60 * 5);

			world.entityJoinedWorld(entity);
			this.btabreeding$setFedTimer(0);
			partner.btabreeding$setFedTimer(0);
			this.btabreeding$setBreedingTimer(20*100);
			partner.btabreeding$setBreedingTimer(20*100);
			this.setTarget(null);
			if (partner instanceof EntityPathfinder){
				((EntityPathfinder) partner).setTarget(null);
			}
		}
	}
}

package useless.btabreeding.mixin;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityPathfinder;
import net.minecraft.core.entity.animal.EntityAnimal;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.btabreeding.BtaBreeding;
import useless.btabreeding.IBreeding;

import java.util.List;

@Mixin(value = EntityAnimal.class, remap = false)
public class EntityAnimalMixin extends EntityPathfinder implements IBreeding {
	public EntityAnimalMixin(World world) {
		super(world);
	}

	@Unique
	public int breedingTimer = 0;
	@Unique
	public int fedTimer = 0;
	@Unique
	public int childhoodTimer = 0;
	@Unique
	public boolean isPersistent = false;
	@Unique
	public Entity passiveTarget = null;

	@Override
	public int btabreeding$getBreedingTimer() {
		return breedingTimer;
	}

	@Override
	public int btabreeding$getFedTimer() {
		return fedTimer;
	}

	@Override
	public int btabreeding$getChildTimer() {
		return childhoodTimer;
	}

	@Override
	public void btabreeding$setBreedingTimer(int value) {
		this.breedingTimer = value;
	}

	@Override
	public void btabreeding$setFedTimer(int value) {
		this.fedTimer = value;
	}

	@Override
	public void btabreeding$setChildTimer(int value) {
		this.childhoodTimer = value;
	}

	@Override
	public boolean btabreeding$isBreedable() {
		return breedingTimer <= 0 && !btabreeding$isBaby();
	}

	@Override
	public boolean btabreeding$isFed() {
		return fedTimer > 0;
	}

	@Override
	public boolean btabreeding$isFoodItem(ItemStack stack) {
		return stack != null && stack.getItem() == Item.wheat;
	}

	@Override
	public void btabreeding$spawnBaby(IBreeding partner) {
		if (!world.isClientSide) {
			// Spawn entity
			EntityAnimal entity = (EntityAnimal) BtaBreeding.createEntity(this.getClass(), world);
			entity.moveTo(x, y, z, 0, 0.0f);
			entity.spawnInit();

			((IBreeding) entity).btabreeding$setChildTimer(20 * 60 * 5);

			world.entityJoinedWorld(entity);
			this.btabreeding$setFedTimer(0);
			partner.btabreeding$setFedTimer(0);
			this.btabreeding$setBreedingTimer(20*100);
			partner.btabreeding$setBreedingTimer(20*100);
			this.btabreeding$setPassiveTarget(null);
			partner.btabreeding$setPassiveTarget(null);
		}
	}

	@Override
	public boolean btabreeding$isBaby() {
		return btabreeding$getChildTimer() > 0;
	}
	@Override
	public void btabreeding$setPassiveTarget(Entity entity){
		this.passiveTarget = entity;
		if (entity == null){
			pathToEntity = null;
		}
	}
	@Override
	public Entity btabreeding$getPassiveTarget() {
		return passiveTarget;
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		ItemStack item = entityplayer.inventory.getCurrentItem();
		boolean flag = super.interact(entityplayer);
		if (item != null && btabreeding$isFoodItem(item) && (btabreeding$isBreedable() || btabreeding$isBaby()) && item.consumeItem(entityplayer)){
			if (this.btabreeding$isBaby()){
				this.btabreeding$setChildTimer((int) (btabreeding$getChildTimer() * 0.75f));
				double d = this.random.nextGaussian() * 0.02;
				double d1 = this.random.nextGaussian() * 0.02;
				double d2 = this.random.nextGaussian() * 0.02;
				this.world.spawnParticle(
					"soulflame",
					this.x + (double)(this.random.nextFloat() * this.bbWidth * 2.0F) - (double)this.bbWidth,
					this.y + 0.5 + (double)(this.random.nextFloat() * this.bbHeight),
					this.z + (double)(this.random.nextFloat() * this.bbWidth * 2.0F) - (double)this.bbWidth,
					d,
					d1,
					d2
				);
			} else {
				this.btabreeding$setFedTimer(20 * 15);
				isPersistent = true;
			}
			return true;
		}
		return flag;
	}

	@Override
	public void onLivingUpdate() {
		if (breedingTimer > 0){
			breedingTimer--;
		}
		if (btabreeding$getChildTimer() > 0){
			isPersistent = true;
			btabreeding$setChildTimer(btabreeding$getChildTimer()-1);
		}
		if (btabreeding$isFed()){
			fedTimer--;
		}
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.bb.expand(0.2F, 0.0, 0.2F));
		if (list != null && !list.isEmpty() && !isMovementCeased()) {
            for (Entity entity : list) {
                if (entity instanceof IBreeding &&
					entity.getClass().isInstance(this) &&
					this.btabreeding$isFed() &&
					((IBreeding) entity).btabreeding$isFed() &&
					this.btabreeding$isBreedable() &&
					((IBreeding) entity).btabreeding$isBreedable())
				{
                    this.btabreeding$spawnBaby((IBreeding) entity);
					break;
                }
            }
		}

		if (tickCount % 40 == 0 && !isMovementCeased()){
			list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.bb.expand(10F, 10F, 10F));
			if (btabreeding$isBaby() && btabreeding$getPassiveTarget() == null){
				for (Entity entity : list) {
					if (entity instanceof IBreeding &&
						entity.getClass().isInstance(this) &&
					!((IBreeding) entity).btabreeding$isBaby()) {
						this.btabreeding$setPassiveTarget(entity);
						break;
					}
				}
			}
			else if (btabreeding$isFed()){
				this.btabreeding$setPassiveTarget(null);
				for (Entity entity : list) {
					if (entity instanceof IBreeding &&
						entity.getClass().isInstance(this) &&
						this.btabreeding$isFed() &&
						((IBreeding) entity).btabreeding$isFed() &&
						this.btabreeding$isBreedable() &&
						((IBreeding) entity).btabreeding$isBreedable()) {
						this.btabreeding$setPassiveTarget(entity);
						break;
					}
				}

			} else if (btabreeding$isBreedable()) {
				this.btabreeding$setPassiveTarget(null);
				for (Entity entity : list) {
					if (entity instanceof EntityPlayer && btabreeding$isFoodItem(((EntityPlayer) entity).getHeldItem())) {
						this.btabreeding$setPassiveTarget(entity);
						break;
					}
				}
			}

		}

		super.onLivingUpdate();

		if (btabreeding$isFed() && tickCount % 4 == 0){
			double d = this.random.nextGaussian() * 0.02;
			double d1 = this.random.nextGaussian() * 0.02;
			double d2 = this.random.nextGaussian() * 0.02;
			this.world.spawnParticle(
					"heart",
					this.x + (double)(this.random.nextFloat() * this.bbWidth * 2.0F) - (double)this.bbWidth,
					this.y + 0.5 + (double)(this.random.nextFloat() * this.bbHeight),
					this.z + (double)(this.random.nextFloat() * this.bbWidth * 2.0F) - (double)this.bbWidth,
					d,
					d1,
					d2
				);
		}
	}
	@Override
	protected void updatePlayerActionState() {
		if (passiveTarget == null && getCurrentTarget() == null){
			pathToEntity = null;
		}
		if (passiveTarget != null){
			if (passiveTarget instanceof EntityPlayer && distanceToSqr(passiveTarget) < 9){
				pathToEntity = null;
			} else {
				pathToEntity = world.getPathToEntity(this, passiveTarget, 20);
			}
		}
		super.updatePlayerActionState();
	}
	@Inject(method = "addAdditionalSaveData(Lcom/mojang/nbt/CompoundTag;)V", at = @At("TAIL"))
	private void saveData(CompoundTag tag, CallbackInfo ci){
		tag.putInt("breeding$breedtime", breedingTimer);
		tag.putInt("breeding$fedtime", fedTimer);
		tag.putInt("breeding$childtime", childhoodTimer);
		tag.putBoolean("breeding$persistent", isPersistent);
	}

	@Inject(method = "readAdditionalSaveData(Lcom/mojang/nbt/CompoundTag;)V", at = @At("TAIL"))
	private void loadData(CompoundTag tag, CallbackInfo ci){
		this.breedingTimer = tag.getInteger("breeding$breedtime");
		this.fedTimer =	tag.getInteger("breeding$fedtime");
		this.childhoodTimer = tag.getInteger("breeding$childtime");
		this.isPersistent = tag.getBoolean("breeding$persistent");
	}
	@Override
	public boolean canDespawn(){
		return super.canDespawn() && !isPersistent;
	}
}

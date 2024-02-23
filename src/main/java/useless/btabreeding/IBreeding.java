package useless.btabreeding;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;

public interface IBreeding {
	int btabreeding$getBreedingTimer();
	int btabreeding$getFedTimer();
	int btabreeding$getChildTimer();
	void btabreeding$setBreedingTimer(int value);
	void btabreeding$setFedTimer(int value);
	void btabreeding$setChildTimer(int value);
	boolean btabreeding$isBreedable();
	boolean btabreeding$isFed();
	boolean btabreeding$isFoodItem(ItemStack stack);
	void btabreeding$spawnBaby(IBreeding partner);
	boolean btabreeding$isBaby();
	void btabreeding$setPassiveTarget(Entity entity);
	Entity btabreeding$getPassiveTarget();
}

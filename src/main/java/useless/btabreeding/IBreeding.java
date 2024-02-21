package useless.btabreeding;

import net.minecraft.core.item.ItemStack;

public interface IBreeding {
	int btabreeding$getBreedingTimer();
	int btabreeding$getFedTimer();
	void btabreeding$setBreedingTimer(int value);
	void btabreeding$setFedTimer(int value);
	boolean btabreeding$isBreedable();
	boolean btabreeding$isFed();
	boolean btabreeding$isFoodItem(ItemStack stack);
	void btabreeding$spawnBaby(IBreeding partner);
}

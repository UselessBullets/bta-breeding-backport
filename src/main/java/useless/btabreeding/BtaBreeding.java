package useless.btabreeding;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class BtaBreeding implements ModInitializer {
    public static final String MOD_ID = "btabreeding";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("BTA Sex mod initialized.");
    }

	public static Entity createEntity(Class<? extends Entity> entityClass, World world) {
		try {
			return entityClass.getConstructor(World.class).newInstance(world);
		} catch (Exception e) {
			throw new CommandError("Could not create Entity!");
		}
	}
}

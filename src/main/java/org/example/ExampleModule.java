package org.example;

import com.mojang.brigadier.Command;
import net.minecraft.server.commands.DebugCommand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.events.player.EventPlayerUpdate;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.core.event.subscribe.Subscribe;

public class ExampleModule extends ToggleableModule {

	Player person;

	public ExampleModule() {
		super("DeathEffect", "Shows a lightning bolt on people who are dead", ModuleCategory.CLIENT);
	}

	private long time = -1L;

	public long getMs(long time) {
		return time / 1000000L;
	}

	public boolean passedMs(long ms) {
		return this.getMs(System.nanoTime() - this.time) >= ms;
	}

	public void resetTimer() {
		this.time = System.nanoTime();
	}

	@Subscribe
	private void onUpdate(EventUpdate event){
		for (Player player : mc.level.players()) {
			if (player.getHealth() > 0.0F) continue;
			this.getLogger().info("player dead");
			onDeath(player);
		}
	}

	private void onDeath(Player player) {
		if ((passedMs(2000) || player != person) && player != mc.player) {
			LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, mc.level);
			bolt.setPos(player.position());
			bolt.teleportTo(player.getX(), player.getY(), player.getZ());

			mc.level.addEntity(bolt);

			mc.level.playSound(mc.player, mc.player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS, 1f, 1f);
			mc.level.playSound(mc.player, mc.player.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.BLOCKS, 1f, 1f);

			person = player;
			resetTimer();
		}
	}

}

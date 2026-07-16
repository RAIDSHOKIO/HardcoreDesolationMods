package raidshokio.hardcoredesolation;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;

public class Main implements ModInitializer {
    public static final String MOD_ID = "heavy-gold";

    private final GoldWeightHandler goldHandler = new GoldWeightHandler();

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                goldHandler.handle(player);
            }
        });
    }
}
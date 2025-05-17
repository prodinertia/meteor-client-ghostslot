package meteordevelopment.meteorclient.systems.modules.misc;

import net.minecraft.text.Text;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;

public class GhostSlotDupe extends Module {
    private boolean insideContainer = false;
    private long lastSlotClickTime = 0;
    private boolean disconnectQueued = false;

    public GhostSlotDupe() {
        super(Category.PLAYER, "GhostSlot Dupe", "Auto-disconnects when moving items inside containers to try triggering server desync.");
   
    }

    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof ClickSlotC2SPacket && mc.currentScreen != null && !disconnectQueued) {
            disconnectQueued = true;
            lastSlotClickTime = System.currentTimeMillis();
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (disconnectQueued && System.currentTimeMillis() - lastSlotClickTime > 150) {
            info("GhostSlot triggered — disconnecting...");
            mc.getNetworkHandler().getConnection().disconnect(Text.of("GhostSlot Trigger"));
            mc.stop();
    }
}

}

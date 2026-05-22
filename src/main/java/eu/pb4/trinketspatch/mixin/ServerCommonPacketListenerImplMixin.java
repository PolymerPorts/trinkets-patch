package eu.pb4.trinketspatch.mixin;

import eu.pb4.trinketspatch.impl.TrinketsFlatUI;
import eu.pb4.trinketspatch.impl.TrinketsPolymerPatch;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonPacketListenerImplMixin {
    @Inject(method = "handleCustomClickAction", at = @At("TAIL"))
    private void onCustomClickAction(ServerboundCustomClickActionPacket packet, CallbackInfo ci) {
        if (packet.id().getNamespace().equals("trinkets") && packet.id().getPath().equals("open") && this instanceof ServerPlayerConnection connection) {
            TrinketsFlatUI.open(connection.getPlayer());
        }
    }
}
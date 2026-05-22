package eu.pb4.trinketspatch.mixin;

import eu.pb4.trinkets.impl.data.EntitySlotLoader;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntitySlotLoader.class)
public class EntitySlotLoaderMixin {
    @Redirect(method = {"sync(Lnet/minecraft/server/level/ServerPlayer;)V", "sync(Ljava/util/List;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void dontSend(ServerGamePacketListenerImpl instance, Packet packet) {}
}

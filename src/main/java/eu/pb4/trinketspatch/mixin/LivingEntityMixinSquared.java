package eu.pb4.trinketspatch.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LivingEntity.class, priority = 1500)
public abstract class LivingEntityMixinSquared {
    @TargetHandler(
        mixin = "eu.pb4.trinkets.mixin.LivingEntityMixin",
        name = "handleEquipmentUpdates"
    )
    @Redirect(
        method = "@MixinSquared:Handler", 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/server/level/ServerChunkCache;sendToTrackingPlayers(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/protocol/Packet;)V"
        )
    )
    private void dontSendPackets(ServerChunkCache instance, final Entity entity, final Packet<? super ClientGamePacketListener> packet) {
    }

    @TargetHandler(
            mixin = "eu.pb4.trinkets.mixin.LivingEntityMixin",
            name = "handleEquipmentUpdates"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void dontSend(ServerGamePacketListenerImpl instance, Packet packet) {}
}
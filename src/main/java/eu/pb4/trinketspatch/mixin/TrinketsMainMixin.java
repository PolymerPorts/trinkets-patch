package eu.pb4.trinketspatch.mixin;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import eu.pb4.trinkets.impl.LivingEntityTrinketAttachment;
import eu.pb4.trinkets.impl.TrinketsConfig;
import eu.pb4.trinkets.impl.TrinketsMain;
import eu.pb4.trinkets.impl.payload.SyncConfigPayload;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TrinketsMain.class)
public class TrinketsMainMixin {

    /**
     * @author Patbox
     * @reason Because yes
     */
    @Overwrite
    public static void syncConfigChanges(MinecraftServer server) {
        for (var level : server.getAllLevels()) {
            for (var entity : level.getAllEntities()) {
                if (entity instanceof LivingEntity livingEntity) {
                    LivingEntityTrinketAttachment.get(livingEntity).rebuild();
                }
            }
        }
    }
}

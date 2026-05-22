package eu.pb4.trinketspatch.mixin;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TrinketsApi.class)
public class TrinketsApiMixin {

    /**
     * @author Patbox
     * @reason Because yes
     */
    @Overwrite
    public static void onTrinketBroken(ItemStack stack, TrinketSlotAccess ref, LivingEntity entity) {
        if (entity.level() instanceof ServerLevel world) {
            for (int i = 0; i < 5; ++i) {
                Vec3 vec3d = new Vec3(((double) entity.getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                vec3d = vec3d.xRot(-entity.getXRot() * 0.017453292F);
                vec3d = vec3d.yRot(-entity.getYRot() * 0.017453292F);
                double d = (double) (-entity.getRandom().nextFloat()) * 0.6D - 0.3D;
                Vec3 vec3d2 = new Vec3(((double) entity.getRandom().nextFloat() - 0.5D) * 0.3D, d, 0.6D);
                vec3d2 = vec3d2.xRot(-entity.getXRot() * 0.017453292F);
                vec3d2 = vec3d2.yRot(-entity.getYRot() * 0.017453292F);
                vec3d2 = vec3d2.add(entity.getX(), entity.getEyeY(), entity.getZ());
                world.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(stack)), vec3d2.x, vec3d2.y, vec3d2.z, 0, vec3d.x, vec3d.y + 0.05D, vec3d.z, 1);
            }
            if (!entity.isSilent() && stack.has(DataComponents.BREAK_SOUND)) {
                world.playSeededSound(null, entity, stack.get(DataComponents.BREAK_SOUND), entity.getSoundSource(), 0.8F, 0.8F + world.getRandom().nextFloat() * 0.4F, world.getRandom().nextInt());
            }
        }
    }
}

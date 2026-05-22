package eu.pb4.trinketspatch.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.Model;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TrinketRenderer.class)
public interface TrinketRendererMixin {

    /**
     * @author Patbox
     * @reason no-op
     */
    @Overwrite
    static boolean translateToModelPart(PoseStack poseStack, Model<?> model, String modelPart, Vector3fc offset) {
        return false;
    }

    /**
     * @author Patbox
     * @reason no-op
     */
    @Overwrite
    static boolean translateToModelPartStartingFrom(PoseStack poseStack, Model<?> model, String startingModelPart, String modelPart, Vector3fc offset) {
        return false;
    }
}

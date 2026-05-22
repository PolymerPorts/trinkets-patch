package eu.pb4.trinketspatch.mixin.client;

import dev.yumi.mc.core.api.ModContainer;
import eu.pb4.trinkets.impl.client.TrinketsClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TrinketsClient.class)
public class TrinketsClientMixin {
    /**
     * @author Patbox
     * @reason Client be no more!
     */
    @Overwrite
    public void onInitializeClient(ModContainer modContainer) {}
}

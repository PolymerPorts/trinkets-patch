package eu.pb4.trinketspatch.mixin;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class TrinketsMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        return mixinClassName.startsWith("eu.pb4.trinkets.mixin.client.")
                || mixinClassName.equals("eu.pb4.trinkets.mixin.InventoryMenuMixin")
                || mixinClassName.equals("eu.pb4.trinkets.mixin.PlayerListMixin")
                || mixinClassName.equals("eu.pb4.trinkets.mixin.ServerEntityMixin")
                ;
    }
}
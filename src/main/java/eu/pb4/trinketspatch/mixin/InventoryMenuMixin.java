package eu.pb4.trinketspatch.mixin;

import eu.pb4.trinkets.api.SlotGroup;
import eu.pb4.trinkets.api.SlotType;
import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.impl.LivingEntityTrinketAttachment;
import eu.pb4.trinkets.impl.Point;
import eu.pb4.trinkets.impl.TrinketInventoryMenu;
import eu.pb4.trinkets.impl.slots.MinimalTrinketSlotState;
import eu.pb4.trinkets.impl.slots.TrinketSlotState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(InventoryMenu.class)
public class InventoryMenuMixin implements TrinketInventoryMenu {
    @Shadow
    @Final
    private Player owner;
    @Unique
    private MinimalTrinketSlotState state;

    @Override
    public void trinkets$updateTrinketSlots(boolean reinitializeAttachment) {
        if (reinitializeAttachment) {
            LivingEntityTrinketAttachment.get(this.owner).rebuild();
        }
        this.state = new MinimalTrinketSlotState(owner, (AbstractContainerMenu) (Object) this, LivingEntityTrinketAttachment.get(this.owner), List.of());
    }

    @Override
    public int trinkets$getTrinketSlotStart() {
        return 0;
    }

    @Override
    public int trinkets$getTrinketSlotEnd() {
        return 0;
    }

    @Override
    public TrinketSlotState trinkets$getSlotState() {
        return this.state;
    }

    @Override
    public TrinketAttachment trinkets$attachment() {
        return LivingEntityTrinketAttachment.get(this.owner);
    }

    @Override
    public boolean trinkets$isCosmeticMode() {
        return false;
    }

    @Override
    public void trinkets$setCosmeticMode(boolean b) {

    }

    @Override
    public boolean trinkets$hasSlots() {
        return false;
    }

    @Override
    public boolean trinkets$hasCosmetic() {
        return false;
    }
}

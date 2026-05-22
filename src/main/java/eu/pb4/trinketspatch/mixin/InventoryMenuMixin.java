package eu.pb4.trinketspatch.mixin;

import eu.pb4.trinkets.api.SlotGroup;
import eu.pb4.trinkets.api.SlotType;
import eu.pb4.trinkets.impl.LivingEntityTrinketAttachment;
import eu.pb4.trinkets.impl.Point;
import eu.pb4.trinkets.impl.TrinketInventoryMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(InventoryMenu.class)
public class InventoryMenuMixin implements TrinketInventoryMenu {
    @Shadow
    @Final
    private Player owner;

    @Override
    public void trinkets$updateTrinketSlots(boolean reinitializeAttachment) {
        if (reinitializeAttachment) {
            LivingEntityTrinketAttachment.get(this.owner).rebuild();
        }
    }

    @Override
    public int trinkets$getGroupNum(SlotGroup group) {
        return 0;
    }

    @Override
    public @Nullable Point trinkets$getGroupPos(SlotGroup group) {
        return null;
    }

    @Override
    public @Nullable SlotGroup trinkets$getGroupAtSlot(int slotIndex) {
        return null;
    }

    @Override
    public @NotNull List<Point> trinkets$getSlotHeights(SlotGroup group) {
        return List.of();
    }

    @Override
    public @Nullable Point trinkets$getSlotHeight(SlotGroup group, int i) {
        return null;
    }

    @Override
    public @NotNull List<SlotType> trinkets$getSlotTypes(SlotGroup group) {
        return List.of();
    }

    @Override
    public int trinkets$getSlotWidth(SlotGroup group) {
        return 0;
    }

    @Override
    public int trinkets$getGroupCount() {
        return 0;
    }

    @Override
    public int trinkets$getTrinketSlotStart() {
        return 0;
    }

    @Override
    public int trinkets$getTrinketSlotEnd() {
        return 0;
    }
}

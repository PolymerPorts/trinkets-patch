package eu.pb4.trinketspatch.impl;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import eu.pb4.polymer.core.api.other.PolymerMobEffect;
import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import eu.pb4.polymer.core.api.utils.PolymerSyncedObject;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;

public class ArtifactsTestBridge {
    private static final EntityType<?> ARMOR_STAND = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.withDefaultNamespace("armor_stand"));

    public static void setup() {
        var id = "artifacts";
        PolymerResourcePackUtils.addModAssets(id);

        RegistryEntryAddedCallback.allEntries(BuiltInRegistries.ITEM, item -> {
            if (item.key().identifier().getNamespace().equals(id)) {
                PolymerItem.registerOverlay(item.value(), (_, _) -> Items.TRIAL_KEY);
            }
        });

        RegistryEntryAddedCallback.allEntries(BuiltInRegistries.SOUND_EVENT, item -> {
            if (item.key().identifier().getNamespace().equals(id)) {
                PolymerSoundEvent.registerOverlay(item.value());
            }
        });

        RegistryEntryAddedCallback.allEntries(BuiltInRegistries.DATA_COMPONENT_TYPE, item -> {
            if (item.key().identifier().getNamespace().equals(id)) {
                PolymerComponent.registerDataComponent(item.value());
            }
        });

        RegistryEntryAddedCallback.allEntries(BuiltInRegistries.MOB_EFFECT, item -> {
            if (item.key().identifier().getNamespace().equals(id)) {
                PolymerMobEffect.registerOverlay(item.value());
            }
        });

        RegistryEntryAddedCallback.allEntries(BuiltInRegistries.GAME_EVENT, item -> {
            if (item.key().identifier().getNamespace().equals(id)) {
                PolymerSyncedObject.setSyncedObject(BuiltInRegistries.GAME_EVENT, item.value(), (_, _) -> GameEvent.ENTITY_ACTION.value());
            }
        });

        RegistryEntryAddedCallback.allEntries(BuiltInRegistries.ATTRIBUTE, item -> {
            if (item.key().identifier().getNamespace().equals(id)) {
                PolymerEntityUtils.registerAttribute(item);
            }
        });

        RegistryEntryAddedCallback.allEntries(BuiltInRegistries.ENTITY_TYPE, item -> {
            if (item.key().identifier().getNamespace().equals(id)) {
                PolymerEntityUtils.registerOverlay(item.value(), _ -> _ -> ARMOR_STAND);
            }
        });
    }
}

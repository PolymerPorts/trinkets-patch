package eu.pb4.trinketspatch.impl.res;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;
import eu.pb4.polymer.resourcepack.extras.api.format.atlas.AtlasAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.item.ItemAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.item.model.BasicItemModel;
import eu.pb4.polymer.resourcepack.extras.api.format.model.GuiLight;
import eu.pb4.polymer.resourcepack.extras.api.format.model.ModelAsset;
import eu.pb4.trinketspatch.impl.TrinketsPolymerPatch;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;

public class ResourcePackGenerator {
    public static void setup() {
        UiResourceCreator.setup();
        GuiTextures.register();
        PolymerResourcePackUtils.RESOURCE_PACK_AFTER_INITIAL_CREATION_EVENT.register(ResourcePackGenerator::build);
    }

    private static void build(ResourcePackBuilder builder) {
        builder.addData("assets/trinkets-patch/models/sgui/elements/filler.json", ModelAsset.builder()
                .guiLight(GuiLight.FRONT)
                .texture("texture", TrinketsPolymerPatch.id("sgui/elements/filler"))
                .element(new Vec3(-1,-1,-1), new Vec3(17,17,17), b -> Arrays.stream(Direction.values())
                        .forEach(d -> b.face(d, 0, 0,16, 16,"texture")))
                .build()
        );


        builder.forEachResource((path, _) -> {
            var parts = path.split("/",3);

            if (parts.length < 3 || !parts[0].equals("assets") || !parts[2].startsWith("textures/gui/sprites/container/slots")) return;

            var pathx = parts[2].substring("textures/gui/sprites/container/slots/".length(), parts[2].length() - ".png".length());

            var id = Identifier.fromNamespaceAndPath(parts[1], "__trinkets/slots/" + pathx);

            builder.addData("assets/" + id.getNamespace() + "/models/sgui/" + id.getPath() + ".json", ModelAsset.builder()
                    .parent(Identifier.withDefaultNamespace("item/generated"))
                    .texture("layer0", id)
                    .build()
            );

            builder.addData("assets/" + id.getNamespace() + "/items/-/sgui/" + id.getPath() + ".json",
                    new ItemAsset(new BasicItemModel(id.withPrefix("sgui/")))
            );
        });

        var atlas = AtlasAsset.builder();
        atlas.directory("gui/sprites/container/slots", "__trinkets/slots/");
        builder.addData("assets/minecraft/atlases/items.json", atlas.build());
    }
}
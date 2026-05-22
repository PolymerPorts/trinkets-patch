package eu.pb4.trinketspatch.impl;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.playerdata.api.PlayerDataApi;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import eu.pb4.polymer.resourcepack.extras.api.format.item.ItemAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.item.model.BasicItemModel;
import eu.pb4.trinkets.api.component.TrinketDataComponents;
import eu.pb4.trinkets.impl.TrinketsMain;
import eu.pb4.trinketspatch.impl.res.GuiTextures;
import eu.pb4.trinketspatch.impl.res.ResourcePackGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.ByteTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.commands.Commands.literal;


public class TrinketsPolymerPatch implements ModInitializer {
    public static final String MOD_ID = "trinkets-polymer-patch";
    public static final Logger LOGGER = LoggerFactory.getLogger("trinkets-polymer-patch");

    public static final PolyConfig CONFIG = PolyConfig.loadOrCreateConfig();
    public static final Identifier COMPACT_SETTING = Identifier.fromNamespaceAndPath(TrinketsMain.NAMESPACE, "compact_ui");


    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath("trinkets-patch", path);
    }

    @Override
    public void onInitialize() {
        PolymerResourcePackUtils.addModAssets("trinkets_updated");
        PolymerResourcePackUtils.addModAssets(MOD_ID);
        ResourcePackExtras.forDefault().addBridgedModelsFolder(Identifier.fromNamespaceAndPath("trinkets-patch", "sgui"), (id, b) -> {
            return new ItemAsset(new BasicItemModel(id), new ItemAsset.Properties(true, true));
        });

        GuiTextures.register();
        ResourcePackGenerator.setup();

        PolymerComponent.registerDataComponent(TrinketDataComponents.ATTRIBUTE_MODIFIERS, TrinketDataComponents.EQUIPMENT);

        PolyConfig.loadOrCreateConfig();

        CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) ->
                dispatcher.register(literal("trinkets")
                        .executes(ctx -> TrinketsFlatUI.open(ctx.getSource().getPlayerOrException()))
                        .then(literal("compact").executes(TrinketsPolymerPatch::toggleCompactCommand))
                )
        );

        if (FabricLoader.getInstance().isDevelopmentEnvironment() && FabricLoader.getInstance().isModLoaded("artifacts") && false) {
            ArtifactsTestBridge.setup();
        }
    }

    public static int toggleCompactCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var player = ctx.getSource().getPlayerOrException();

        var isCompact = !getIsCompact(player);

        ctx.getSource().sendSuccess(() -> Component.translatable("trinkets.command.compact." + isCompact), false);

        PlayerDataApi.setGlobalDataFor(player, COMPACT_SETTING, ByteTag.valueOf(isCompact));

        return 0;
    }

    public static boolean getIsCompact(ServerPlayer player) {
        var data = PlayerDataApi.getGlobalDataFor(player, COMPACT_SETTING, ByteTag.TYPE);

        if (data == null) {
            return CONFIG.compactUi;
        } else {
            return data.byteValue() > 0;
        }
    }
}
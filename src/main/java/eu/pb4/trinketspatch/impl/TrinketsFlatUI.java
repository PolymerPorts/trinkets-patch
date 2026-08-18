package eu.pb4.trinketspatch.impl;

import com.google.common.collect.ImmutableMap;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.trinkets.api.DefaultTrinketSlots;
import eu.pb4.trinkets.impl.LivingEntityTrinketAttachment;
import eu.pb4.trinkets.impl.slots.SurvivalTrinketSlot;
import eu.pb4.trinkets.impl.TrinketInventoryImpl;
import eu.pb4.trinketspatch.impl.res.GuiTextures;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.function.Predicates;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrinketsFlatUI extends SimpleGui {
    private static final Map<String, Item> MODEL_MAP = ImmutableMap.<String, Item>builder()
            .put(DefaultTrinketSlots.HEAD_FACE, Items.PLAYER_HEAD)
            .put(DefaultTrinketSlots.HEAD_HAT, Items.CHAINMAIL_HELMET)
            .put(DefaultTrinketSlots.CHEST_BACK, Items.CHAINMAIL_CHESTPLATE)
            .put(DefaultTrinketSlots.CHEST_CAPE, Items.BANNER.white())
            .put(DefaultTrinketSlots.CHEST_NECKLACE, Items.IRON_CHAIN)
            .put(DefaultTrinketSlots.HAND_GLOVE, Items.WOOL.white())
            .put(DefaultTrinketSlots.HAND_RING, Items.GOLD_NUGGET)
            .put(DefaultTrinketSlots.OFFHAND_GLOVE, Items.WOOL.lightGray())
            .put(DefaultTrinketSlots.OFFHAND_RING, Items.COPPER_NUGGET)
            .put(DefaultTrinketSlots.LEGS_BELT, Items.CHAINMAIL_LEGGINGS)
            .put(DefaultTrinketSlots.FEET_SHOES, Items.CHAINMAIL_BOOTS)
            .put(DefaultTrinketSlots.FEET_AGLET, Items.IRON_INGOT)
            .build();

    private final List<TrinketInventoryImpl> inventories;
    private final LivingEntityTrinketAttachment component;
    private final int displayPerPage;
    private final int[] subPage;
    private final int[] cachedSize;
    private final TrinketInventoryImpl[] currentlyDisplayed;
    private final boolean compact;
    private int page = 0;


    public TrinketsFlatUI(ServerPlayer player, boolean compact) {
        super(MenuType.GENERIC_9x6, player, false);
        this.compact = compact;
        this.component = LivingEntityTrinketAttachment.get(player);

        this.displayPerPage = compact ? 10 : 5;

        this.subPage = new int[this.displayPerPage];
        this.cachedSize = new int[this.displayPerPage];
        this.currentlyDisplayed = new TrinketInventoryImpl[this.displayPerPage];

        this.inventories = this.component.inventory.values().stream()
                .sorted(Comparator.<TrinketInventoryImpl>comparingInt(
                                inv -> this.component.getGroups().get(inv.slotType().group()).order())
                        .thenComparingInt(a -> a.slotType().order())

                )
                .collect(Collectors.toList());

        this.setTitle(PolymerResourcePackUtils.hasMainPack(player)
                ? (compact ? GuiTextures.FLAT_GUI_COMPACT : GuiTextures.FLAT_GUI).apply(Component.translatable("trinkets.name"))
                : Component.translatable("trinkets.name")
        );
        this.drawLines();
        this.drawNavbar();

        this.open();
    }

    public static int open(ServerPlayer player) {
        playClickSound(player);
        new TrinketsFlatUI(player, TrinketsPolymerPatch.getIsCompact(player));
        return 1;
    }

    public static void playClickSound(ServerPlayer player) {
        player.connection.send(new ClientboundSoundEntityPacket(
                SoundEvents.UI_BUTTON_CLICK, SoundSource.UI, player, 0.7f, 1, player.getRandom().nextLong()
        ));
    }

    public void drawLines() {
        if (this.compact && !PolymerResourcePackUtils.hasMainPack(this.player)) {
            for (int x = 0; x < 5; x++) {
                this.setSlot(9 * x + 4, GuiTextures.FILLER.get(false).hideTooltip());
            }
        }

        var hasPack = PolymerResourcePackUtils.hasMainPack(this.player);

        for (int i = 0; i < this.displayPerPage; i++) {
            var y = page * this.displayPerPage + i;
            if (y < this.inventories.size()) {
                drawLine(i, this.inventories.get(y), subPage[i]);
            } else {
                if (this.compact) {
                    int base = i / 2 * 9 + ((i % 2) * 5);
                    for (int x = 0; x < 4; x++) {
                        this.setSlot(base + x, GuiTextures.FILLER.get(hasPack).hideTooltip());
                    }
                } else {
                    for (int x = 0; x < 9; x++) {
                        this.setSlot(i * 9 + x, GuiTextures.FILLER.get(hasPack).hideTooltip());
                    }
                }
                this.cachedSize[i] = 0;
                this.currentlyDisplayed[i] = null;
            }
        }
    }

    @Override
    public void onTick() {
        for (int i = 0; i < this.displayPerPage; i++) {
            if (this.currentlyDisplayed[i] != null && this.currentlyDisplayed[i].getContainerSize() != this.cachedSize[i]) {
                this.drawLine(i, this.currentlyDisplayed[i], this.subPage[i]);
            }
        }

        super.onTick();
    }

    private void drawLine(int index, TrinketInventoryImpl trinketInventory, int subPage) {
        var type = trinketInventory.slotType();
        this.cachedSize[index] = trinketInventory.getContainerSize();
        this.currentlyDisplayed[index] = trinketInventory;
        boolean hasPack = PolymerResourcePackUtils.hasMainPack(player);

        var base = this.compact ? index / 2 * 9 + ((index % 2) * 5) : index * 9;
        var invSize = this.compact ? 2 : 7;

        int slot = 0;

        var icon = new GuiElementBuilder(MODEL_MAP.getOrDefault(type.getId(), Items.OAK_SIGN))
                .setName(type.getTranslation().withStyle(ChatFormatting.WHITE)).hideDefaultTooltip();

        if (hasPack) {
            icon.model(ResourcePackExtras.bridgeModel(Identifier.fromNamespaceAndPath(
                    trinketInventory.slotType().icon().getNamespace(),
                    trinketInventory.slotType().icon().getPath().replace("container/slots/", "sgui/__trinkets/slots/")
            )));
        }
        this.setSlot(base + slot++, icon);

        //if (!this.compact) {
        //    this.setSlot(base + slot++, GuiTextures.FILLER.get(hasPack).hideTooltip());
        //}

        if (trinketInventory.getContainerSize() <= invSize) {
            for (int i = 0; i < invSize; i++) {
                if (i < trinketInventory.getContainerSize()) {
                    this.setSlot(base + slot++, new SurvivalTrinketSlot(trinketInventory, i, 0, 0, Predicates.truePredicate(), false, this.player));
                } else {
                    this.setSlot(base + slot++, GuiTextures.FILLER.get(hasPack).hideTooltip());
                }
            }

            if (hasPack) {
                this.setSlot(base + slot++, ItemStack.EMPTY);
            } else {
                this.setSlot(base + slot++, GuiTextures.FILLER.get(hasPack).hideTooltip());
            }
        } else {
            for (int i = 0; i < invSize; i++) {
                if (subPage * invSize + i < trinketInventory.getContainerSize()) {
                    this.setSlot(base + slot++, new SurvivalTrinketSlot(trinketInventory, subPage * invSize + i, 0, 0, Predicates.truePredicate(), false, this.player));
                } else {
                    this.setSlot(base + slot++, GuiTextures.FILLER.get(hasPack).hideTooltip());
                }
            }

            this.setSlot(base + slot++,
                    GuiTextures.SUBPAGE.get(hasPack)
                            .setName(Component.empty()
                                    .append(Component.literal("« ").withStyle(ChatFormatting.GRAY))
                                    .append((this.subPage[index] + 1) + "/" + ((trinketInventory.getContainerSize() - 1) / invSize + 1))
                                    .append(Component.literal(" »").withStyle(ChatFormatting.GRAY))
                            )
                            .setCallback((y) -> {
                                if (y.isLeft) {
                                    this.subPage[index] = this.subPage[index] - 1;

                                    if (this.subPage[index] < 0) {
                                        this.subPage[index] = (trinketInventory.getContainerSize() - 1) / invSize;
                                    }
                                    this.drawLine(index, trinketInventory, this.subPage[index]);

                                    playClickSound(this.player);
                                } else if (y.isRight) {
                                    this.subPage[index] = this.subPage[index] + 1;

                                    if (this.subPage[index] > (trinketInventory.getContainerSize() - 1) / invSize) {
                                        this.subPage[index] = 0;
                                    }
                                    this.drawLine(index, trinketInventory, this.subPage[index]);

                                    playClickSound(this.player);
                                }
                            })
            );
        }
    }

    private void drawNavbar() {
        boolean hasPack = PolymerResourcePackUtils.hasMainPack(this.player);
        var navabarFiller = GuiTextures.FILLER_NAVBAR.get(hasPack).hideTooltip();

        if (this.inventories.size() > this.displayPerPage) {

            this.setSlot(5 * 9, navabarFiller);
            this.setSlot(5 * 9 + 1, navabarFiller);

            this.setSlot(5 * 9 + 2, GuiTextures.PREVIOUS.get(hasPack)
                    .setName(Component.translatable("spectatorMenu.previous_page"))
                    .hideDefaultTooltip()
                    .setCallback(() -> {
                        this.page -= 1;

                        if (this.page < 0) {
                            this.page = (this.inventories.size() - 1) / this.displayPerPage;
                        }

                        Arrays.fill(this.subPage, 0);
                        this.drawLines();
                        playClickSound(this.player);
                    })
            );

            this.setSlot(5 * 9 + 3, navabarFiller);
            this.setSlot(5 * 9 + 4, navabarFiller);
            this.setSlot(5 * 9 + 5, navabarFiller);


            this.setSlot(5 * 9 + 6, GuiTextures.NEXT.get(hasPack)
                    .setName(Component.translatable("spectatorMenu.next_page"))
                    .hideDefaultTooltip()
                    .setCallback(() -> {
                        this.page += 1;

                        if (this.page > (this.inventories.size() - 1) / this.displayPerPage) {
                            this.page = 0;
                        }

                        Arrays.fill(this.subPage, 0);
                        this.drawLines();
                        playClickSound(this.player);
                    })
            );


            this.setSlot(5 * 9 + 7, navabarFiller);
            this.setSlot(5 * 9 + 8, navabarFiller);
        } else {
            for (int i = 0; i < 9; i++) {
                this.setSlot(5 * 9 + i, navabarFiller);
            }
        }
    }
}

package eu.pb4.trinketspatch.impl.res;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;
import java.util.function.Supplier;

import static eu.pb4.trinketspatch.impl.res.UiResourceCreator.background;
import static eu.pb4.trinketspatch.impl.res.UiResourceCreator.icon16;

public class GuiTextures {
    public static final Function<Component, Component> FLAT_GUI = background("flat_gui");
    public static final Function<Component, Component> FLAT_GUI_COMPACT = background("flat_gui_compact");

    public static final Element FILLER = Element.of(Items.STAINED_GLASS_PANE.white(), "filler");
    public static final Element FILLER_NAVBAR = new Element(Items.STAINED_GLASS_PANE.black(), () -> new GuiElementBuilder(Items.AIR));


    public static final Element PREVIOUS = Element.of(Items.STAINED_GLASS_PANE.green(), "previous");
    public static final Element NEXT = Element.of(Items.STAINED_GLASS_PANE.green(), "next");
    public static final Element SUBPAGE = Element.of(Items.STAINED_GLASS_PANE.lightBlue(), "subpage");


    public static void register() {

    }


    public record Element(Item item, Supplier<GuiElementBuilder> modeled) {

        public static Element of(Item fallback, String texture) {
            return new Element(fallback, icon16(texture));
        }

        public GuiElementBuilder get(boolean hasPack) {
            return hasPack ? modeled.get() : new GuiElementBuilder(item);
        }
    }
}

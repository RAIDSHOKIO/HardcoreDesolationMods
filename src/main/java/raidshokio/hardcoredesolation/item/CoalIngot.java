package raidshokio.hardcoredesolation;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CoalIngot implements ModInitializer {

    public static final Item COAL_INGOT = new Item(new Item.Properties()) {
        @Override
        public Component getName(ItemStack stack) { return Component.translatable("item.hardcoredesolation.coal_ingot"); }
    };

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath("hardcoredesolation", "coal_ingot"),  // namespace можно оставить свой, например heavy-gold
                COAL_INGOT);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
                .register(entries -> entries.accept(COAL_INGOT));
    }
}
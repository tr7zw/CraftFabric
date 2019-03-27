package io.github.tr7zw.fabricbukkit;

import org.bukkit.Server;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sortme.JsonLikeTagParser;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FabricBukkit implements ModInitializer {

    public static final Item FABRIC_ITEM = new Item(new Item.Settings().itemGroup(ItemGroup.MISC));

    @Override
    public void onInitialize() {
        System.out.println("Hello Fabric world!");
        Registry.register(Registry.ITEM, new Identifier("my-mod", "fabric_item"), FABRIC_ITEM);

        Server server = new ServerImpl();
        System.out.println(server.getName());

        System.out.println(Registry.BLOCK.get(1).getTranslationKey());

        System.out.println(org.bukkit.Material
            .getMaterial(Registry.BLOCK.get(1).getTranslationKey().replace("block.minecraft.", "minecraft:")));

        try { // Found the nbt parser
            CompoundTag tag = (CompoundTag) (new JsonLikeTagParser(new StringReader("{" + "    \"additionalData\": {"
                + "        \"Invisible\": 0,"
                + "        \"Pose\": \"{Body:[41f,0f,0f],Head:[0f,52f,52f],LeftLeg:[105f,64f,29f],RightLeg:[37f,0f,0f]}\""
                + "    }" + "}"))).parseTag();
            System.out.println(tag.getCompound("additionalData").getTag("Pose").getClass().getSimpleName());
        } catch (CommandSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

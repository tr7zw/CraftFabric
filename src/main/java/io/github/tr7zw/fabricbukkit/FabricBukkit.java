package io.github.tr7zw.fabricbukkit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;

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
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("FabricBukkit is starting...");
        Registry.register(Registry.ITEM, new Identifier("my-mod", "fabric_item"), FABRIC_ITEM);

        LOG.info(Registry.BLOCK.get(1).getTranslationKey());

        Bukkit.getServer();

        LOG.info(org.bukkit.Material
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

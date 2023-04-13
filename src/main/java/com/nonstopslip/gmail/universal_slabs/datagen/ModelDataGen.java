package com.nonstopslip.gmail.universal_slabs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ModelDataGen extends FabricModelProvider {

    public ModelDataGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        Block[] slabBlocks = Registries.BLOCK.stream().filter(block -> block instanceof SlabBlock).toList().toArray(new Block[0]);
        for (Block slab : slabBlocks) {
            Identifier slabId = Registries.BLOCK.getId(slab);

            BlockStateVariant bottomVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, slabId.withPath("block/" + slabId.getPath().replace("waxed_", "")));
            BlockStateVariant topVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, slabId.withPath("block/" + slabId.getPath().replace("waxed_", "")+ "_top"));

            BlockStateVariant northVariant = BlockStateVariant.union(bottomVariant, BlockStateVariant.create()
                    .put(VariantSettings.MODEL, slabId.withPath("block/" + slabId.getPath().replace("waxed_", "")))
                    .put(VariantSettings.UVLOCK, true)
                    .put(VariantSettings.X, VariantSettings.Rotation.R270));
            BlockStateVariant southVariant = BlockStateVariant.union(northVariant, BlockStateVariant.create()
                    .put(VariantSettings.MODEL, slabId.withPath("block/" + slabId.getPath().replace("waxed_", "")+ "_top")));

            BlockStateVariant eastVariant = BlockStateVariant.union(bottomVariant, BlockStateVariant.create()
                    .put(VariantSettings.X, VariantSettings.Rotation.R270)
                    .put(VariantSettings.Y, VariantSettings.Rotation.R90));
            BlockStateVariant westVariant = BlockStateVariant.union(eastVariant, BlockStateVariant.create()
                    .put(VariantSettings.MODEL, slabId.withPath("block/" + slabId.getPath().replace("waxed_", "")+ "_top")));

            BlockStateVariant doubleVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, slabId.withPath("block/" + slabId.getPath()
                            .replace("_slab", "")
                            .replace("petrified", "")
                            .replace("waxed_", "")
                            .replace("brick", "bricks")
                            .replace("tile", "tiles")
                            .replace("purpur", "purpur_block")
                            .replace("quartz", "quartz_block")
                            .replace("smooth_quartz_block", "smooth_quartz")
                            .replace("acacia", "acacia_planks")
                            .replace("bamboo", "bamboo_planks")
                            .replace("birch", "birch_planks")
                            .replace("crimson", "crimson_planks")
                            .replace("jungle", "jungle_planks")
                            .replace("mangrove", "mangrove_planks")
                            .replace("oak", "oak_planks")
                            .replace("spruce", "spruce_planks")
                            .replace("warped", "warped_planks")
                    ));

            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(slab)
                    .coordinate(BlockStateVariantMap.create(Properties.AXIS, Properties.SLAB_TYPE)
                            .register(Direction.Axis.X, SlabType.BOTTOM, westVariant)
                            .register(Direction.Axis.X, SlabType.TOP, eastVariant)

                            .register(Direction.Axis.Y, SlabType.BOTTOM, bottomVariant)
                            .register(Direction.Axis.Y, SlabType.TOP, topVariant)

                            .register(Direction.Axis.Z, SlabType.BOTTOM, northVariant)
                            .register(Direction.Axis.Z, SlabType.TOP, southVariant)

                            .register(Direction.Axis.X, SlabType.DOUBLE, doubleVariant)
                            .register(Direction.Axis.Y, SlabType.DOUBLE, doubleVariant)
                            .register(Direction.Axis.Z, SlabType.DOUBLE, doubleVariant)
                    )
            );
        }

    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {}

}

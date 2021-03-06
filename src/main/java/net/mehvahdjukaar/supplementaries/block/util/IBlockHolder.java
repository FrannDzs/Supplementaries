package net.mehvahdjukaar.supplementaries.block.util;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;

public interface IBlockHolder {
    BlockState getHeldBlock();
    boolean setHeldBlock(BlockState state);

    default boolean onPlacement(BlockState targetState, BlockItem handStack){
        return this.setHeldBlock(targetState);
    }
}

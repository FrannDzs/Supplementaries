package net.mehvahdjukaar.supplementaries.blocks;

import net.mehvahdjukaar.supplementaries.common.CommonUtil;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Random;

public class FaucetBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty HAS_WATER = CommonUtil.HAS_WATER;
    public static final BooleanProperty HAS_JAR = CommonUtil.HAS_JAR;
    public FaucetBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(HAS_JAR, false).with(FACING, Direction.NORTH)
                .with(ENABLED, false).with(POWERED, false).with(HAS_WATER, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (state.get(HAS_JAR)) {
            switch (state.get(FACING)) {
                case UP :
                case DOWN :
                case NORTH :
                default :
                    return VoxelShapes.create(0.6875D, 0, 1D, 0.3125D, 0.625D, 0.3125D);
                case SOUTH :
                    return VoxelShapes.create(0.3125D, 0, 0D, 0.6875D, 0.625D, 0.6875D);
                case EAST :
                    return VoxelShapes.create(0D, 0, 0.6875D, 0.6875D, 0.625D, 0.3125D);
                case WEST :
                    return VoxelShapes.create(1D, 0, 0.3125D, 0.3125D, 0.625D, 0.6875D);
            }
        } else {
            switch (state.get(FACING)) {
                case UP :
                case DOWN :
                case NORTH :
                default :
                    return VoxelShapes.create(0.6875D, 0.3125D, 1D, 0.3125D, 0.9375D, 0.3125D);
                case SOUTH :
                    return VoxelShapes.create(0.3125D, 0.3125D, 0D, 0.6875D, 0.9375D, 0.6875D);
                case EAST :
                    return VoxelShapes.create(0D, 0.3125D, 0.6875D, 0.6875D, 0.9375D, 0.3125D);
                case WEST :
                    return VoxelShapes.create(1D, 0.3125D, 0.3125D, 0.3125D, 0.9375D, 0.6875D);
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
                                             BlockRayTraceResult hit) {
        float f = state.get(ENABLED) ? 0.6F : 0.5F;
        worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
        this.updateBlock(state, worldIn, pos, true);
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        this.updateBlock(state, worldIn, pos, false);
    }

    //TODO: replace this with updatePostPlacement
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.neighborChanged(state, world, pos, neighborBlock, fromPos, moving);
        this.updateBlock(state, world, pos, false);
    }

    public void updateBlock(BlockState state, World world, BlockPos pos, boolean toggle) {
        BlockPos backpos = pos.offset(state.get(FACING), -1);
        BlockState backblock = world.getBlockState(backpos);
        // checks backblock
        boolean ispowered = world.getRedstonePowerFromNeighbors(pos) > 0;
        boolean ishoney = backblock.getBlock() instanceof BeehiveBlock && backblock.get(BlockStateProperties.HONEY_LEVEL) > 0;
        boolean isjarliquid = (backblock.getBlock() instanceof JarBlock
                && backblock.getBlock().getBeaconColorMultiplier(backblock, world, backpos, backpos) != null);
        boolean iswater = (world.getFluidState(backpos).isTagged(FluidTags.WATER)
                || ((backblock.getBlock() instanceof CauldronBlock) && backblock.getComparatorInputOverride(world, backpos) > 0));
        BlockState downstate = world.getBlockState(pos.down());
        boolean hasjar = downstate.getBlock() instanceof JarBlock;
        boolean haswater = ishoney || iswater || isjarliquid;
        if (ispowered != state.get(POWERED) || haswater != state.get(HAS_WATER) || hasjar != state.get(HAS_JAR) || toggle) {
            world.setBlockState(pos,
                    state.with(POWERED, ispowered).with(HAS_WATER, haswater).with(HAS_JAR, hasjar).with(ENABLED, toggle ^ state.get(ENABLED)), 2);
        }
        int newcolor = -2;
        //TODO:rewrite this
        if (ishoney)
            newcolor = CommonUtil.JarContentType.HONEY.color;
        else if (isjarliquid) {
            TileEntity tileentity = world.getTileEntity(backpos);
            if (tileentity instanceof JarBlockTile) {
                newcolor = ((JarBlockTile) tileentity).color;
            }
        } else if (iswater)
            newcolor = -1;
        if (newcolor != -2) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof FaucetBlockTile) {
                ((FaucetBlockTile) tileentity).watercolor = newcolor;
            }
        }

        //handles concrete
        System.out.println("a");
        if(!hasjar && haswater && (state.get(ENABLED)^toggle^ispowered) && downstate.getBlock() instanceof  ConcretePowderBlock){
            try {
                //field_200294_a ->solidifiedState
                Field f = ObfuscationReflectionHelper.findField(ConcretePowderBlock.class,"field_200294_a");
                f.setAccessible(true);
                world.setBlockState(pos.down(), (BlockState) f.get(downstate.getBlock()), 2|16);
            } catch (Exception e) {}

        }


    }

    public boolean isOpen(BlockState state) {
        return (state.get(BlockStateProperties.POWERED) ^ state.get(BlockStateProperties.ENABLED));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED, POWERED, HAS_WATER, HAS_JAR);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getFace() == Direction.UP || context.getFace() == Direction.DOWN)
            return this.getDefaultState().with(FACING, Direction.NORTH);
        return this.getDefaultState().with(FACING, context.getFace());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        super.animateTick(state, world, pos, random);
        if (this.isOpen(state) && state.get(HAS_WATER) && !state.get(HAS_JAR)) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            for (int l = 0; l < 4; ++l) {
                double d0 = (x + 0.375 + 0.25 * random.nextFloat());
                double d1 = (y + 0.25 + 0 * random.nextFloat());
                // 0.3125
                double d2 = (z + 0.375 + 0.25 * random.nextFloat());
                world.addParticle(ParticleTypes.FALLING_WATER, d0, d1, d2, 0, 0, 0);
                world.addParticle(ParticleTypes.DRIPPING_WATER, x + 0.5, y + 0.25, z + 0.5, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FaucetBlockTile();
    }

    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int eventID, int eventParam) {
        super.eventReceived(state, world, pos, eventID, eventParam);
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }
}

package com.nonstopslip.gmail.universal_slabs.mixin;

import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlabBlock.class)
public abstract class UniversalSlabMixin extends Block implements Waterloggable {


	@Shadow @Final
	public static EnumProperty<SlabType> TYPE;
	@Shadow @Final
	public static BooleanProperty WATERLOGGED;
	private static final EnumProperty<Direction.Axis> AXIS;

	@Shadow @Final
	protected static VoxelShape BOTTOM_SHAPE;
	@Shadow @Final
	protected static VoxelShape TOP_SHAPE;
	private static final VoxelShape NORTH_SHAPE;
	private static final VoxelShape EAST_SHAPE;
	private static final VoxelShape SOUTH_SHAPE;
	private static final VoxelShape WEST_SHAPE;



	UniversalSlabMixin(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y));
	}


	@SuppressWarnings("deprecation")
	@Shadow
	public abstract boolean hasSidedTransparency(BlockState state);


	@Inject(method = "appendProperties", at = @At("RETURN"))
	public void onAppendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
		builder.add(AXIS);
	}


	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		SlabType slabType = state.get(TYPE);
		Direction.Axis axis = state.get(AXIS);
		return switch (slabType) {
			case BOTTOM -> switch (axis) {
				case X -> WEST_SHAPE;
				case Y -> BOTTOM_SHAPE;
				case Z -> NORTH_SHAPE;
			};
			case TOP -> switch (axis) {
				case X -> EAST_SHAPE;
				case Y -> TOP_SHAPE;
				case Z -> SOUTH_SHAPE;
			};
			case DOUBLE -> VoxelShapes.fullCube();
		};
	}


	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		BlockState blockState = ctx.getWorld().getBlockState(blockPos);
		Direction hitSide = ctx.getSide();
		Direction.Axis axis;
		SlabType type;
		if (blockState.isOf(this)) {
			return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
		} else {
			FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
			if (ctx.getPlayer() != null && ctx.getPlayer().isSneaking()) {
				axis = hitSide.getAxis();
				type = hitSide.getDirection() == Direction.AxisDirection.POSITIVE ? SlabType.BOTTOM : SlabType.TOP;
			} else {
				axis = hitSide.getAxis().isHorizontal() ? Direction.Axis.Y
						: ctx.getHorizontalPlayerFacing().getAxis();
				Vec3d hitPos = ctx.getHitPos();
				boolean hitOffsetLessThanPointFive = switch (axis) {
					case X -> (hitPos.x - (double) blockPos.getX() < 0.5);
					case Y -> (hitPos.y - (double) blockPos.getY() < 0.5);
					case Z -> (hitPos.z - (double) blockPos.getZ() < 0.5);
				};
				type = hitOffsetLessThanPointFive ? SlabType.BOTTOM : SlabType.TOP;
			}
			return this.getDefaultState()
					.with(AXIS, axis)
					.with(TYPE, type)
					.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
		}
	}


	@SuppressWarnings("deprecation")
	@Override
	public  boolean canReplace(BlockState state, ItemPlacementContext ctx) {
		SlabType slabType = state.get(TYPE);
		if (slabType != SlabType.DOUBLE && ctx.getStack().isOf(this.asItem())) {
			if (ctx.canReplaceExisting()) {
				Direction.AxisDirection slabAxisDir = slabType == SlabType.BOTTOM ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE;
				Direction slabDir = Direction.from(state.get(AXIS), slabAxisDir);
				Vec3d hitPos = ctx.getHitPos();
				BlockPos blockPos = ctx.getBlockPos();
				boolean hitOffsetGreaterThanPointFive = switch (state.get(AXIS)) {
					case X -> (hitPos.x - (double) blockPos.getX() > 0.5);
					case Y -> (hitPos.y - (double) blockPos.getY() > 0.5);
					case Z -> (hitPos.z - (double) blockPos.getZ() > 0.5);
				};
				if (slabType == SlabType.BOTTOM) {
					return ctx.getSide() == slabDir.getOpposite() ||
						hitOffsetGreaterThanPointFive && ctx.getSide().getAxis() != state.get(AXIS);
				} else {
					return ctx.getSide() == slabDir.getOpposite() ||
							!hitOffsetGreaterThanPointFive && ctx.getSide().getAxis() != state.get(AXIS);
				}
			} else {
				return true;
			}
		}
		return false;
	}


	static {
		AXIS = Properties.AXIS;

		NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
		EAST_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
		SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
		WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
	}
}

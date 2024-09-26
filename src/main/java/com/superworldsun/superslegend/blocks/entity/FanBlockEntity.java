package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.registries.BlockEntityInit;
import com.superworldsun.superslegend.registries.BlockInit;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class FanBlockEntity extends BlockEntity {
    private static final double AIRFLOW_STRENGTH = 0.3D;
    private static final int AIRFLOW_MAX_DISTANCE = 16;
    public float bladesRotation;

    public FanBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.FAN.get(), pos, state);
    }
    public FanBlockEntity(BlockEntityType<?> pType, BlockPos pos, BlockState state) {
        super(pType, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FanBlockEntity blockEntity) {
        if (blockEntity.isPowered()) {
            blockEntity.getPushedEntities().stream().filter(blockEntity::canPush).forEach(blockEntity::pushEntity);
        }
    }

    private void pushEntity(Entity entity) {
        entity.move(MoverType.PISTON, getAirflowPushVector());
    }

    private boolean canPush(Entity entity) {
        return !(entity instanceof LivingEntity) || !((LivingEntity) entity).getItemBySlot(EquipmentSlot.FEET).is(ItemInit.IRON_BOOTS.get());
    }

    private List<Entity> getPushedEntities() {
        return level.getEntities(null, getAirflowAreaOfEffect());
    }

    private AABB getAirflowAreaOfEffect() {
        int airflowDistance = getAirflowDistance();
        AABB airflowArea = new AABB(worldPosition);
        Vec3 airflowExpansionVector = Vec3.atLowerCornerOf(getAirflowDirection().getNormal()).scale(airflowDistance);
        return airflowArea.expandTowards(airflowExpansionVector);
    }

    private int getAirflowDistance() {
        int airflowDistance = 0;
        for (int i = 1; i < AIRFLOW_MAX_DISTANCE; i++) {
            boolean isAirflowBlocked = isAirflowBlockedAt(worldPosition.relative(getFanDirection(), i));
            if (isAirflowBlocked) {
                return airflowDistance;
            }
            airflowDistance++;
        }
        return airflowDistance;
    }

    private boolean isAirflowBlockedAt(BlockPos blockPos) {
        VoxelShape blockShape = level.getBlockState(blockPos).getCollisionShape(level, blockPos);
        return !level.isEmptyBlock(blockPos) && blockShape != Shapes.empty() && blockShape.max(Direction.Axis.Y) >= 0.75D && blockShape.min(Direction.Axis.Y) <= 0.75D;
    }

    public Direction getFanDirection() {
        return getBlockState().getValue(DirectionalBlock.FACING);
    }

    private Vec3 getAirflowPushVector() {
        return Vec3.atLowerCornerOf(getAirflowDirection().getNormal()).scale(AIRFLOW_STRENGTH);
    }

    private Direction getAirflowDirection() {
        return getFanDirection();
    }

    public boolean isPowered() {
        return true;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        bladesRotation = tag.getFloat("BladesRotation");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("BladesRotation", bladesRotation);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            load(tag);
        }
    }

    public static BlockEntityType<FanBlockEntity> createType() {
        return BlockEntityType.Builder.of(FanBlockEntity::new, BlockInit.FAN.get()).build(null);
    }
}
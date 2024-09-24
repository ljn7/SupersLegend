package com.superworldsun.superslegend.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockShapeHelper {
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        if (from == to) {
            return shape;
        }
        VoxelShape[] buffer = new VoxelShape[] { shape, Shapes.empty() };
        if (from.getAxis() != Direction.Axis.Y && to.getAxis() != Direction.Axis.Y) {
            int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
            for (int i = 0; i < times; i++) {
                buffer[0].forAllBoxes(
                        (minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }
        } else {
            int times = from.getAxis() == to.getAxis() ? 2 : to == Direction.UP ? 3 : 1;
            for (int i = 0; i < times; i++) {
                buffer[0].forAllBoxes(
                        (minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.box(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY)));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }
        }
        return buffer[0];
    }
}
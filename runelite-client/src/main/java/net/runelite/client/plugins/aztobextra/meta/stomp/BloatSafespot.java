//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta.stomp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.runelite.client.plugins.aztobextra.meta.stomp.def.BloatPath;
import net.runelite.client.plugins.aztobextra.meta.stomp.def.BloatRotation;
import net.runelite.client.plugins.aztobextra.meta.stomp.def.DistanceInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

public final class BloatSafespot {
    public static final SSLine[][][] grid = new SSLine[][][]{{{new SSLine(new Coordinates(29, 39), new Coordinates(29, 34), -1, 1), new SSLine(new Coordinates(29, 29), new Coordinates(29, 24), -1, -1)}, {new SSLine(new Coordinates(34, 39), new Coordinates(34, 34), 1, 1), new SSLine(new Coordinates(34, 29), new Coordinates(34, 24), 1, -1)}}, {{new SSLine(new Coordinates(24, 34), new Coordinates(29, 34), -1, 1), new SSLine(new Coordinates(34, 34), new Coordinates(39, 34), 1, 1)}, {new SSLine(new Coordinates(28, 29), new Coordinates(23, 29), 1, -1), new SSLine(new Coordinates(34, 29), new Coordinates(39, 29), 1, -1)}}};
    public final BloatPath bloatPath;
    public final BloatRotation clockRotation;
    public final DistanceInfo distanceInfo;

    public BloatSafespot(Pair<BloatPath, BloatRotation> pair, Supplier<Integer> distance) {
        this.bloatPath = (BloatPath)pair.getLeft();
        this.clockRotation = (BloatRotation)pair.getRight();
        this.distanceInfo = new DistanceInfo((Integer)distance.get());
    }

    public List<SSLine> getSafespotLines() {
        return this.distanceInfo.isCorner() ? this.getCornerSafespots() : this.getSideSafespots();
    }

    public List<SSLine> getCornerSafespots() {
        if (this.bloatPath == BloatPath.UNKNOWN) {
            return Collections.emptyList();
        } else {
            Pair<SSLine[], int[]> lop = (Pair)this.bloatPath.getCornerSafespots(grid).get(this.distanceInfo.getCornerIndex(this.clockRotation.isClockwise()));
            SSLine[] safespotLines = (SSLine[])lop.getLeft();
            if (!this.distanceInfo.shouldModifyCorner(this.bloatPath)) {
                return Arrays.asList(safespotLines);
            } else {
                byte bit = (byte)(this.bloatPath != BloatPath.N_PATH && this.bloatPath != BloatPath.S_PATH ? 0 : 1);
                boolean isCol = bit == 0;
                int[] offsets = (int[])lop.getRight();
                return Arrays.asList(safespotLines[bit].offset((c) -> {
                    return c.dx(isCol ? offsets[0] : offsets[2]).dy(isCol ? offsets[1] : offsets[3]);
                }), safespotLines[(3 + bit) % 2]);
            }
        }
    }

    public List<SSLine> getSideSafespots() {
        if (this.bloatPath == BloatPath.UNKNOWN) {
            return Collections.emptyList();
        } else {
            SSLine[] safespotLines = this.bloatPath.getSideSafespotLines(grid);
            if (!this.clockRotation.isClockwise()) {
                ArrayUtils.reverse(safespotLines);
            }

            List<Integer> offsets = this.bloatPath.getSideOffsets(this.distanceInfo.isSideMin());
            return Arrays.asList(safespotLines[0].offset((c) -> {
                return c.dx(this.bloatPath.shouldOffsetX() ? (Integer)offsets.get(0) : 0).dy(this.bloatPath.shouldOffsetY() ? (Integer)offsets.get(0) : 0);
            }), safespotLines[1].offset((c) -> {
                return c.dx(this.bloatPath.shouldOffsetX() ? (Integer)offsets.get(1) : 0).dy(this.bloatPath.shouldOffsetY() ? (Integer)offsets.get(1) : 0);
            }));
        }
    }
}

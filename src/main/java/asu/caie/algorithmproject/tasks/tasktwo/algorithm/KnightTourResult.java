package asu.caie.algorithmproject.tasks.tasktwo.algorithm;

import java.util.List;

public class KnightTourResult {
    public List<int[]> path;
    public boolean isClosed;
    public boolean visitedAll;

    public KnightTourResult(List<int[]> path, boolean isClosed, boolean visitedAll) {
        this.path = path;
        this.isClosed = isClosed;
        this.visitedAll = visitedAll;
    }
}
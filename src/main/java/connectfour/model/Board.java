package connectfour.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    public static final int DEFAULT_ROWS = 6;
    public static final int DEFAULT_COLUMNS = 7;

    private final int rows;
    private final int columns;
    private final Disc[][] cells;
    private final int[] heights;

    public Board() {
        this(DEFAULT_ROWS, DEFAULT_COLUMNS);
    }

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("A board needs at least one row and one column");
        }
        this.rows = rows;
        this.columns = columns;
        this.cells = new Disc[rows][columns];
        this.heights = new int[columns];
    }

    public int drop(int column, Disc disc) {
        Objects.requireNonNull(disc, "disc");
        if (!isValidColumn(column)) {
            throw new IllegalArgumentException("Column " + column + " is outside the board");
        }
        if (isColumnFull(column)) {
            throw new IllegalArgumentException("Column " + column + " is full");
        }
        int row = heights[column]++;
        cells[row][column] = disc;
        return row;
    }

    public Disc removeTop(int column) {
        if (!isValidColumn(column) || heights[column] == 0) {
            throw new IllegalStateException("Column " + column + " has no disc to remove");
        }
        int row = --heights[column];
        Disc removed = cells[row][column];
        cells[row][column] = null;
        return removed;
    }

    public Disc getDisc(int row, int column) {
        if (!isInside(row, column)) {
            throw new IndexOutOfBoundsException("(" + row + ", " + column + ") is outside the board");
        }
        return cells[row][column];
    }

    public boolean isInside(int row, int column) {
        return row >= 0 && row < rows && isValidColumn(column);
    }

    public boolean isValidColumn(int column) {
        return column >= 0 && column < columns;
    }

    public boolean isColumnFull(int column) {
        return heights[column] == rows;
    }

    public boolean canDropIn(int column) {
        return isValidColumn(column) && !isColumnFull(column);
    }

    public List<Integer> availableColumns() {
        List<Integer> available = new ArrayList<>();
        for (int column = 0; column < columns; column++) {
            if (!isColumnFull(column)) {
                available.add(column);
            }
        }
        return available;
    }

    public boolean isFull() {
        return availableColumns().isEmpty();
    }

    public boolean isEmpty() {
        for (int height : heights) {
            if (height > 0) {
                return false;
            }
        }
        return true;
    }

    public int getHeight(int column) {
        return heights[column];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Board copy() {
        Board copy = new Board(rows, columns);
        for (int row = 0; row < rows; row++) {
            System.arraycopy(cells[row], 0, copy.cells[row], 0, columns);
        }
        System.arraycopy(heights, 0, copy.heights, 0, columns);
        return copy;
    }
}

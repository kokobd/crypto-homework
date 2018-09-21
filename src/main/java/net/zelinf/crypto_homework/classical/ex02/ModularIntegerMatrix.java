package net.zelinf.crypto_homework.classical.ex02;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.*;

import java.util.*;

public final class ModularIntegerMatrix implements AnyMatrix {

    private RealMatrix matrix;

    private ModularArithmetic mod;

    private ModularIntegerMatrix(int mod, RealMatrix matrix) {
        this.matrix = matrix;
        this.mod = new ModularArithmetic(mod);
    }

    public ModularIntegerMatrix(int mod, int rowDimension, int columnDimension) {
        this.mod = new ModularArithmetic(mod);
        matrix = new Array2DRowRealMatrix(rowDimension, columnDimension);
    }

    public ModularIntegerMatrix(int mod, int[][] data) {
        this(mod, data.length, data[0].length);
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[0].length; ++j) {
                setEntry(i, j, data[i][j]);
            }
        }
    }

    /**
     * Matrix multiplication
     *
     * @param another the matrix to multiply with this one
     * @return the result
     * @throws IllegalArgumentException If the argument doesn't have matching size.
     */
    public ModularIntegerMatrix multiply(ModularIntegerMatrix another) {
        if (getColumnDimension() != another.getRowDimension() || getMod() != another.getMod())
            throw new IllegalArgumentException();

        ModularIntegerMatrix result =
                new ModularIntegerMatrix(getMod(), getRowDimension(), another.getColumnDimension());
        for (int i = 0; i < result.getRowDimension(); ++i) {
            for (int j = 0; j < result.getColumnDimension(); ++j) {
                int sum = 0;
                for (int n = 0; n < getColumnDimension(); ++n) {
                    sum += mod.multiply(getEntry(i, n), another.getEntry(n, j));
                }
                result.setEntry(i, j, sum);
            }
        }
        return result;
    }

    /**
     * Matrix addition. Doesn't modify original matrix.
     *
     * @param another another matrix to add to this one
     * @return the sum
     * @throws IllegalArgumentException If the argument doesn't have the same size.
     */
    public ModularIntegerMatrix add(ModularIntegerMatrix another) {
        checkForSameSize(another);
        ModularIntegerMatrix result = this.copy();

        for (int i = 0; i < getRowDimension(); ++i) {
            for (int j = 0; j < getColumnDimension(); ++j) {
                result.setEntry(i, j, mod.sum(getEntry(i, j), another.getEntry(i, j)));
            }
        }
        return result;
    }

    /**
     * Matrix subtraction. Doesn't modify original matrix.
     *
     * @param another the matrix to subtract from this one
     * @return the result
     * @throws IllegalArgumentException If the argument doesn't have the same size.
     */
    public ModularIntegerMatrix subtract(ModularIntegerMatrix another) {
        checkForSameSize(another);
        ModularIntegerMatrix result = new ModularIntegerMatrix(getMod(), getRowDimension(), getColumnDimension());

        for (int i = 0; i < getRowDimension(); ++i) {
            for (int j = 0; j < getColumnDimension(); ++j) {
                result.setEntry(i, j, mod.subtract(getEntry(i, j), another.getEntry(i, j)));
            }
        }
        return result;
    }

    private void checkForSameSize(ModularIntegerMatrix another) {
        if (another.getColumnDimension() != getColumnDimension()
                || another.getRowDimension() != getRowDimension()
                || getMod() != another.getMod())
            throw new IllegalArgumentException();
    }


    public Optional<ModularIntegerMatrix> getInverse() {
        if (!isSquare()) {
            return Optional.empty();
        }

        final int dimension = getRowDimension();

        ModularIntegerMatrix extendedMat = new ModularIntegerMatrix(getMod(), dimension, dimension * 2);
        extendedMat.setSubMatrix(this, 0, 0);
        extendedMat.setSubMatrix(createIdentityMatrix(getMod(), dimension), 0, dimension);
        if (!extendedMat.reducedRowEchelonForm())
            return Optional.empty();

        boolean hasInverse = true;
        outerLoop:
        for (int row = 0; row < dimension; ++row) {
            for (int col = 0; col < dimension; ++col) {
                if (row == col && extendedMat.getEntry(row, col) != 1
                        || row != col && extendedMat.getEntry(row, col) != 0) {
                    hasInverse = false;
                    break outerLoop;
                }
            }
        }

        if (!hasInverse) {
            return Optional.empty();
        }

        ModularIntegerMatrix inverse =
                extendedMat.getSubMatrix(0, dimension - 1, dimension, dimension * 2 - 1);
        return Optional.of(inverse);
    }

    public void setSubMatrix(ModularIntegerMatrix subMatrix, int row, int col) {
        matrix.setSubMatrix(subMatrix.matrix.getData(), row, col);
    }

    /**
     * Convert current matrix to reduced row echelon form.
     * If the transformation fails, content of the matrix is
     * undefined.
     *
     * @return true if the transformation is successful
     */
    public boolean reducedRowEchelonForm() {
        for (int row = 0; row < getRowDimension(); ++row) {
            final List<Integer> candidates = rowsWithLeastLeadingZeros(row);
            if (candidates.isEmpty())
                throw new IllegalStateException();
            final Optional<Integer> pivotColumnO = firstNonZeroColumn(candidates.get(0));
            if (!pivotColumnO.isPresent())
                return true;
            final int pivotColumn = pivotColumnO.get();

            Optional<Integer> rowToSwap = Optional.empty();
            findRowToSwap:
            for (int i = 0; i < candidates.size(); ++i) {
                int rowI = candidates.get(i);
                int value = getEntry(rowI, pivotColumn);
                Optional<Integer> inverseO = mod.multiplicativeInvert(value);
                if (inverseO.isPresent()) {
                    multiplyRow(rowI, inverseO.get());
                    rowToSwap = Optional.of(rowI);
                    break;
                } else {
                    for (int rowJ = 0; rowJ < candidates.size(); ++rowJ) {
                        if (rowJ == rowI)
                            continue;
                        int valueJ = getEntry(rowJ, pivotColumn);
                        Optional<Pair<Integer, Integer>> bezoutC = mod.bezoutCoefficients(value, valueJ);
                        if (bezoutC.isPresent()) {
                            multiplyRow(rowI, bezoutC.get().getLeft());
                            addRows(rowJ, rowI, bezoutC.get().getRight());
                            rowToSwap = Optional.of(rowI);
                            break findRowToSwap;
                        }
                    }
                }
            }

            if (rowToSwap.isPresent()) {
                switchRows(rowToSwap.get(), row);
            }

            for (int otherRow = 0; otherRow < getRowDimension(); ++otherRow) {
                if (otherRow == row)
                    continue;
                int value = getEntry(otherRow, pivotColumn);
                if (value != 0) {
                    addRows(row, otherRow, mod.invert(value));
                }
            }
        }
        return true;
    }

    /**
     * Row switching.
     * This is one of the elementary row operations
     */
    private void switchRows(int row1, int row2) {
        if (row1 != row2) {
            RealVector row1_v = matrix.getRowVector(row1);
            RealVector row2_v = matrix.getRowVector(row2);
            matrix.setRowVector(row1, row2_v);
            matrix.setRowVector(row2, row1_v);
        }
    }

    /**
     * Row Multiplication.
     * This is one of the elementary row operations.
     */
    private void multiplyRow(int row, int k) {
        for (int column = 0; column < getColumnDimension(); ++column) {
            setEntry(row, column, mod.multiply(getEntry(row, column), k));
        }
    }

    /**
     * Row Addition
     * This is one of the elementary row operations.
     */
    private void addRows(int from, int to, int k) {
        for (int col = 0; col < getColumnDimension(); ++col) {
            final int value = mod.sum(mod.multiply(getEntry(from, col), k), getEntry(to, col));
            setEntry(to, col, value);
        }
    }

    private List<Integer> rowsWithLeastLeadingZeros(int from) {
        ArrayList<Integer> rows = new ArrayList<>();
        int minLeadingZeros = Integer.MAX_VALUE;

        for (int row = from; row < getRowDimension(); ++row) {
            int leadingZeros = 0;
            for (int col = 0; col < getColumnDimension(); ++col) {
                if (getEntry(row, col) != 0) {
                    break;
                }
                ++leadingZeros;
            }
            if (leadingZeros < minLeadingZeros) {
                rows.clear();
                rows.add(row);
                minLeadingZeros = leadingZeros;
            } else if (leadingZeros == minLeadingZeros) {
                rows.add(row);
            }
        }
        return rows;
    }

    private Optional<Integer> firstNonZeroColumn(int row) {
        for (int col = 0; col < getColumnDimension(); ++col) {
            if (getEntry(row, col) != 0) {
                return Optional.of(col);
            }
        }
        return Optional.empty();
    }

    public ModularIntegerMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) {
        return new ModularIntegerMatrix(getMod(), matrix.getSubMatrix(startRow, endRow, startColumn, endColumn));
    }

    public static ModularIntegerMatrix createIdentityMatrix(int mod, int dimension) {
        return new ModularIntegerMatrix(mod, MatrixUtils.createRealIdentityMatrix(dimension));
    }

    public ModularIntegerMatrix copy() {
        return new ModularIntegerMatrix(getMod(), matrix.copy());
    }

    public int getEntry(int row, int column) {
        return (int) matrix.getEntry(row, column);
    }

    public void setEntry(int row, int column, int value) {
        matrix.setEntry(row, column, mod(value));
    }

    private int mod(int x) {
        return x % getMod();
    }

    public int getMod() {
        return mod.getMod();
    }

    @Override
    public boolean isSquare() {
        return matrix.isSquare();
    }

    @Override
    public int getRowDimension() {
        return matrix.getRowDimension();
    }

    @Override
    public int getColumnDimension() {
        return matrix.getColumnDimension();
    }

    @Override
    public String toString() {
        return matrix.toString();
    }


    private int[][] getData() {
        int[][] data = new int[getRowDimension()][getColumnDimension()];
        for (int row = 0; row < getRowDimension(); ++row) {
            for (int col = 0; col < getColumnDimension(); ++col) {
                data[row][col] = getEntry(row, col);
            }
        }
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModularIntegerMatrix that = (ModularIntegerMatrix) o;
        return Objects.equals(matrix, that.matrix) &&
                Objects.equals(mod, that.mod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matrix, mod);
    }
}

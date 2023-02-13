import java.io.File
import java.nio.file.Paths
import java.util.Scanner

class Sudoku(private val field: ArrayList<ArrayList<Int>>) {

    private val fieldSize = 9

    private val possibleInCells: ArrayList<ArrayList<MutableSet<Int>>> = ArrayList()
    private val emptyCells: ArrayList<Pair<Int, Int>> = ArrayList()

    init {
        for (i in 0 until fieldSize) {
            possibleInCells.add(arrayListOf())
            for (j in 0 until fieldSize)
                possibleInCells[i].add(mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        }
        for (i in 0 until fieldSize) {
            for (j in 0 until fieldSize) {
                if (field[i][j] != 0) {
                    fillInTheCell(i, j, field[i][j])
                } else {
                    emptyCells.add(Pair(i, j))
                }
            }
        }
    }

    private fun fillInTheCell(row: Int, column: Int, number: Int) {
        field[row][column] = number
        for (k in 0 until 9) {
            val iForSquare = row / 3 * 3 + k / 3
            val jForSquare = column / 3 * 3 + k % 3
            possibleInCells[row][k].remove(field[row][column])
            possibleInCells[k][column].remove(field[row][column])
            possibleInCells[iForSquare][jForSquare].remove(field[row][column])
        }
    }

    private fun getPossibleInOtherCells(
        row: Int,
        column: Int
    ): Triple<MutableSet<Int>, MutableSet<Int>, MutableSet<Int>> {
        val possibleInOtherCellsInRow = mutableSetOf<Int>()
        val possibleInOtherCellsInColumn = mutableSetOf<Int>()
        val possibleInOtherCellsInSquare = mutableSetOf<Int>()
        for (k in 0 until 9) {
            val iForSquare = row / 3 * 3 + k / 3
            val jForSquare = column / 3 * 3 + k % 3
            if (field[row][k] == 0 && k != column)
                possibleInOtherCellsInRow.addAll(possibleInCells[row][k])
            if (field[k][column] == 0 && k != row)
                possibleInOtherCellsInColumn.addAll(possibleInCells[k][column])
            if (field[iForSquare][jForSquare] == 0 && Pair(iForSquare, jForSquare) != Pair(row, column))
                possibleInOtherCellsInSquare.addAll(possibleInCells[iForSquare][jForSquare])
        }
        return Triple(possibleInOtherCellsInRow, possibleInOtherCellsInColumn, possibleInOtherCellsInSquare)
    }

    private fun getNakedPairs() {

    }

    private fun fillInEmptyCells() {
        val newEmptyCells: ArrayList<Pair<Int, Int>> = ArrayList()
        for ((i, j) in emptyCells) {
            var filled = false
            if (possibleInCells[i][j].size == 1) {
                fillInTheCell(i, j, possibleInCells[i][j].first())
                filled = true
            } else {
                val possibleInOtherCells = getPossibleInOtherCells(i, j)
                for (number in possibleInCells[i][j]) {
                    if (number !in possibleInOtherCells.first || number !in possibleInOtherCells.second || number !in possibleInOtherCells.third) {
                        fillInTheCell(i, j, number)
                        filled = true
                        break
                    }
                }
            }
            if (!filled)
                newEmptyCells.add(Pair(i, j))
        }
        emptyCells.clear()
        emptyCells.addAll(newEmptyCells)
    }

    fun solve() {
        print()
        while (isNotSolved()) {
            fillInEmptyCells()
            print()
        }
    }

    fun print() {
        var s = ""
        for (i in field) {
            for (j in i)
                s += "$j "
            s += "\n"
        }
        print(s + "\n")
    }

    private fun isNotSolved(): Boolean = emptyCells.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (other is Sudoku)
            return other.field == field
        return false
    }
}

fun main() {
    val input: ArrayList<ArrayList<Int>> = ArrayList()
    val dirPath = "${Paths.get("").toAbsolutePath()}\\src\\main\\kotlin\\"
    val paths = listOf(dirPath + "testInput.txt", dirPath + "easyInput.txt", dirPath + "mediumInput.txt")
    val sc = Scanner(File(paths[0]))
    for (i in 0 until 9) {
        input.add(arrayListOf())
        for (j in 0 until 9) {
            input[i].add(sc.nextInt())
        }
    }
    val sudoku = Sudoku(input)
    sudoku.solve()
}
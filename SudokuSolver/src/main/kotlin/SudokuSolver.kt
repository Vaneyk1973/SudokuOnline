import java.io.File
import java.nio.file.Paths
import java.util.Scanner

class Sudoku(val field: ArrayList<ArrayList<Int>>) {

    private val fieldSize = 9

    private val possibleInRow: ArrayList<MutableSet<Int>> = ArrayList()
    private val possibleInColumn: ArrayList<MutableSet<Int>> = ArrayList()
    private val possibleInSquare: ArrayList<MutableSet<Int>> = ArrayList()
    private val emptyCells: ArrayList<Pair<Int, Int>> = ArrayList()

    init {
        for (i in 0 until fieldSize) {
            possibleInSquare.add(mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
            possibleInColumn.add(mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
            possibleInRow.add(mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        }
        for (i in 0 until fieldSize) {
            for (j in 0 until fieldSize) {
                if (field[i][j] != 0) {
                    possibleInRow[i].remove(field[i][j])
                    possibleInColumn[j].remove(field[i][j])
                    possibleInSquare[i / 3 * 3 + j / 3].remove(field[i][j])
                } else {
                    emptyCells.add(Pair(i, j))
                }
            }
        }
    }

    private fun fillInTheCell(row: Int, column: Int, number: Int) {
        field[row][column] = number
        possibleInSquare[row / 3 * 3 + column / 3].remove(number)
        possibleInRow[row].remove(number)
        possibleInColumn[column].remove(number)
    }

    fun check() {
        val newEmptyCells: ArrayList<Pair<Int, Int>> = ArrayList()
        for ((i, j) in emptyCells) {
            var filled = false
            val possibleInCell =
                possibleInColumn[j].intersect(possibleInRow[i].intersect(possibleInSquare[i / 3 * 3 + j / 3]))
            if (possibleInCell.size == 1) {
                fillInTheCell(i, j, possibleInCell.first())
                filled = true
            } else {
                val possibleInOtherCellsInRow = mutableSetOf<Int>()
                val possibleInOtherCellsInColumn = mutableSetOf<Int>()
                val possibleInOtherCellsInSquare = mutableSetOf<Int>()
                for (k in 0 until 9) {
                    val iForSquare = i / 3 * 3 + k / 3
                    val jForSquare = j / 3 * 3 + k % 3
                    if (field[i][k] == 0 && k != j)
                        possibleInOtherCellsInRow.addAll(
                            possibleInColumn[k].intersect(
                                possibleInRow[i].intersect(
                                    possibleInSquare[i / 3 * 3 + k / 3]
                                )
                            )
                        )
                    if (field[k][j] == 0 && k != i)
                        possibleInOtherCellsInColumn.addAll(
                            possibleInColumn[j].intersect(
                                possibleInRow[k].intersect(
                                    possibleInSquare[k / 3 * 3 + j / 3]
                                )
                            )
                        )
                    if (field[iForSquare][jForSquare] == 0 && Pair(iForSquare, jForSquare) != Pair(i, j))
                        possibleInOtherCellsInSquare.addAll(
                            possibleInColumn[jForSquare].intersect(
                                possibleInRow[iForSquare].intersect(
                                    possibleInSquare[i / 3 * 3 + j / 3]
                                )
                            )
                        )
                }
                for (number in possibleInCell) {
                    if (number !in possibleInOtherCellsInRow || number !in possibleInOtherCellsInColumn || number !in possibleInOtherCellsInSquare) {
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

    fun print() {
        var s = ""
        for (i in field) {
            for (j in i)
                s += "$j "
            s += "\n"
        }
        print(s + "\n")
    }

    fun printPossibleInSquares() {
        var s = ""
        for (i in possibleInSquare) {
            for (j in i)
                s += "$j "
            s += "\n"
        }
        print(s)
    }

    fun printPossibleInColumns() {
        var s = ""
        for (i in possibleInColumn) {
            for (j in i)
                s += "$j "
            s += "\n"
        }
        print(s)
    }

    fun printPossibleInRows() {
        var s = ""
        for (i in possibleInRow) {
            for (j in i)
                s += "$j "
            s += "\n"
        }
        print(s)
    }

    fun isNotSolved(): Boolean = emptyCells.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (other is Sudoku) {
            return other.field == field
        }
        return false
    }

    override fun hashCode(): Int {
        var result = field.hashCode()
        result = 31 * result + fieldSize
        result = 31 * result + possibleInRow.hashCode()
        result = 31 * result + possibleInColumn.hashCode()
        result = 31 * result + possibleInSquare.hashCode()
        result = 31 * result + emptyCells.hashCode()
        return result
    }
}


fun solve(sudoku: Sudoku) {
    sudoku.print()
    while (sudoku.isNotSolved()) {
        sudoku.check()
        sudoku.print()
    }
}

fun main() {
    val input: ArrayList<ArrayList<Int>> = ArrayList()
    val dirPath = "${Paths.get("").toAbsolutePath()}\\src\\main\\kotlin\\"
    val paths= listOf(dirPath + "testInput.txt", dirPath + "easyInput.txt", dirPath + "mediumInput.txt")
    val sc = Scanner(File(paths[0]))
    for (i in 0 until 9) {
        input.add(arrayListOf())
        for (j in 0 until 9) {
            input[i].add(sc.nextInt())
        }
    }
    val sudoku = Sudoku(input)
    solve(sudoku)
}
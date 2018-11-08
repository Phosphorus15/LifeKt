import java.awt.*
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

class Matrix(internal val width: Int, internal val height: Int) : Cloneable {

    var obj: Array<BitSet> = Array(height) { _ -> BitSet(width) }

    private val random = Random()

    operator fun get(x: Int) = obj[x]

    fun rand(density: Double = .5) = obj.forEach { set ->
        (0 until width).forEach {
            set[it] = random.nextDouble() <= density
        }
    }

    override fun clone(): Matrix = Matrix(width, height).apply {
        (0 until height).forEach { x ->
            (0 until width).forEach { y ->
                obj[x][y] = this@Matrix[x][y]
            }
        }
    }

    private fun getX(x: Int) = when {
        x < 0 -> height + x
        x >= height -> x - height
        else -> x
    }

    private fun getY(y: Int) = when {
        y < 0 -> height + y
        y >= height -> y - height
        else -> y
    }

    fun next() {
        val mat = Matrix(width, height)
        (0 until height).forEach { x ->
            (0 until width).forEach { y ->
                val ct = acc(x, y)
                mat[x][y] = ((obj[x][y]
                        && ct == 2) || ct == 3)
            }
        }
        obj = mat.obj
    }

    private fun acc(x: Int, y: Int): Int {
        var acc = 0
        (-1..1).forEach { dx ->
            (-1..1).forEach { dy ->
                if (dx != dy || dx != 0)
                    if (obj[getX(x + dx)][getY(y + dy)]) acc++
            }
        }
        return acc
    }

    override fun toString(): String {
        var buffer = ""
        obj.forEach { set ->
            (0 until width).forEach {
                buffer += if (set[it]) 'x' else 'o'
                buffer += ' '
            }
            buffer += '\n'
        }
        return buffer
    }

    fun empty() = !obj.map { !it.isEmpty }.stream().anyMatch { it == true }
}

class PaintCanvas(var matrix: Matrix) : JPanel() {

    init {
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        matrix.paint(g as Graphics2D, width, height, Color.GREEN, Color.BLACK)
    }

}

fun Color.zoom(rate: Double): Color = Color((red * rate).toInt(), (green * rate).toInt(), (blue * rate).toInt())

fun Matrix.paint(g: Graphics2D, w: Int, h: Int
                 , colorA: Color = Color.BLACK, colorB: Color = Color.WHITE) {
    val tileH = h / this.height
    val tileW = w / this.width
    val tileI = Math.min(tileH, tileW)
    (0 until height).forEach { x ->
        (0 until width).forEach { y ->
            g.color = if (obj[x][y]) colorA.zoom(Random().nextDouble()) else colorB
            g.fillRect(y * tileI, x * tileI
                    , tileI, tileI)
        }
    }
}

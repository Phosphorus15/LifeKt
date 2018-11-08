import java.awt.Dimension
import java.awt.Toolkit
import java.util.*
import javax.swing.JFrame
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    println(Toolkit.getDefaultToolkit().screenSize)
    val zooming = 8
    val mat = Matrix(1920 / zooming, 1080 / zooming)
    val mat2 = Matrix(1920 / zooming, 1920 / zooming)
    val mat3 = Matrix(1920 / zooming, 1920 / zooming)
    //mat.rand(.3)
    val frame = JFrame().apply {
        size = Dimension(Toolkit.getDefaultToolkit().screenSize)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        contentPane = PaintCanvas(mat)
        isUndecorated = true
        isVisible = true
    }
    frame.repaint()
    val refresh = 20L
    while (true) {
        Thread.sleep(refresh)
        var lp = 0
        val dens = Random().nextDouble()
        //println(mat)
        mat.rand(dens)
        while (!mat.empty()) {
            mat3.obj = mat2.obj.clone()
            mat2.obj = mat.obj.clone()
            mat.next()
            if (mat.obj.contentEquals(mat2.obj)
                    || mat.obj.contentEquals(mat3.obj)) break
            lp++
            Thread.sleep(refresh)
            frame.repaint()
            frame.title = "Loop $lp, with density $dens"
            Thread.sleep(refresh)
        }
        //println("e")
    }
}
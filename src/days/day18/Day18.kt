package days.day18

import readInputList
import java.util.*
import kotlin.collections.ArrayDeque

data class Point(val x: Int, val y: Int, val z: Int)
fun part1(input: List<String>): Int {
    val faceX = mutableMapOf<Point,Int>()
    val faceY = mutableMapOf<Point,Int>()
    val faceZ = mutableMapOf<Point,Int>()
    for (line in input) {
        val scan = Scanner(line)
        scan.useDelimiter(",")
        val x = scan.nextInt()
        val y = scan.nextInt()
        val z = scan.nextInt()
        faceX[Point(x,y,z)] = (faceX[Point(x,y,z)]?:0) + 1
        faceX[Point(x+1,y,z)] = (faceX[Point(x+1,y,z)]?:0) + 1
        faceY[Point(x,y,z)] = (faceY[Point(x,y,z)]?:0) + 1
        faceY[Point(x,y+1,z)] = (faceY[Point(x,y+1,z)]?:0) + 1
        faceZ[Point(x,y,z)] = (faceZ[Point(x,y,z)]?:0) + 1
        faceZ[Point(x,y,z+1)] = (faceZ[Point(x,y,z+1)]?:0) + 1
    }
    return faceX.filter { (_,v)->v==1 }.size +
            faceY.filter { (_,v)->v==1 }.size +
            faceZ.filter { (_,v)->v==1 }.size
}

fun part2(input: List<String>): Int {
    val faceX = mutableSetOf<Point>()
    val faceY = mutableSetOf<Point>()
    val faceZ = mutableSetOf<Point>()
    for (line in input) {
        val scan = Scanner(line)
        scan.useDelimiter(",")
        val x = scan.nextInt()
        val y = scan.nextInt()
        val z = scan.nextInt()
        faceX.add(Point(x,y,z))
        faceX.add(Point(x+1,y,z))
        faceY.add(Point(x,y,z))
        faceY.add(Point(x,y+1,z))
        faceZ.add(Point(x,y,z))
        faceZ.add(Point(x,y,z+1))
    }
    val minX = faceX.minOf { f -> f.x } - 1
    val maxX = faceX.maxOf { f -> f.x } + 1
    val minY = faceY.minOf { f -> f.y } - 1
    val maxY = faceY.maxOf { f -> f.y } + 1
    val minZ = faceX.minOf { f -> f.z } - 1
    val maxZ = faceX.maxOf { f -> f.z } + 1

    val steam = mutableSetOf<Point>()
    val queue = ArrayDeque<Point>()
    queue.add(Point(minX,minY,minZ))
    var faces = 0
    while (queue.isNotEmpty()) {
        val cube = queue.removeFirst()
        if (cube in steam) continue
        else steam.add(cube)
        if (cube.x > minX) {
            val left = cube.copy(x=cube.x-1)
            if (cube in faceX) {
                faces++
            } else
                queue.addLast(left)
        }
        if (cube.x < maxX) {
            val right = cube.copy(x=cube.x+1)
            if (right in faceX) {
                faces++
            } else
                queue.addLast(right)
        }
        if (cube.y > minY) {
            val front = cube.copy(y=cube.y-1)
            if (cube in faceY) {
                faces++
            } else
                queue.addLast(front)
        }
        if (cube.y < maxY) {
            val back = cube.copy(y=cube.y+1)
            if (back in faceY) {
                faces++
            } else
                queue.addLast(back)
        }
        if (cube.z > minZ) {
            val bot = cube.copy(z=cube.z-1)
            if (cube in faceZ) {
                faces++
            } else
                queue.addLast(bot)
        }
        if (cube.z < maxZ) {
            val top = cube.copy(z=cube.z+1)
            if (top in faceZ) {
                faces++
            } else
                queue.addLast(top)
        }
    }
    return faces
}

fun main() {

    val input = readInputList(18)
    println(part1(input))
    println(part2(input))
}

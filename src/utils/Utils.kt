import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInputList(day: Int): List<String> {
    val dayStringValue = day.toString().padStart(2, '0')
    return File("src/days/day${dayStringValue}", "Day${dayStringValue}.txt")
        .readLines()
}

fun readInput(day: Int): String {
    val dayStringValue = day.toString().padStart(2, '0')
    return File("src/days/day${dayStringValue}", "Day${dayStringValue}.txt")
        .readText()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

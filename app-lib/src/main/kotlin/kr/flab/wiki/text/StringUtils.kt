package kr.flab.wiki.text

import java.util.regex.Pattern

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

fun String.matchesIn(pattern: Pattern) = pattern.matcher(this).matches()

@SuppressWarnings("MagicNumber")
fun ByteArray.toHexString(): String {
    val hexChars = CharArray(this.size * 2)
    for (j in this.indices) {
        val v = this[j].toInt() and 0xFF
        hexChars[j * 2] = HEX_ARRAY[v ushr 4]
        hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }
    return String(hexChars)
}

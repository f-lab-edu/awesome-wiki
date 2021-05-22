package kr.flab.wiki.lib.time

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun utcNow(): LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)

fun LocalDateTime.utcEpochSecond(): Long = this.toEpochSecond(ZoneOffset.UTC)

/**
 * Do not use this method after year 2038 if timestamp is 4 bytes long only.
 */
fun utcLocalDateTimeOf(epochTimestamp: Number): LocalDateTime =
    utcLocalDateTimeOf(epochTimestamp.toLong())

fun utcLocalDateTimeOf(epochTimestamp: Long): LocalDateTime = if (epochTimestamp > Integer.MAX_VALUE) {
    LocalDateTime.ofInstant(Instant.ofEpochMilli(epochTimestamp), ZoneOffset.UTC)
} else {
    LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTimestamp), ZoneOffset.UTC)
}

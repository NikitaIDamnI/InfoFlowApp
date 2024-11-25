
package com.example.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val SECONDS_IN_MINUTE = 60
private const val MINUTES_IN_HOUR = 60
private const val HOURS_IN_DAY = 24
private const val DAYS_IN_MONTH = 30
private const val DAYS_IN_YEAR = 365
private const val MIN_MONTHS_IN_YEAR = 12
private const val PLURAL_RANGE_START = 11
private const val PLURAL_RANGE_END = 19
private const val DATE_FORMAT_PATTERN = "dd MMMM, HH:mm, yyyy"
private const val LAST_DIGIT_DIVIDER = 10
private const val LAST_TWO_DIGITS_DIVIDER = 100
private const val ONE = 1
private const val TWO = 2
private const val FOUR = 4

fun Date.getTimeAgo(): String {
    val now = Date()
    val diffInMillis = now.time - this.time

    val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
    val months = days / DAYS_IN_MONTH
    val years = days / DAYS_IN_YEAR

    return when {
        seconds < SECONDS_IN_MINUTE -> "только что"
        minutes < MINUTES_IN_HOUR ->
            "$minutes ${
                getPluralForm(
                    minutes.toInt(),
                    "минуту",
                    "минуты",
                    "минут"
                )
            } назад"

        hours < HOURS_IN_DAY ->
            "$hours ${
                getPluralForm(
                    hours.toInt(),
                    "час",
                    "часа",
                    "часов"
                )
            } назад"

        days < DAYS_IN_MONTH -> "$days ${getPluralForm(days.toInt(), "день", "дня", "дней")} назад"
        months < MIN_MONTHS_IN_YEAR ->
            "$months ${
                getPluralForm(
                    months.toInt(),
                    "месяц",
                    "месяца",
                    "месяцев"
                )
            } назад"

        else -> "$years ${getPluralForm(years.toInt(), "год", "года", "лет")} назад"
    }
}

fun Date.getDatePublication(): String {
    val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US)
    return dateFormat.format(this.time)
}

fun getPluralForm(number: Int, form1: String, form2: String, form3: String): String {
    val n = number % LAST_TWO_DIGITS_DIVIDER // Последние две цифры числа
    val n1 = number % LAST_DIGIT_DIVIDER // Последняя цифра числа
    return when {
        n in PLURAL_RANGE_START..PLURAL_RANGE_END -> form3 // 11-19 -> форма 3
        n1 == ONE -> form1 // 1 -> форма 1
        n1 in TWO..FOUR -> form2 // 2-4 -> форма 2
        else -> form3 // Остальные числа -> форма 3
    }
}

fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
    return merge(this, another)
}


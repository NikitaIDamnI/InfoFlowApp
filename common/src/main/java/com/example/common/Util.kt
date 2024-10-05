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

fun Date.getTimeAgo(): String {
    val now = Date()
    val diffInMillis = now.time - this.time

    val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
    val months = days / 30
    val years = days / 365

    return when {
        seconds < 60 -> "только что"
        minutes < 60 -> "$minutes ${
            getPluralForm(
                minutes.toInt(),
                "минуту",
                "минуты",
                "минут"
            )
        } назад"

        hours < 24 -> "$hours ${getPluralForm(hours.toInt(), "час", "часа", "часов")} назад"
        days < 30 -> "$days ${getPluralForm(days.toInt(), "день", "дня", "дней")} назад"
        months < 12 -> "$months ${
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
    val dateFormat = SimpleDateFormat("dd MMMM, HH:mm, yyyy", Locale.US)
    return dateFormat.format(this.time)
}


fun getPluralForm(number: Int, form1: String, form2: String, form3: String): String {
    val n = number % 100
    val n1 = number % 10
    return when {
        n in 11..19 -> form3
        n1 == 1 -> form1
        n1 in 2..4 -> form2
        else -> form3
    }
}

fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
    return merge(this, another)
}



internal object DateTimeUTCSerializer : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    private val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    override fun serialize(
        encoder: Encoder,
        value: Date
    ) = encoder.encodeString(formatter.format(value))

    override fun deserialize(decoder: Decoder): Date = formatter.parse(decoder.decodeString())
}
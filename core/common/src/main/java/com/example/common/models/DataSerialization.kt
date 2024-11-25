@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.common.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val UTC_DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"

object DataSerialization : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    private val formatter: DateFormat = SimpleDateFormat(UTC_DATE_FORMAT_PATTERN, Locale.US)

    override fun serialize(
        encoder: Encoder,
        value: Date
    ) = encoder.encodeString(formatter.format(value))

    override fun deserialize(decoder: Decoder): Date = formatter.parse(decoder.decodeString())
}

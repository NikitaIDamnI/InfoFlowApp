package com.example.data.mappers

import com.example.data.model.Source
import com.example.news.api.models.SourceDTO
import com.example.database.models.Source as SourceDBO

internal fun SourceDBO.toSource(): Source {
    return Source(id = id, name = name)
}

internal fun SourceDTO.toSource(): Source {
    return Source(id = id ?: name, name = name)
}

internal fun SourceDTO.toSourceDbo(): SourceDBO {
    return SourceDBO(id = id ?: name, name = name)
}


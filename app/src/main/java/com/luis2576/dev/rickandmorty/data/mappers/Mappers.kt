package com.luis2576.dev.rickandmorty.data.mappers


import com.luis2576.dev.rickandmorty.data.local.CharacterEntity
import com.luis2576.dev.rickandmorty.data.local.CharacterEntityPreview
import com.luis2576.dev.rickandmorty.data.remote.response.Character
import com.luis2576.dev.rickandmorty.domain.model.MyCharacter
import com.luis2576.dev.rickandmorty.domain.model.MyCharacterPreview

fun CharacterEntity.toMyCharacter(): MyCharacter {
    return MyCharacter(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        originName = this.originName,
        originUrl = this.originUrl,
        locationName = this.locationName,
        locationUrl = this.locationUrl,
        image = this.image,
        episodeList = this.episodeList,
        created = this.created
    )
}

fun CharacterEntityPreview.toMyCharacterPreview(): MyCharacterPreview {
    return MyCharacterPreview(
        id = this.id,
        name = this.name,
        image = this.image,
        gender = this.gender,
        status = this.status
    )
}

fun CharacterEntity.toMyCharacterPreview(): MyCharacterPreview {
    return MyCharacterPreview(
        id = this.id,
        name = this.name,
        image = this.image,
        gender = this.gender,
        status = this.status
    )
}

fun Character.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id.toString(),
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        originName = this.origin.name,
        originUrl = this.origin.url,
        locationName = this.location.name,
        locationUrl = this.location.url,
        image = this.image,
        episodeList = this.episode,
        created = this.created
    )
}

package com.molchanov.cats.domain

data class Cat(
    val imageId: String,
    val imageUrl: String,
    val username: String,
    val favoriteId: String,
    val filename: String,

    val dogFriendly: Int?,
    val strangerFriendly: Int?,
    val hypoallergenic: Int?,
    val indoor: Int?,
    val sheddingLevel: Int?,
    val affectionLevel: Int?,
    val adaptiveLevel: Int?,

    val description: String?,
    val name: String?,
    val origin: String?,
    val altNames: String?,
    val temperament: String?,
    val weight: String?,
    val wikiUrl: String?,
    val lifeSpan: String?


) {

    data class Builder(
        private var imageId: String = "",
        private var imageUrl: String = "",
        private var username: String = "",
        private var favoriteId: String = "",
        private var filename: String = "",

        private var dogFriendly: Int? = null,
        private var strangerFriendly: Int? = null,
        private var hypoallergenic: Int? = null,
        private var indoor: Int? = null,
        private var sheddingLevel: Int? = null,
        private var affectionLevel: Int? = null,
        private var adaptiveLevel: Int? = null,

        private var description: String? = null,
        private var name: String? = null,
        private var origin: String? = null,
        private var altNames: String? = null,
        private var temperament: String? = null,
        private var weight: String? = null,
        private var wikiUrl: String? = null,
        private var lifeSpan: String? = null
    ) {
        fun imageId(s: String) = apply { this.imageId = s }
        fun imageUrl(s: String) = apply { this.imageUrl = s }
        fun username(s: String) = apply { this.username = s }
        fun favoriteId(s: String) = apply { this.favoriteId = s }
        fun filename(s: String) = apply { this.filename = s }
        fun dogFriendly(n: Int?) = apply { this.dogFriendly = n }
        fun strangerFriendly(n: Int?) = apply { this.strangerFriendly = n }
        fun hypoallergenic(n: Int?) = apply { this.hypoallergenic = n }
        fun indoor(n: Int?) = apply { this.indoor = n }
        fun sheddingLevel(n: Int?) = apply { this.sheddingLevel = n }
        fun affectionLevel(n: Int?) = apply { this.affectionLevel = n }
        fun adaptiveLevel(n: Int?) = apply { this.adaptiveLevel = n }
        fun description(s: String?) = apply { this.description = s }
        fun name(s: String?) = apply { this.name = s }
        fun origin(s: String?) = apply { this.origin = s }
        fun altNames(s: String?) = apply { this.altNames = s }
        fun temperament(s: String?) = apply { this.temperament = s }
        fun weight(s: String?) = apply { this.weight = s }
        fun wikiUrl(s: String?) = apply { this.wikiUrl = s }
        fun lifeSpan(s: String?) = apply { this.lifeSpan = s }
        fun build(): Cat {
            return Cat(
                imageId,
                imageUrl,
                username,
                favoriteId,
                filename,
                dogFriendly,
                strangerFriendly,
                hypoallergenic,
                indoor,
                sheddingLevel,
                affectionLevel,
                adaptiveLevel,
                description,
                name,
                origin,
                altNames,
                temperament,
                weight,
                wikiUrl,
                lifeSpan
            )
        }

        var isUploaded: Boolean = filename != ""
        var isFavorites: Boolean = favoriteId != ""
    }
}

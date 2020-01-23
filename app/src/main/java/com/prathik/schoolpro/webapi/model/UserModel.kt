package com.prathik.schoolpro.webapi.model


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Optional

@Serializable
data class UserModel(
    @Optional
    @SerialName("data")
    val `data`: List<Data>,
    @Optional
    @SerialName("page")
    val page: Int,
    @Optional
    @SerialName("per_page")
    val perPage: Int,
    @Optional
    @SerialName("total")
    val total: Int,
    @Optional
    @SerialName("total_pages")
    val totalPages: Int
) {
    @Serializable
    data class Data(
        @Optional
        @SerialName("avatar")
        val avatar: String,
        @Optional
        @SerialName("email")
        val email: String,
        @Optional
        @SerialName("first_name")
        val firstName: String,
        @Optional
        @SerialName("id")
        val id: Int,
        @Optional
        @SerialName("last_name")
        val lastName: String
    )
}
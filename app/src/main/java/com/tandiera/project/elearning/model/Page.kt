package com.tandiera.project.elearning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Page(
    @SerializedName("parts_page")
    val partsPage: List<PartsPage>? = null
) : Parcelable

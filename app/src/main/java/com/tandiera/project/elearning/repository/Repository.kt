package com.tandiera.project.elearning.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tandiera.project.elearning.model.Content
import com.tandiera.project.elearning.model.Material
import java.io.IOException
import java.net.MalformedURLException

object Repository {
    fun getMaterials(context: Context?): MutableList<Material>?{
        val json: String
        try {
            val inputStream = context?.assets?.open("json/materials.json")
            json = inputStream?.bufferedReader().use { it?.readText().toString() }
        }catch (e: IOException){
            e.printStackTrace()
            return null
        }

        val type = object : TypeToken<MutableList<Material>>() {}.type
        val materials = Gson().fromJson<MutableList<Material>>(json, type)
        materials?.let { return it }

        return null
    }

    fun getContents(context: Context?): MutableList<Content>?{
        val json: String
        try {
            val inputStream = context?.assets?.open("json/contents.json")
            json = inputStream?.bufferedReader().use { it?.readText().toString() }
        }catch (e: IOException){
            e.printStackTrace()
            return null
        }

        val type = object : TypeToken<MutableList<Content>>() {}.type
        val materials = Gson().fromJson<MutableList<Content>>(json, type)
        materials?.let { return it }

        return null
    }
}
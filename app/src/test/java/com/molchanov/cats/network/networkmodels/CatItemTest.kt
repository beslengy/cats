package com.molchanov.cats.network.networkmodels


import org.junit.Assert.assertEquals
import org.junit.Test

class CatItemTest {
    @Test
    fun `passing favorite return isFavorite true`(){
        val cat = CatItem(id = "123", url = "image.com", favourite = CatItem.Favourite("456"))
        assertEquals(true, cat.isFavorite)
    }
    @Test
    fun `passing image return isFavorite true`(){
        val cat = CatItem(id = "123", image = CatItem.Image("456", "image.com"))
        assertEquals(true, cat.isFavorite)
    }
    @Test
    fun `passing filename return isUploaded true`(){
        val cat = CatItem(id = "123", url = "image.com", filename = "name.jpeg")
        assertEquals(true, cat.isUploaded)
    }
    @Test
    fun `don't passing favorite return isFavorite false`(){
        val cat = CatItem(id = "123", url = "image.com")
        assertEquals(false, cat.isFavorite)
    }
    @Test
    fun `don't passing image return isFavorite false`(){
        val cat = CatItem(id = "123", url = "image.com")
        assertEquals(false, cat.isFavorite)
    }
}
package ru.urfu.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_characters")
    fun getAllFavorites(): Flow<List<CharacterEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_characters WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(character: CharacterEntity)

    @Query("DELETE FROM favorite_characters WHERE id = :id")
    suspend fun deleteFavorite(id: Int)
}
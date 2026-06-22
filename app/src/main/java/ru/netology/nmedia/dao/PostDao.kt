package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>
    @Upsert
    fun save(post: PostEntity)
    @Query("""
        UPDATE ${PostColumns.TABLE} SET
            ${PostColumns.COLUMN_FAVORITE} = ${PostColumns.COLUMN_FAVORITE} + CASE WHEN ${PostColumns.COLUMN_FAVORITE_BY_ME} THEN -1 ELSE 1 END,
            ${PostColumns.COLUMN_FAVORITE_BY_ME} = CASE WHEN ${PostColumns.COLUMN_FAVORITE_BY_ME} THEN 0 ELSE 1 END
        WHERE id = :id;
    """)
    fun favoriteById(id: Long)
    @Query("""
        UPDATE ${PostColumns.TABLE} SET
            ${PostColumns.COLUMN_SHARE} = ${PostColumns.COLUMN_SHARE} + 1
        WHERE id = :id
    """)
    fun shareById(id: Long)
    @Query("DELETE FROM posts WHERE id = :id")
    fun removeById(id: Long)
}
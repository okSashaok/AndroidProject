package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

object PostColumns {
    const val TABLE = "post"
    const val COLUMN_ID = "id"
    const val COLUMN_AUTHOR = "author"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_DATE_PUBLICATION = "datePublication"
    const val COLUMN_FAVORITE_BY_ME = "favoriteByMe"
    const val COLUMN_FAVORITE = "favorite"
    const val COLUMN_SHARE = "share"
    val ALL_COLUMNS = arrayOf(
        COLUMN_ID,
        COLUMN_AUTHOR,
        COLUMN_CONTENT,
        COLUMN_DATE_PUBLICATION,
        COLUMN_FAVORITE_BY_ME,
        COLUMN_FAVORITE,
        COLUMN_SHARE
    )
}

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
            CREATE TABLE ${PostColumns.TABLE}(
                ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
                ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
                ${PostColumns.COLUMN_DATE_PUBLICATION} TEXT NOT NULL,
                ${PostColumns.COLUMN_FAVORITE_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_FAVORITE} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_SHARE} INTEGER NOT NULL DEFAULT 0
            );
        """.trimIndent()
    }


    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_DATE_PUBLICATION, "now")
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun favoriteById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE} SET
                    ${PostColumns.COLUMN_FAVORITE} = ${PostColumns.COLUMN_FAVORITE} + CASE WHEN ${PostColumns.COLUMN_FAVORITE_BY_ME} THEN -1 ELSE 1 END,
                    ${PostColumns.COLUMN_FAVORITE_BY_ME} = CASE WHEN ${PostColumns.COLUMN_FAVORITE_BY_ME} THEN 0 ELSE 1 END
                WHERE id = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE} SET
                    ${PostColumns.COLUMN_SHARE} = ${PostColumns.COLUMN_SHARE} + 1
                WHERE ${PostColumns.COLUMN_ID} = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                datePublication = getString(getColumnIndexOrThrow(PostColumns.COLUMN_DATE_PUBLICATION)),
                favorite = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_FAVORITE)),
                favoriteByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_FAVORITE_BY_ME)) != 0,
                share = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARE))
            )
        }
    }
}
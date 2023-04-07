package com.example.github.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

data class GithubResponse(

	val totalCount: Int,
	val incompleteResults: Boolean,
	val items: MutableList<ItemsItem>
) {
	@Parcelize
	@Entity(tableName = "user")
	data class ItemsItem(
		@PrimaryKey
		val id: Int,
		@ColumnInfo(name = "avatar_url")
		val avatar_url: String,
		@ColumnInfo(name = "login")
		val login: String,
	) : Parcelable
}



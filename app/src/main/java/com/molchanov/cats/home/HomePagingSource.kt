package com.molchanov.cats.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.molchanov.cats.network.CatsApiService
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class HomePagingSource(
    private val catsApi: CatsApiService,
    private val query: Map<String, String>,
) : PagingSource<Int, CatItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatItem> {
        Log.d("M_PagingSource", "load launched")
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val catItems = catsApi.getAllImages(query, params.loadSize, position)
            Log.d("M_PagingSource", "api getAllImagesLaunched, list size: ${catItems.size}")
            LoadResult.Page(
                data = catItems,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (catItems.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            Log.d("M_PagingSource", "Ошибка: ${e.message}")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.d("M_PagingSource", "Ошибка: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CatItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}

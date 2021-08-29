package com.molchanov.cats.uploaded

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.molchanov.cats.network.CatsApiService
import com.molchanov.cats.network.networkmodels.CatItem
import retrofit2.HttpException
import java.io.IOException

class UploadedPagingSource(
    private val catsApi: CatsApiService,
) : PagingSource<Int, CatItem>() {
    companion object {
        private const val STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatItem> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val catItems = catsApi.getAllUploaded(params.loadSize, position)

            LoadResult.Page(
                data = catItems,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (catItems.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
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
package com.molchanov.cats.utils

import androidx.recyclerview.widget.GridLayoutManager
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.PageAdapter

object Functions {

    //делаем так, чтобы хэдер был на все три столбца
    //так как SpanSizeLookup является абстрактным классом, мы не можем создать его экземпляр
    //зато можем использовать object:
    fun setupManager(
        manager: GridLayoutManager,
        adapter: PageAdapter,
        footerAdapter: CatsLoadStateAdapter,
        headerAdapter: CatsLoadStateAdapter,
    ): GridLayoutManager {
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            //переопределяем обязательный метод и добавляем логику выбора

            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && footerAdapter.itemCount > 0 && headerAdapter.itemCount == 0) {
                    2
                } else if (position == adapter.itemCount + 1 && footerAdapter.itemCount > 0 && headerAdapter.itemCount > 0) {
                    2
                } else if (position == 0 && headerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
        return manager
    }
}


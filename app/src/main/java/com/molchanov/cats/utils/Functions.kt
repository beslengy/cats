package com.molchanov.cats.utils

import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.molchanov.cats.R
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.PageAdapter

object Functions {
    fun showToast(message: String) {
        Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
    }

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

     fun enableExpandedToolbar(enable: Boolean) {
        if (enable) {
            APP_ACTIVITY.findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
        } else {
            APP_ACTIVITY.findViewById<AppBarLayout>(R.id.app_bar).setExpanded(false)
        }

    }
}


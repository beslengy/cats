![Cats logo](/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)
# Сats - приложение для просмотра картинок котиков посредством сервиса [The Cat API](https://thecatapi.com).
## Возможности

- Просмотр случайных изображений котиков.
- Фильтрация по породе и категории.
- Добавление изображений в избранное
- Просмотр подробной информации о котике и голосование.
- Загрузка собственных изображений на сервер (камера или галерея).

## Инструменты и реализация элементов

- Архитектура - паттерны Single Activity и MVVM.
- Навигация по основным фрагментам - BottomNavigationView.
- Основные фрагменты представляют собой экраны RecyclerView. Для заполнения RW и пагинации используется библиотека Paging 3.
- Сеть, JSON - Retrofit 2, OkHTTP, Moshi.
- DI - Dagger Hilt
- Анимация - Material Motion, MotionLayout.
- Загрузка изображений - Glide.
- Данные - ViewModel, LiveData.

## Реализация элементов

![Gif](https://media.giphy.com/media/837VDluMOTnvTvNEXQ/giphy.gif)

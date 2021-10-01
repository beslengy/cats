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

![Gif](https://psv4.userapi.com/c536436/u22843863/docs/d34/66ce5a5c266a/filter_breed.gif?extra=CLmDUqOt2DSm2EdGHFZgUVRqpo3p4TD6HqACjA9DaKV3ZBlrilbJPXc2oumLt1BdblcbxI-qrT9-BOFvR0Lb0hLp0Qaby-BPeIKpY_uwTNrDjxuRHnbUWEnOBweCXLz7K2iVKomcppwLc10ZR8C-8w)

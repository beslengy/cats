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
- Карточка котика и голосование
---
![Детализация](https://media.giphy.com/media/5H1k28L5025eyH7CQl/giphy.gif)

- Фильрация по породе и по категории - Modal bottom sheet
---
![Порода](https://media.giphy.com/media/IaqozjZxFzgSXO5daY/giphy.gif)
![Категория](https://media.giphy.com/media/OZZ090usyYzcBE4IJy/giphy.gif)

- Добавления и удаление из избранного
---
![Добавление избранного](https://media.giphy.com/media/ft9Vla3pksH0mtk4gW/giphy.gif)
![Удаление избранного](https://media.giphy.com/media/8sqs3E3TBBye6VB4DV/giphy.gif)

- Загрузка изображения на сервер
---
![Загрузка через камеру](https://media.giphy.com/media/DFJ5vNe0bpDzB2NhKO/giphy.gif)

- Удаление изображений из загруженных по долгому нажатию
---
![Удаление загруженных](https://media.giphy.com/media/837VDluMOTnvTvNEXQ/giphy.gif)

- Анимация перехода между основными фрагментами - MaterialFadeThrough
---
![Нормальная скорость](https://media.giphy.com/media/uh4VVe8lDPa5cHieQz/giphy.gif)
![Замедленно](https://media.giphy.com/media/1F5xS17pqLzTt17Om7/giphy.gif)

- Анимация перехода на карточку котика - MaterialContainerTransform
---
![Нормальная скорость](https://media.giphy.com/media/dZUK9bvIn3LmmEKLza/giphy.gif)
![Замедленно](https://media.giphy.com/media/S2edju0vSYv9UjFmpL/giphy.gif)

- Сохранение состояние при смене фрагмента через LiveData и SavedStateHandle во ViewModel
---
![Сохранение состояния](https://media.giphy.com/media/chHiWJb6Q7eBu11gm4/giphy.gif)

- Обработка состояний: пустой список, отсутствие сети, медленное соединение.
---
![Отсутствие сети](https://media.giphy.com/media/Q95x10tWecRrdeD9OU/giphy.gif)
![Пустой список](https://media.giphy.com/media/nCs3K0zQsfQumJqt5B/giphy.gif)
![Плохое соединение](https://media.giphy.com/media/a1L9nRIn5M1J34aMXq/giphy.gif)



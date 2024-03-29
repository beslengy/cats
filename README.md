![Cats logo](/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)
# Сats - приложение для просмотра картинок котиков посредством сервиса [The Cat API](https://thecatapi.com).

Ссылка на скачивание [APK](https://drive.google.com/file/d/1gzamgYP8Nnn8_OfG2Jv8vOvfLkRVita_/view?usp=sharing)
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
![Порода](https://media.giphy.com/media/ZhUpc664y6vgNCriel/giphy.gif)
![Категория](https://media.giphy.com/media/OZZ090usyYzcBE4IJy/giphy.gif)

- Добавления и удаление из избранного
---
![Добавление избранного](https://media.giphy.com/media/ft9Vla3pksH0mtk4gW/giphy.gif)
![Удаление избранного](https://media.giphy.com/media/8sqs3E3TBBye6VB4DV/giphy.gif)

- Загрузка изображения на сервер
---
![Загрузка через камеру](https://media.giphy.com/media/XyqthJojzF69dBnHN9/giphy.gif)

- Удаление изображений из загруженных по долгому нажатию
---
![Удаление загруженных](https://media.giphy.com/media/837VDluMOTnvTvNEXQ/giphy.gif)

- Анимация перехода между основными фрагментами - MaterialFadeThrough
---
![Нормальная скорость](https://media.giphy.com/media/m7OSpEkUd2trirENp2/giphy.gif)
![Замедленно](https://media.giphy.com/media/TXqBExBWLxN0V27lED/giphy.gif)

- Анимация перехода на карточку котика - MaterialContainerTransform
---
![Нормальная скорость](https://media.giphy.com/media/3guIrwrDdLDSDw3loE/giphy.gif)
![Замедленно](https://media.giphy.com/media/EfaBDiWQCyO6V65qRX/giphy.gif)

- Обработка состояний: пустой список, отсутствие сети, медленное соединение.
---
![Отсутствие сети](https://media.giphy.com/media/dZDBBYan2tAvxWBSQP/giphy.gif)
![Пустой список](https://media.giphy.com/media/nCs3K0zQsfQumJqt5B/giphy.gif)
![Плохое соединение](https://media.giphy.com/media/a1L9nRIn5M1J34aMXq/giphy.gif)



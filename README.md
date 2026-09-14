# SkillCinema

Приложение для Android: поиск фильмов, оценки, список просмотренного.

## Что умеет
- Поиск фильмов через TMDB API
- Личные списки (посмотреть / просмотрено)
- История просмотров
- Оценки

## Стек
- Kotlin, XML
- MVVM, Clean Architecture
- Room (локальная база)
- Coroutines, Flow
- Hilt (DI)
- Retrofit (сеть)
- JUnit, Mockito, Espresso (тесты)

## Важно
Приложение работает вместе с Leaks — это часть системы, а не отдельное приложение.
Если удалить Leaks, SkillCinema тоже перестанет работать.

## Сборка из исходников (опционально)
./gradlew assembleDebug

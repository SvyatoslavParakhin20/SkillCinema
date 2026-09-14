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

## Скачать
[Скачать SkillCinema v1.0 (APK)](https://github.com/SvyatoslavParakhin20/SkillCinema/releases/download/v1.0/SkillCinema.v1.0.apk)

Требуется Android 12.0 или выше.

## Сборка из исходников (опционально)

**Windows:**
```bash
gradlew.bat assembleDebug
```

**Linux/macOS:**
```bash
./gradlew assembleDebug
```

Готовый APK появится в `app/build/outputs/apk/debug/app-debug.apk`.

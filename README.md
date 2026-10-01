# Приложеение "Прогноз погоды"
Тестовое приложение Прогноз погоды (мобильной разработки) на retrofit

# Технологический стек
- **Minimum SDK level 24, compile/target SDK 34**
- **100% Kotlin + Coroutines + Flow (StateFlow) для асинхронности**
- **UI: Jetpack Compose + Material 3**
  - Кастомная Material 3 тема (палитра и типографика перенесены из ресурсов проекта)
  - Главный экран: поиск города, карта, текущая погода, почасовой прогноз (LazyRow) и прогноз на 3 дня
  - Экран деталей дня: метрики, восход/закат, фазы луны
  - Pull-to-refresh из Material 3
- **Карты и геолокация:** Google Maps SDK + **maps-compose** (карта как composable), FusedLocationProviderClient, Geocoder
- **Архитектура:** MVVM — один общий ViewModel: StateFlow для состояния экрана (sealed interface Loading/Data/Error), LiveData для выбранного дня; фрагменты — тонкие хосты ComposeView
- **Навигация:** Navigation Component (Single Activity)
- **Сеть:** Retrofit2 + Gson + OkHttp3 (API: [weatherapi.com](https://www.weatherapi.com))
- **Изображения:** Coil (AsyncImage)

# Настройка API-ключей
Ключи не хранятся в репозитории — при сборке они читаются из `local.properties` (файл в `.gitignore`):

```properties
WEATHER_API_KEY=<ключ с https://www.weatherapi.com>
MAPS_API_KEY=<ключ Google Maps SDK for Android из Google Cloud Console>
```

Ключи попадают в приложение через `BuildConfig.WEATHER_API_KEY` и manifest placeholder `${MAPS_API_KEY}`.

# Скриншоты
<img align="left" src="https://github.com/user-attachments/assets/3d321f67-54f8-46bb-b36d-fc2d83e4e145" width="240">
<img align="left" src="https://github.com/user-attachments/assets/46d5a5b7-51ee-45ac-8e3e-2606ada6eb87" width="240">
<img align="left" src="https://github.com/user-attachments/assets/d49d46f0-dfc7-4e75-9176-59cdf7430d1e" width="240">

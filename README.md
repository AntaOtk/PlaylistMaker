# PlaylistMaker
Учебное приложение для практики по андроид-разработке с 0.

В приложении использаваны:
MVVM, Retrofit, JSON, Jetpack Navigation Component, Clean Architecture, LiveData, Room, Koin

Техническое задание
Проект представляет собой приложение для поиска треков, использующее API сервиса Itunes. Приложение предоставляет следующую функциональность:

- Поиск треков.
- Хранение истории поиска треков.
- Прослушивание треков
- Ханение избранных треков
- Создание и редактирование плейлиста с возможностью выбора иконки из mediaфайлов на телефоне
- Смена темы приложения
- Добвление и удаокние треков из плейлиста

Ниже представлен список требований и особенностей различных экранов приложения, ознакомьтесь с ним до начала разработки.

Общие требования
Приложение должно поддерживать устройства, начиная с Android 9.0 (minSdkVersion = 28)

# Экран поиска треков
На этом экране пользователь может искать треки по любому непустому набору слов поискового запроса. Результаты поиска представляют собой список, содержащий информацию о треке(название, имя исполнителя, длительность).

История поиска поиска зранится при помощи Shared Preferences. с условиями: 
- В истории должно храниться не больше 10 треков.
- Нажатие на трек в списке результатов поиска добавляет его в историю.
- Треки хранятся в истории в порядке добавления (новые отображаются в верхней части списка). В истории должно храниться не больше 10 треков.
- Если трек, на который нажал пользователь, уже есть в истории, то он должен оказаться в верхней части списка, а предыдущая запись об этом треке должна быть удалена.
- Нажатие на кнопку «Очистить историю» полностью очищает записи истории поиска.

# Аудиоплеер
Когда пользователь находится на экране аудиоплеера, то он видит следующие UI-элементы:
 - Кнопку «Назад» (нажатие на неё эквивалентно нажатию системной кнопки «Назад»),
 - Область для отображения обложки плеера,
 - Название трека (trackName),
 - Имя исполнителя (artistName),
 - Название альбома (collectionName) (если есть),
 - Год релиза трека (releaseDate),
 - Жанр трека (primaryGenreName),
 - Страна исполнителя (country),
 - Продолжительность трека в формате mm:ss (trackTimeMills),
 - Кнопка добавления в плейлист,
 - Кнопка добавления в избранное,
 - Кнопка контроля воспроизведения,
 - Прогресс воспроизведения (время под кнопкой контроля воспроизведения в формате mm:ss).
   
![Screenshot_20231213_125132](https://github.com/AntaOtk/PlaylistMaker/assets/118642167/be24a1af-16c2-4de3-85e4-8a76d2f8ed22)

дополнитлельные условия
- Логикв воспроизведения отрывка трека на экране «Аудиоплеер» реализована с использованием MediaPlayer.
- Управление воспроизведением должно осуществляться при помощи кнопки в центре экрана. во время проигрования состояние кнопки «Пауза».
- Отображактся текущее время воспроизведения отрывка
- Добавление в избранное и удаление, происходит нажатием на кнопку «Нравится» с изображением сердечка. Цвет сердца опркляет содержание трека в списке изьранного
- При нажатии на добавить кнопку в плейлист всплавыет BottomSheet со списком  плейлистов и кнопкой созания нового плейлиста

# Экран медиатеки

Два фрагмента, расположенных на этом экране, отображают:
список треков в избранном,
список созданных плейлистов, в которые можно добавлять понравившиеся треки.

# Экран плейлист
На экране пользователь должен видеть:
 - кнопку «Назад»;
 - область для отображения обложки;
 - название плейлиста;
 - описание плейлиста, если оно есть (это может быть любой текст, заданный при создании плейлиста, — на макете в области для описания отображается «2022»);
 - общую продолжительность всех треков в плейлисте;
 - количество треков, добавленных в плейлист;
 - кнопку «Поделиться»;
 - кнопку «Меню» (три точки);
 - область для отображения списка треков.
   
При нажатии на кнопку «Назад» на экране, на системную кнопку Back или при выполнении соответствующего жеста пользователь должен быть перенаправлен на предыдущий экран — «Медиатека», раздел «Плейлисты».

 Если для текущего плейлиста пользователь выбрал обложку, то в области для отображения обложки пользователь должен видеть соответствующее изображение. Если текущий плейлист не имеет обложки, то в области для обложки должно отображаться изображение-заглушка

# Экран настроек

 - При нажатии на кнопку «Поделиться приложением» появляется диалог для шаринга сообщения в любой из доступных мессенджеров на устройстве. Текст сообщения содержит ссылку на курс по Андроид-разработке в Практикуме.
 - При нажатии на кнопку «Написать в поддержку» должен открыться почтовый клиент. В почтовом клиенте должна быть открыта форма для отправки письма с предзаполненными полями
 - При нажатии на кнопку «Пользовательское соглашение» должен открыться браузер
 - Приложение сохраняет текущую настройку темы (светлая/тёмная) в SharedPreferences.Тема приложения загружается из SharedPreferences при каждом запуске.






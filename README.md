# WolfQuotesJava

Приложение генерирует фразы, которые некоторым людям могут показаться смешными (попытка имитации такого явления как "цитаты волка").   
Идея пришла после посещения сайта https://inspirobot.me.   
Если людям понравится - выложу на плеймаркет.  
Проект целиком и полностью делаю сам (разве что stackoverflow и сайт с документацией периодически выручают).  


Использованные штуки/технологии:  

Java:  
SQLite для хранения истории, избранного и базы слов (работа через rawQuery);  
История и избранное - отдельные Activity, получающие данные из MainActivity (intents все дела);  
Есть пара классов для удобства работы с данными (упаковываю так, чтобы значения слов и вероятность их выпадения вместе лежали и методы для работы с этим);  
Много различных методов от загрузки/выгрузки баз данных, до небольших функций работы со строками (вроде изменения формы прилагательного большой -> большого);  
Простой алгоритм для выбора случайного элемента, если у каждого элемента есть некоторая вероятность выпадения (вес);  
Основная структура, содержащая данные - HashMap, ключ - имя группы слов, значение - группа слов (вроде глаголы, существительные и т.д.).  

Android:  
Много LinearLayots чтобы располагать элементы в зависимости от их веса (ну и space-инги чтобы не слипались в кучу) , textViews, scrollViews, buttons, несколько activity.  
Relative Layout не совсем подошёл, а Constraint пока не довелось применить.  

p.s.  
1) Пробовал в начале писать на Kotlin, но когда нужно было добавить базы данных решил переписать на Java, т.к. не нашёл хорошего туториала.  
2) Первый запуск может занять чуть больше времени, т.к. загружаются слова в базу данных (либо у меня просто телефон старый :)  

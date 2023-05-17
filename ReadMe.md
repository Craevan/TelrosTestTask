[![Codacy Badge](https://app.codacy.com/project/badge/Grade/fc596c1ebea641acae4f7ee57383a01d)](https://app.codacy.com/gh/Craevan/TelrosTestTask/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
![example workflow](https://github.com/Craevan/TelrosTestTask/actions/workflows/tests.yml/badge.svg?event=push)
___

## Тестовое задание

* Реализован REST API с использованием Spring Boot
* Сделано 2 профиля для работы с базами данных:
    * H2 DB in-memory mode - по-умолчанию
    * PostgreSQL (необходимо активировать соответствующий профиль; в файле `application.yaml`:
        * раскомментировать настройки для подключения к БД
        * раскомментировать `sql.init.mode: always`
        * закоментировать настройки H2)
* Выполненно покрытие кода тестами
* Настроен GitHub Actions для авто тестирования

## Тестирование

* [Коллекция запросов для Postman](https://github.com/Craevan/TelrosTestTask/blob/master/Telros.postman_collection.json)
* [Swagger](http://localhost:8080/swagger-ui/index.html)

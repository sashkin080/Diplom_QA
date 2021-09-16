# План автоматизированного тестирования покупки тура в Маракеш

# Документация

 - [План тестирования]( https://github.com/sashkin080/Diplom_QA/blob/master/docs/plan.md)
 - [Отчёт о проведённом тестировании](https://github.com/sashkin080/Diplom_QA/blob/master/docs/Report.md)
 - [Отчёт о проведённой автоматизации](https://github.com/sashkin080/Diplom_QA/blob/master/docs/Summary.md)

## Начало работы

Cклонировать репозиторий вполнив команду git clone https://github.com/sashkin080/Diplom_QA

### Prerequisites

Необходимо установить:

- IntelliJ IDEA
- Git 
- Docker
- Google Chrome

### Установка и запуск 

- Для запуска MySQL
1. Выполнить команду: ``` docker-compose -f docker-compose-mysql.yml up -d ```
2. Выполнить команду: ``` java -jar artifacts/aqa-shop.jar -P:jdbc.url=jdbc:mysql://localhost:3306/app -P:jdbc.user=app -P:jdbc.password=pass ```
3. Выполнить команду: ``` ./gradlew "-Ddb.url=jdbc:mysql://localhost:3306/app" clean test ```
4. Выполнить команду: ``` ./gradlew allureReport ```
5. Выполнить команду: ``` ./gradlew allureServe ```

- Для запуска Postgres
1. Выполнить команду: ``` docker-compose -f docker-compose-postgres.yml up -d ```
2. Выполнить команду: ``` java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar ```
3. Выполнить команду: ``` ./gradlew "-Ddb.url=jdbc:postgresql://localhost:5432/app" clean test ```
4. Выполнить команду: ``` ./gradlew allureReport ```
5. Выполнить команду: ``` ./gradlew allureServe ```

## Лицензия
- IntelliJ IDEA - бесплатное ПО.
- Git - бесплатное ПО.
- Docker - бесплатное ПО.
- Google Chrome - беслатное ПО.

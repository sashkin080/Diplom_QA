- Для запуска MySQL
1. Выполнить команду: docker-compose -f docker-compose-mysql.yml up -d
2. Выполнить команду: java -jar artifacts/aqa-shop.jar -P:jdbc.url=jdbc:mysql://localhost:3306/app -P:jdbc.user=app -P:jdbc.password=pass
3. Выполнить команду: .\gradlew clean test

- Для запуска Postgres
1. Выполнить команду: docker-compose -f docker-compose-postgres.yml up -d
2. Выполнить команду: java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar
3. Выполнить команду: gradlew -Ddb.url=jdbc:postgresql://localhost:5432/app clean test 

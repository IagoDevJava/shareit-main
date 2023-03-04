# java-shareit

## Это репозиторий учебного проекта ShareIt (Java EE, Spring, Hibernate, SQL), с добавленным функционалом для контейнеризации через Docker. Архитектура микросервисная, подключена БД с помощью Hibernate ORM, PostgreSQL. Клиент-серверная структура на основе REST. 

#### Приложение умеет: позволять бронировать вещи, выбирать их для бронирования; регистрироваться как владелец, добавлять сви вещи, принимать или отклонять бронирования.

Приложение написано на Java при помощи Spring и сопустствующих библиотек. Пример кода:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItServer {

	public static void main(String[] args) {
		SpringApplication.run(ShareItServer.class, args);
	}

}
```

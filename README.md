# java-shareit

## Это репозиторий учебного проекта ShareIt, с добавденным функционалом лдя контейниризации через Docker 

#### Используется многомодульный проект, разделение на два микросервиса и подключение БД

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

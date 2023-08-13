package hexlet.code.app;

import com.rollbar.notifier.Rollbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {
    @Autowired
    static Rollbar rollbar;
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
        rollbar.debug("Here is some debug message");
    }
}

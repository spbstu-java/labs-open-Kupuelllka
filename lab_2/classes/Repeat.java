package lab_2.classes;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Значит, что аннотация будет доступна во время выполнения программы (через reflection)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repeat {
    int value();
}

package ar.com.wolox.usecase.util;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface StringValidator {

    Optional<Boolean> validStrings(List<String> values);

    default Optional<Boolean> validate(){
        return Optional.empty();
    }
}

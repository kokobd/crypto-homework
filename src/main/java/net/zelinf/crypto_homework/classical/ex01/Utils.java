package net.zelinf.crypto_homework.classical.ex01;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Utils {

    public static <E> Optional<List<E>> sequenceA(List<Optional<E>> optionals) {
        if (optionals.stream().allMatch(Optional::isPresent)) {
            return Optional.of(optionals.stream().map(Optional::get).collect(Collectors.toList()));
        }
        return Optional.empty();
    }
}

package util;

import java.util.Objects;

public record KeyTriple<K1, K2, K3>(K1 key1, K2 key2, K3 key3) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KeyTriple<?, ?, ?> that)) return false;
        if (this == o) return true;
        return Objects.equals(key1, that.key1) &&
                Objects.equals(key2, that.key2);
    }

}
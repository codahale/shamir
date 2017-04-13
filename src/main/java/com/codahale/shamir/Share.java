package com.codahale.shamir;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A share of a split secret.
 */
public class Share {
    final int id;
    final byte[] value;

    /**
     * Create a new {@link Share}.
     *
     * @param id    the share's unique identifier
     * @param value the share's value
     */
    public Share(int id, byte[] value) {
        this.id = id;
        this.value = Arrays.copyOf(value, value.length);
    }

    /**
     * Returns the share's unique identifier.
     *
     * @return the share's unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the share's value.
     *
     * @return the share's value
     */
    public byte[] getValue() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Share that = (Share) o;
        return this.id == that.id && Arrays.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(value));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("id = " + id)
                .add("value = " + Arrays.toString(value))
                .toString();
    }
}

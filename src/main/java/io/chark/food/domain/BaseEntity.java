package io.chark.food.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

    /**
     * Default length for longer string fields.
     */
    public static final int DEFAULT_LONG_LENGTH = 1024;

    /**
     * Default length for various string values.
     */
    public static final int DEFAULT_LENGTH = 64;

    /**
     * Default min length for various string values.
     */
    public static final int MIN_LENGTH = 4;

    @Id
    @GeneratedValue
    protected long id;

    @JsonView(MinimalView.class)
    public long getId() {
        return id;
    }

    /**
     * Interface for minimal json view details.
     */
    public interface MinimalView {
    }
}
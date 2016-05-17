package io.chark.food.app.administrate.audit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AuditPackage {

    private final List<Long> accountCounts;

    private final long totalRestaurantCount;
    private final long totalAccountCount;

    @JsonCreator
    public AuditPackage(@JsonProperty("accountCounts") List<Long> accountCounts,
                        @JsonProperty("totalRestaurantCount") long totalRestaurantCount,
                        @JsonProperty("totalAccountCount") long totalAccountCount) {

        this.accountCounts = accountCounts;
        this.totalRestaurantCount = totalRestaurantCount;
        this.totalAccountCount = totalAccountCount;
    }

    public List<Long> getAccountCounts() {
        return accountCounts;
    }

    public long getTotalRestaurantCount() {
        return totalRestaurantCount;
    }

    public long getTotalAccountCount() {
        return totalAccountCount;
    }
}
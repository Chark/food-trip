package io.chark.food.app.administrate.audit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class AuditPackage {

    private final Map<Integer, Long> accountCounts;

    private final long totalRestaurantCount;
    private final long totalAccountCount;
    private final long loggedInUserCount;

    @JsonCreator
    public AuditPackage(@JsonProperty("accountCounts") Map<Integer, Long> accountCounts,
                        @JsonProperty("totalRestaurantCount") long totalRestaurantCount,
                        @JsonProperty("totalAccountCount") long totalAccountCount,
                        @JsonProperty("loggedInUserCount") long loggedInUserCount) {

        this.accountCounts = accountCounts;
        this.totalRestaurantCount = totalRestaurantCount;
        this.totalAccountCount = totalAccountCount;
        this.loggedInUserCount = loggedInUserCount;
    }

    public Map<Integer, Long> getAccountCounts() {
        return accountCounts;
    }

    public long getTotalRestaurantCount() {
        return totalRestaurantCount;
    }

    public long getTotalAccountCount() {
        return totalAccountCount;
    }

    public long getLoggedInUserCount() {
        return loggedInUserCount;
    }
}
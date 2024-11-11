package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingStatus {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED;

    public static Optional<BookingStatus> from(String status) {
        for (BookingStatus value : BookingStatus.values()) {
            if (value.name().equals(status)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

}
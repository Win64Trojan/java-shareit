package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemRequest {
    final long   id;
    final String description;
    final long   requestor;
    final LocalDate created;
}
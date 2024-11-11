package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface  BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select (count(b) > 0) from Booking b " +
            "where b.item.id = ?1  and b.status = 'APPROVED' " +
            "and b.start <= ?3  and b.end >= ?2")
    boolean isAvailable(Long itemId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBooker(User booker, Sort sort);

    List<Booking> findByItemOwner(User owner, Sort sort);

    List<Booking> findByBookerAndEndAfterAndStartBefore(User booker, LocalDateTime now, LocalDateTime now1, Sort sort);

    List<Booking> findByItemOwnerAndEndAfterAndStartBefore(User owner, LocalDateTime now, LocalDateTime now1, Sort sort);

    List<Booking> findByBookerAndStartAfter(User booker, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerAndStartAfter(User owner, LocalDateTime now, Sort sort);

    List<Booking> findByBookerAndEndBefore(User booker, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerAndEndBefore(User owner, LocalDateTime now, Sort sort);

    List<Booking> findByBookerAndStatusEquals(User booker, String status, Sort sort);

    List<Booking> findByItemOwnerAndStatusEquals(User owner, String status, Sort sort);

    @Query("select b from Booking b " +
            "where b.item = ?1 and b.status = ('APPROVED')")
    List<Booking> findApprovedForItem(Item savedItem, Sort start);

    Optional<Booking> findByBookerAndItem(User user, Item item);

    @Query("select b from Booking b " +
            "where b.item IN ?1 and b.status = ('APPROVED')")
    List<Booking> findApprovedForItems(List<Item> items, Sort start);
}
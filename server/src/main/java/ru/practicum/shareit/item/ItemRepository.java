package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Получение владельцем списка его вещей в БД
     */
    List<Item> findItemByOwnerId(Long id);

    /**
     * Поиск вещи потенциальным арендатором
     */
    @Query(value = "select * " +
            "from ITEMS i " +
            "where POSITION(:text IN concat(LOWER(NAME), ' ', LOWER(DESCRIPTION))) > 0 " +
            "AND is_available = true", nativeQuery = true)
    List<Item> findItemsByRequest(@Param("text") String text);
}
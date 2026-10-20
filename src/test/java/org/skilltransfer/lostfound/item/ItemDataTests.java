package org.skilltransfer.lostfound.item;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

/** schema.sql / data.sql 이 실행되어 테스트 데이터 100건이 들어갔는지 확인합니다. */
@SpringBootTest
class ItemDataTests {

    @Autowired
    JdbcClient jdbc;

    @Autowired
    ItemController controller;

    @Test
    void seedDataHas100Items() {
        long count = jdbc.sql("SELECT COUNT(*) FROM items").query(Long.class).single();
        assertEquals(100, count);
    }

    @Test
    void controllerFiltersByStatusAndOwner() {
        assertEquals(100, controller.list(null, null, null).size());
        assertEquals(67, controller.list("FOUND", null, null).size());
        assertEquals(34, controller.list("FOUND", 101L, null).size());
        assertEquals("빨간 우산", controller.one(100).getBody().title());
        assertEquals(404, controller.one(999).getStatusCode().value());
    }
}

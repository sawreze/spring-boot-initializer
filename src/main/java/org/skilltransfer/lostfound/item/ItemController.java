package org.skilltransfer.lostfound.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 분실물(items) 조회 API.
 *
 * GET /api/items                         전체 목록 (id 순)
 * GET /api/items?status=FOUND            상태로 거르기 (FOUND / RETURNED)
 * GET /api/items?ownerId=101             등록자로 거르기
 * GET /api/items?location=도서관          발견 장소로 거르기
 * GET /api/items?status=FOUND&ownerId=101 조건 여러 개 함께
 * GET /api/items/105                     한 건 조회 (없으면 404)
 * GET /api/items/stats                   전체/상태별/등록자별 개수
 */
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final JdbcClient jdbc;

    public ItemController(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping
    public List<Item> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String location) {

        StringBuilder sql = new StringBuilder(
                "SELECT id, title, location, status, owner_id, note FROM items WHERE 1 = 1");
        Map<String, Object> params = new HashMap<>();

        // 값은 문자열로 이어 붙이지 않고 :이름 파라미터로 넘깁니다 (SQL 인젝션 방지).
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = :status");
            params.put("status", status.trim().toUpperCase());
        }
        if (ownerId != null) {
            sql.append(" AND owner_id = :ownerId");
            params.put("ownerId", ownerId);
        }
        if (location != null && !location.isBlank()) {
            sql.append(" AND location = :location");
            params.put("location", location.trim());
        }
        sql.append(" ORDER BY id");

        return jdbc.sql(sql.toString())
                .params(params)
                .query(Item.class)
                .list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> one(@PathVariable long id) {
        return jdbc.sql("SELECT id, title, location, status, owner_id, note FROM items WHERE id = :id")
                .param("id", id)
                .query(Item.class)
                .optional()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        long total = jdbc.sql("SELECT COUNT(*) FROM items")
                .query(Long.class)
                .single();

        Map<String, Long> byStatus = new java.util.LinkedHashMap<>();
        jdbc.sql("SELECT status, COUNT(*) AS cnt FROM items GROUP BY status ORDER BY status")
                .query((rs, rowNum) -> Map.entry(rs.getString("status"), rs.getLong("cnt")))
                .list()
                .forEach(e -> byStatus.put(e.getKey(), e.getValue()));

        Map<String, Long> byOwner = new java.util.LinkedHashMap<>();
        jdbc.sql("SELECT owner_id, COUNT(*) AS cnt FROM items GROUP BY owner_id ORDER BY owner_id")
                .query((rs, rowNum) -> Map.entry(String.valueOf(rs.getLong("owner_id")), rs.getLong("cnt")))
                .list()
                .forEach(e -> byOwner.put(e.getKey(), e.getValue()));

        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("total", total);
        result.put("byStatus", byStatus);
        result.put("byOwnerId", byOwner);
        return result;
    }
}

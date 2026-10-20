-- lost-found-server H2 콘솔 조회 연습
-- 서버 실행 후 http://127.0.0.1:8092/h2-console 접속
--   Driver Class: org.h2.Driver
--   JDBC URL    : jdbc:h2:mem:lostfound
--   User Name   : sa / Password: 빈칸
-- 한 문장씩 붙여 넣고 Run 합니다.

-- 1. 전체 개수 (100)
SELECT COUNT(*) AS total FROM items;

-- 2. 앞부분 10건
SELECT id, title, location, status, owner_id, note FROM items ORDER BY id LIMIT 10;

-- 3. 상태별 개수 (FOUND 67 / RETURNED 33)
SELECT status, COUNT(*) AS cnt FROM items GROUP BY status ORDER BY status;

-- 4. 등록자별 개수 (101: 50 / 102: 50)
SELECT owner_id, COUNT(*) AS cnt FROM items GROUP BY owner_id ORDER BY owner_id;

-- 5. 장소별 개수 (많은 순)
SELECT location, COUNT(*) AS cnt FROM items GROUP BY location ORDER BY cnt DESC, location;

-- 6. 등록자 101의 FOUND 물건 (34건)
SELECT id, title, location, status, owner_id FROM items
WHERE owner_id = 101 AND status = 'FOUND' ORDER BY id;

-- 7. 메모(note)가 없는 물건
SELECT id, title FROM items WHERE note IS NULL ORDER BY id;

-- 8. 등록자 이름까지 함께 보기 (JOIN)
SELECT i.id, i.title, i.status, u.username
FROM items i JOIN users u ON u.id = i.owner_id
ORDER BY i.id LIMIT 10;

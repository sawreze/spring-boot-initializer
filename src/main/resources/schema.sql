-- 서버가 시작될 때 자동으로 실행됩니다 (spring.sql.init.mode=always).
-- 4장 실습의 users / items 구조와 같습니다.
-- 메모리 DB라서 서버를 다시 켜면 처음 상태로 돌아갑니다.

DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  username VARCHAR(40) UNIQUE NOT NULL
);

CREATE TABLE items (
  id BIGINT PRIMARY KEY,
  title VARCHAR(80) NOT NULL,
  location VARCHAR(80) NOT NULL,
  status VARCHAR(16) NOT NULL
    CHECK (status = 'FOUND' OR status = 'RETURNED'),
  owner_id BIGINT NOT NULL REFERENCES users(id),
  note VARCHAR(150)
);

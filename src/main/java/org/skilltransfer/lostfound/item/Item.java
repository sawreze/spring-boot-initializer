package org.skilltransfer.lostfound.item;

/**
 * items 테이블의 한 행.
 * ownerId는 이 기록을 등록한 사용자 ID입니다(물건을 잃어버린 실제 주인이 아님).
 */
public record Item(
        Long id,
        String title,
        String location,
        String status,
        Long ownerId,
        String note
) {
}

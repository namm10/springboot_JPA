package com.shop.repository;

//JpaRepository를는 2개의 제네릭 타입을 사용
//첫번째는 엔티티타입 클래스, 두번째는 기본키타입으로 넣어줌
import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

//JpaRepository를 상속받는 ItemRepository를 작성
//Item클래스는 기본키 타입이 Long이므로 Long을 넣어줌
public interface ItemRepository extends JpaRepository<Item, Long> {
}

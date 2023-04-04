package com.shop.repository;

//JpaRepository를는 2개의 제네릭 타입을 사용
//첫번째는 엔티티타입 클래스, 두번째는 기본키타입으로 넣어줌
import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//JpaRepository를 상속받는 ItemRepository를 작성
//Item클래스는 기본키 타입이 Long이므로 Long을 넣어줌
public interface ItemRepository extends JpaRepository<Item, Long> {


    //itemNm(상품명)으로 데이터를 조회하기 위해서 By 뒤에 필드명인 ItemNm을 메소드의 이름에 붙여줌.
    //엔티티명은 생략이 가능하므로 findItemByItemNm 대신에 findByItemNm으로 메소드명을 만들어줌.
    //매개 변수로는 검색할 때 사용할 상품명 변수를 넘겨줍니다.
    List<Item> findByItemNm(String itemNm);

    //상품을 상품명과 상품 상세 설명을 OR 조건을 이용하여 조회하는 쿼리 메소드입니다.
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    //파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드입니다.
    List<Item> findByPriceLessThan(Integer price);

    //내림차순
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
}

package com.shop.repository;

//JpaRepository를는 2개의 제네릭 타입을 사용
//첫번째는 엔티티타입 클래스, 두번째는 기본키타입으로 넣어줌
import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


//QuerydslPredicateExecutor를 이용한 상품조회 import
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


//JpaRepository를 상속받는 ItemRepository를 작성
//Item클래스는 기본키 타입이 Long이므로 Long을 넣어줌
public interface ItemRepository extends JpaRepository<Item, Long>,
//QuerydslPredicateExecutor 인터페이스 상속을 추가
QuerydslPredicateExecutor<Item>{


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

    //@Query 어노테이션 안에 JPQL로 작성한 쿼리문을 넣어줍니다.
    //from 뒤에는 엔티티 클래스로 작성한 Item을 지정해주었고, Item으로부터 데이터를 select하겠다는 것을 의미
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    //파라미터에 @Param 어노테이션을 이용하여 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정해줄 수 있습니다.
    //현재는 itemDetail 변수를 "like % %" 사이에 "itemDetail"로 값이 들어가도록 작성했습니다.
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    //value 안에 네이티브 쿼리문을 작성하고 "nativeQuery=true"를 지정합니다.
    //만약 기존의 데이터베이스에서 사용하던 쿼리를 그대로 사용하애 할 때는 @Query의 nativeQuery속성을 사용하면
    //기존 쿼리를 그대로 활용할 수 있다 하지만 독립적이라는 장점을 잃어버린다.
    //기존에 작성한 통계용 쿼리처럼 복잡한 쿼리를 그대로 사용해야 하는 경우 활용할 수 있다.
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc",
                    nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}

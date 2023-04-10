//테스트
package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import java.util.List;

//JPAQueryFactory를 이용한 상품 조회 import
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.shop.entity.QItem;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

//QuerydslPredicateExecutor를 이용한 상품조회 import
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

//import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

//테스트 코드 실행 시 application.properties에 설정해둔 값보다
//application-test.properties에 같은 설정이 있다면
//더 높은 우선순위를 부여함 
//기존에는 MySQL을 사용하지만 테스트 코드 실행 시에는 H2 데이터베이스를 사용하게됨
@TestPropertySource(locations="classpath:application-test.properties")
class ItemRepositoryTest {

    //영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입합니다.
    @PersistenceContext
    EntityManager em;

    //ItemRepository를 사용하기 위해서 @Autowired 어노테이션을 이용하여 Bean을 주입
    @Autowired
    ItemRepository itemRepository;

    //테스트할 메소드 위에 선언하여 해당 메소드를 테스트 대상으로 지정
    @Test
    //Junit에 추가된 어노테이션으로 테스트 코드 실행 시 @DisplayName에 지정한 테스트명일 노출됨
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    //테스트 코드 실행 시 데이터베이스에 상품 데이터가 없으므로 테스트 데이터 생성을 위해서
    // 10개의 상품을 저장하는 메소드를 작성하여 findByItemNmTest()에서 실행해줍니다.
    public void createItemList(){
        for(int i=1; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100); item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();
        //ItemRepository 인터페이스에 작성했던 findByItemNm 메소드를 호출합니다.
        //파라미터로는 "테스트 상품1"이라는 상품명을 전달하겠습니다
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for(Item item : itemList){
           //조회 결과 얻은 item 객체들을 출력합니다
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest(){
        //기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어주겠습니다.
        this.createItemList();
        List<Item> itemList =
                //상품명이 "테스트상품1" 또는 상품 상세 설명이 "테스트 상품상세설명5"이면
                //해당 상품을 itemList에 할당합니다.
                //테스트코드를 실행하면 조건대로 2개의 상품이 출력되는 것을 볼 수 있습니다.
                itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList();
        //현재 데이터베이스에 저장된 가격은 10001~10010이다.
        //테스트 코드 실행 시 10개의 상품을 저장하는 로그가 콘솔에 나타나고
        //맨마지막에 가격이 10005보다 작은 4개의 상품을 출력해주는 것을 확인할 수 있습니다.
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    //현재출력되는 가격 데이터는 10001부터 10004까지 차례대로 출력됩니다.
    //출력 결과를 OrderBy 키워드를 이용한다면 오름차순 또는 내림차순으로 조회할 수 있습니다.
    //오름차순의 경우 'OrderBy + 속성명 + Asc키워드'를 이용하고,
    //내림차순에서는 'OrderBy + 속성명 + Desc키워드'를 이용해 데이터의 순서를 처리할 수 있습니다.
    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    //테스트 상품 상세 설명을 포함하고 있는 상품데이터 10개가 가격이 높은 순부터 조회
    //@Query 어노테이션을 사용하여 조회한것
    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    //native를 활용한 상품데이터 10개가 가격이 높은 순부터 조회 (쿼리는 그대로 사용함)
    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }


    //JPAQueryFactory를 이용한 상품 조회
    //상품의 가격이 내림차순으로 정렬돼 데이터를 조회
    //JPAQuery에 추가한 판매상태 코드와 상품 상세 설명이 where 조건에 추가됨
    //(콘솔창)고정된 쿼리문이 아닌 비즈니스 로직에 따라서 동적으로 쿼리문을 생생해줄 수 있다
    @Test
    @DisplayName("Querydsl 조회테스트1")
    public void queryDslTest(){
        this.createItemList();
        //JPAQueryFactory를 이용하여 쿼리를 동적으로 생성
        //생성자의 파라미터로는 EntityManager 객체를 넣어줌
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //Querydsl을 통해 쿼리를 생서하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용
        QItem qItem = QItem.item;
        //자바 소스코드지만 SQL문과 비슷하게 소스를 작성할 수 있다.
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        //JPAQuery 메소드중 하나인 fetch를 이용해서 쿼리 결과를 리스트로 반환한다.
        //fetch() 메소드 실행 시점에 쿼리문이 실행된다.
        List<Item> itemList = query.fetch();

        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    //QuerydslPredicateExecutor를 이용한 상품조회
    //상품 데이터를 만드는 새로운 메소드 생성
    //1~5번 상품은 상품의 판매상태를 SELL(판매중)으로 지정하고,
    //6~10번 상품은 판매상태를 SOLD_OUT(품절)으로 세팅해 생성
    public void createItemList2(){
        for(int i=1; i<=5; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for(int i=6; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){

        this.createItemList2();

        //BooleanBuilder는 쿼리에 들어갈 조건을 만들어주는 빌더라고 생각하면됨
        //Predicate를 구현하고 있으며 메소드 체인 형힉으로 사용할 수 있다.
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        //필요한 상품을 조회하는데 필요한 "and" 조건을 추가하고 있다.
        //아래소스에서 상품의 판매상태가 SELL일때만
        // booleanBuilder에 판매상태 조건을 동적으로 추가하는것을 볼 수 있다.
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        //데이터를 페이징해 조회하도록 PageRequest.of() 메소드를 이용해 Pageble 객체를 생성
        //첫번째 인자는 조회할 페이지의 번호, 두번째 인자는 한 페이지당 조회할 데이터의 개수를 넣어줌
        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult =
        //QueryDslPredicateExecutor 인터페이스에서 정의한 findALL() 메소드를 이용해
        //조건에 맞는 데이터를 Page 객체로 받아옴
        itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult. getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }

    }



}
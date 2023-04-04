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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//테스트 코드 실행 시 application.properties에 설정해둔 값보다
//application-test.properties에 같은 설정이 있다면
//더 높은 우선순위를 부여함 
//기존에는 MySQL을 사용하지만 테스트 코드 실행 시에는 H2 데이터베이스를 사용하게됨
@TestPropertySource(locations="classpath:application-test.properties")
class ItemRepositoryTest {

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
}
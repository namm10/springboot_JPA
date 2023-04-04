package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
//(@Table=어노테이션을 통해 어떤 테이블과 매핑될지 지정)item테이블과 매핑되도록 name을 item으로 지정
@Table(name="item")
@Getter
@Setter
@ToString

public class Item {

    //entity로 선언한 클래스는 반드시 기본키를 가져야함
    //기본키가 되는 멤버변수에 @id 어노테이션을 붙여 테이블에 매핑될 컬럼의 이름을 @Column 어노테이션을 통해 설정해줌
    //item클래스의 id변수와 item테이블의 item_id 컬럼이 매핑되도록한다.
    @Id
    @Column(name="item_id")
    //@GeneratedValue 어노테이션을 통해 기본키 생성전략을 AUTO로 지정
    @GeneratedValue(strategy = GenerationType.AUTO)

    //상품코드~상품판매상태까지 상품정보임
    private Long id; //상품코드

    //@Column 어노테이션의 nullable 속성을 이용해서 항상 값이 있어야 하는 필드는 not null 설정을 함
    @Column(nullable = false, length = 50)
    //String 필드는 default 값으로 255가 설정되있음,
    //각 String 필드마다 필요한 길이를 length 속성에 default 값을 세팅
    private String itemNm; //상품명

    @Column(name="price", nullable = false)
    private int price; //가격

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    //재고가 없거나, 상품을 미리 등록해 놓고 나중에 '판매중' 상태로 바꾸거나
    //재고가 없을때는 프론트에 노출시키지 않기 위해서 판매상태를 코드로 갖고있는다.
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    @Column(nullable = false)
    private int stockNumber; //재고수량

    //상품테이블에 기록하기 위해 등록시간과 수정시간을 LocalDateTime으로 선언함
    private LocalDateTime regTime; //등록 시간

    private LocalDateTime updateTime; //수정 시간
}

package hello.springcorereview.order;

import hello.springcorereview.discount.FixDiscountPolicy;
import hello.springcorereview.member.Grade;
import hello.springcorereview.member.Member;
import hello.springcorereview.member.MemoryMemberRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceImplTest {

    /**
     * 생성자 주입을 사용
     * 누락 데이터의 경우 컴파일 오류를 통해 확인할 수 있다.
     * 프레임워크에 의존하지 않고, 순수한 자바 언어의 특징을 살려 필요한 구현체들을 직접 설정해서 테스트 할 수 있다.
     * 기본으로 생성자 주입을 사용하고, 필수 값이 아닌 경우 수정자 주입 옵션을 부여하면 된다. 생성자 주입과 수정자 주입을 동시에 사용할 수 있다.
     * 항상 생성자 주입을 선택하자! 가끔 옵션이 필요하면 수정자 주입을 선택하고, 필드 주입은 사용하지 않는게 좋다.

     * final 키워드 - 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다.
     * 컴파일 오류는 세상에서 가장 빠르고, 좋은 오류다!
     */
    @Test
    void createOrder() {
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "name", Grade.VIP));

        OrderServiceImpl orderService = new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);
        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}
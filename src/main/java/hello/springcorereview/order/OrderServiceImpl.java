package hello.springcorereview.order;

import hello.springcorereview.discount.DiscountPolicy;
import hello.springcorereview.member.Member;
import hello.springcorereview.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    /**
     * 생성자가 딱 1개만 존재하는 경우 Autowired 생략가능(스프링 빈에만 해당), 여러개인 경우는 반드시 특정 생성자를 지정해야 한다.
     * 스프링 컨테이너는 크게 2가지 라이프 사이클 - 스프링빈을 등록하는 단계, 연관관계를 주입하는 단계(@Autowired 를 참조)
     * 생성자 주입의 경우는 스프링빈을 생성하며 의존관계 주입도 같이 일어난다.(new 객체 생성시 생성자가 호출되기 때문)
     * 참고 : @Autowired 의 기본 동작은 주입할 대상이 없으면 오류가 발생, required = false 로 지정하면 주입할 대상이 없어도 동작하게 된다.

     * 생성자 주입 - 생성자 호출 시점에 딱 1번만 호출되는 것이 보장 -> 불변, 필수 의존관계에 사용한다.
     * 수정자(setter) 주입 - 선택, 변경 가능성이 있는 의존관계에 사용한다.
     * 필드 주입 - 필드에 바로 주입하는 방식, 외부에서 변경이 불가능해서 테스트하기 힘든 단점이 있음, DI 프레임 워크가 없으면 아무것도 할 수 없다.
       애플리케이션의 실제 코드와 관계 없는 테스트 코드, 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용하자!
     * 일반 메서드 주입 - 한 번에 여러 필드를 주입 받을 수 있다. 일반적으로 잘 사용하지 않는다.
     * 참고 : 당연하지만!! 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 동작한다.

     * 생성자 주입을 선택해라!!
     * 과거에는 필드 주입을 많이 사용했지만, 최근에는 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장한다.
     * 대부분 의존관계 주입은 한 번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없다. 오히려 종료 전까지 변경하면 안된다.
     * 수정자 주입을 사용하면 public 으로 함수를 열어야 하고 누군가 실수로 변경할 수도 있기 때문에 변경하면 안되는 메서드를 열어두는 것은 좋은 설계가 아니다.
     * 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없어 불변하게 설계할 수 있다.
     */
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    // 인터페이스에 의존하며 DIP 를 지키는 것처럼 보이지만 구현체에도 의존하고 있다 -> DIP 위반
    // 구현체 변경시 클라이언트(OrderServiceImpl)의 코드에 영향을 준다 -> OCP 위반

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}

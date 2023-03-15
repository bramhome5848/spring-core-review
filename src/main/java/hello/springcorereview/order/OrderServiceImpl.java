package hello.springcorereview.order;

import hello.springcorereview.annotation.MainDiscountPolicy;
import hello.springcorereview.discount.DiscountPolicy;
import hello.springcorereview.member.Member;
import hello.springcorereview.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor    // field field 에 대한 생성자를 만들어 준다 (cmd + f12 로 확인가능), 코드도 간결해지고 field 추가도 편하다.
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    /**
     * @Autowired는 타입으로 조회하기 때문에 빈이 2개 이상일 때 문제가 발생할 수 있다.
     * @Autowired는 타입 매칭을 시도하고, 이 때 여러 빈이 존재할 경우 필드 이름(파라미터 이름)으로 빈 이름을 추가 매칭한다.
     * DiscountPolicy 의 하위 타입 FixDiscountPolicy, RateDiscountPolicy -> NoUniqueBeanDefinitionException
     * 하위 타입으로 직접 지정하지 않고 해결할 수 있는 방법
     * 1. @Autowired 필드명 매칭 -> 필드명을 빈 이름으로 변경, 파라미터명을 빈 이름으로 변경(생성자 직접 사용의 경우)
     * 2. @Qualifier -> 구분자를 붙여주는 방식, 주입시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다.
     * @Qualifier("mainDiscountPolicy") 를 못찾으면 -> mainDiscountPolicy 라는 이름의 스프링 빈을 추가로 찾는다.
     * @Bean 직접 등록시에도 @Qualifier 를 동일하게 사용할 수 있다.
     * @Qualifier 는 명확하게 @Qualifier 끼리 사용하는 것이 가장 좋다.
     * 3. @Primary -> 우선순위를 정하는 방법으로 여러 빈 매칭되면 @Primary 가 우선권을 가지도록 함
     * @Qualifier 처럼 주입시에 @Qualifier 를 붙여줄 필요가 없이 편하게 사용 가능
     * 우선 순위
     * @Primary 는 기본값 처럼 동작하는 것, @Qualifier 는 매우 상세하게 동작
     * 스프링은 자동보다는 수동, 넓은 범위보다는 좁은 범위의 선택권이 우선순위가 높다. @Primary < @Qualifier
     */
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
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
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

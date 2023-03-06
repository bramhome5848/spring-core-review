package hello.springcorereview;

import hello.springcorereview.discount.DiscountPolicy;
import hello.springcorereview.discount.RateDiscountPolicy;
import hello.springcorereview.member.MemberService;
import hello.springcorereview.member.MemberServiceImpl;
import hello.springcorereview.member.MemoryMemberRepository;
import hello.springcorereview.order.OrderService;
import hello.springcorereview.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 구현 객체를 생성하고 연결하는 책임을 가지는 별도의 설정 클래스
 * 할인 정책을 변경할 경우에도 AppConfig 만 변경하면 된다
 * 클라이언트 코드 OrderServiceImpl 를 포함해서 사용 영역의 어떤 코드도 변경할 필요 없다
 */
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}

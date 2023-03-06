package hello.springcorereview.order;

import hello.springcorereview.discount.DiscountPolicy;
import hello.springcorereview.member.Member;
import hello.springcorereview.member.MemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

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

package hello.springcorereview.order;

import hello.springcorereview.discount.DiscountPolicy;
import hello.springcorereview.discount.FixDiscountPolicy;
import hello.springcorereview.member.Member;
import hello.springcorereview.member.MemberRepository;
import hello.springcorereview.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}

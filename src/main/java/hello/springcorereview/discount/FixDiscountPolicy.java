package hello.springcorereview.discount;

import hello.springcorereview.member.Grade;
import hello.springcorereview.member.Member;
import org.springframework.stereotype.Component;

@Component
//@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {

    private final int discountFixAmount = 1000;   //1000원 할인

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP) {    //equals 도 == 을 비교하고 있다, == 의 경우 compile 타임에 타입 미스매칭 까지 확인 가능
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}

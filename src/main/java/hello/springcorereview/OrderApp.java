package hello.springcorereview;

import hello.springcorereview.member.Grade;
import hello.springcorereview.member.Member;
import hello.springcorereview.member.MemberService;
import hello.springcorereview.member.MemberServiceImpl;
import hello.springcorereview.order.Order;
import hello.springcorereview.order.OrderService;
import hello.springcorereview.order.OrderServiceImpl;

public class OrderApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order = " + order);
    }
}

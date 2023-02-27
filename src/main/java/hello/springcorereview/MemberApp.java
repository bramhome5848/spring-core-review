package hello.springcorereview;

import hello.springcorereview.member.Grade;
import hello.springcorereview.member.Member;
import hello.springcorereview.member.MemberService;
import hello.springcorereview.member.MemberServiceImpl;

public class MemberApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findById(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }
}

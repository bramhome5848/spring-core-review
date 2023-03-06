package hello.springcorereview.singleton;

import hello.springcorereview.AppConfig;
import hello.springcorereview.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 순수한 DI 컨테이너(AppConfig)는 요청을 할 때 마다 객체를 새로 생성 -> 요청이 올 때마다 객체가 생성 -> 메모리 낭비
 * 객체가 딱 1개만 생성되고, 공유하도록 설계 -> 싱글톤
 */
class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();
        //1.조회 : 호출할 때마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();

        //2.조히 : 호출할 때마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        //참조갑싱 다른것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    public void singletonServiceTest() {
        //1.조회 : 호출할 때마다 같은 객체를 반환.
        SingletonService singletonService1 = SingletonService.getInstance();

        //2.조회 : 호출할 때마다 같은 객체를 반환.
        SingletonService singletonService2 = SingletonService.getInstance();

        //참조값이 같은 것을 확인
        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);

        //singletonService1 == singletonService2
        assertThat(singletonService1).isSameAs(singletonService2);

        //same  -> ==
        //equal -> equals()
        singletonService1.logic();
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        //1. 조회 : 호출할 때마다 같은 객체를 반환
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);

        //2. 조회 : 호출할 때마다 같은 객체를 반환
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        //참조값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //memberService1 == memberService2
        assertThat(memberService1).isSameAs(memberService2);
    }
}

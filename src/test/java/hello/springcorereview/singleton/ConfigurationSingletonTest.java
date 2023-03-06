package hello.springcorereview.singleton;

import hello.springcorereview.AppConfig;
import hello.springcorereview.member.MemberRepository;
import hello.springcorereview.member.MemberServiceImpl;
import hello.springcorereview.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        //모두 같은 인스턴스를 참고하고 있다.
        System.out.println("memberService -> memberRepository = " + memberService.getMemberRepository());
        System.out.println("orderService -> memberRepository = " + orderService.getMemberRepository());
        System.out.println("memberRepository = " + memberRepository);

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    /**
     * class hello.springcorereview.AppConfig$$EnhancerBySpringCGLIB$$f357e792
     * 순수한 클래스였다면 class.hello.springcorereview.AppConfig
     * 스프링이 CGLIB 라는 바이트조작 라이브러리를 사용해 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고, 그 클래스를 스프링빈으로 등록 한 것이다.
     * 만들어진 임의의 클래스가 싱글톤이 보장되도록 해준다.

     * CGLIB 예상
     * if(이미 스프링 컨테이너에 등록되어 있으면) -> 스프링 빈에서 컨테이너에서 찾아서 반환
     * else -> 새로 생성하고 스프링 컨테이너에 등록한 후 반환

     * @Configuration 을 적용하지 않고 @Bean 만 적용할 경우
     * 스프링 빈으로 등록되지만, 싱글톤이 보장되지 않는다.
     */
    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean = " + bean.getClass());

    }
}

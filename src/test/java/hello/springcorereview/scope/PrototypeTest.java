package hello.springcorereview.scope;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 프로토타입 스코프
 * 스프링 컨테이너에 조회하면 스프링 컨테이너는 항상 새로운 인스턴스를 생성해서 반환한다.

 * 1. 프로토타입 스코프의 빈을 스프링 컨테이너에 요청한다.
 * 2. 스프링 컨테이너는 이 시점에 프로토타입 빈을 생성하고, 필요한 의존관계를 주입한다.
 * 3. 스프링 컨테이너는 생성한 프로토타입 빈을 클라이언트에 반환한다.
 * 4. 이후에 스프링 컨테이너에 같은 요청이 오면 항상 새로운 프로토타입 빈을 생성해서 반환한다.

 * 스프링 컨테이너는 프로토 타입을 생성하고, 의존관계 주입, 초기화까지만 처리한다.
 * 4의 과정 이후에 스프링 컨테이너는 생성된 프로토타입 빈을 관리하지 않는다, 빈의 관리 책임이 클라이언트에 있다.
 * 그래서 @PreDestroy 같은 종료 메서드가 호출되지 않는다.

 * 싱글톤 빈은 스프링 컨테이너 생성 시점에 초기화 메서드가 실행되지만, 프로토타입 빈은 스프링 컨테이너에서 조회할 떄 초기화 메서드가 실행된다.
 * 싱글톤 빈은 스프링 컨테이너가 종료될 때 빈의 종료 메서드가 실행되지만, 프로토타입 빈은 종료 메서드가 전혀 실행되지 않는다.

 * 정리
 * 스프링 컨테이너에 요청할 때마다 새로 생성된다.
 * 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입 그리고 초기화까지만 관여한다.
 * 종료메서드가 호출되지 않는다.
 * 프로토타입 빈은 해당 빈을 조회한 클라이언트가 관리해야 한다. 종료 메서드에 대한 호출도 클라이언트가 직접해야 한다.
 */
class PrototypeTest {

    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);

        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);
        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("SingletonBean.destroy");
        }
    }
}

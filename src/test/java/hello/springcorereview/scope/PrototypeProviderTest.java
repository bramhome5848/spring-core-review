package hello.springcorereview.scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 Provider 로 문제 해결
 * 가장 간단한 방법은 싱글톤 빈이 프로토타입을 사용할 때 마다 스프링 컨테이너에 새로 요청하는 것

 * 의존관계를 외부에서 주입 받는게 아니라 직접 필요한 의존관계를 찾는 것을 DL(Dependency Look up) 의존관계 탐색이라 한다.
 * 하지만 이 경우 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워지게 된다.
 * 지정한 프로토 타입 빈을 컨테이너에서 대신 찾아주는 DL 정ㄷ의 기능만 제공하는 무엇인가 있으면 된다.

 * ObjectProvider 지정한 컨테이너 빈을 대신 찾아는 DL 서비스를 제공
 * ObjectProvider.getObject() 를 호출하면 내부에서 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다.(DL)
 * ObjectProvider.getObject() 를 통해서 항상 새로운 프로토타입 빈이 생성되는 것을 확인할 수 있다.

 * 특징
 * ObjectFactory : 기능이 단순, 별도 라이브러리 필요 없음, 스프링에 의존
 * ObjectProvider : ObjectFactory 상속, 옵션, 스트림 처리등 편의 기능이 많고, 별도의 라이브러리 필요 없음, 스프링에 의존

 * 정리
 * 프로토타입 빈을 언제 사용할까? -> 매번 사용할 때 마다 의존관계 주입이 완료된 새로운 객체가 필요하 면 사용하면 된다
 * 실무에서 웹 애플리케이션을 개발해보면, 싱글톤 빈으로 대부분의 문제를 해결할 수 있기 때문에 프로토타입 빈을 직접적으로 사용하는 일은 거의 없다.
 * ObjectProvider , JSR330 Provider 등은 프로토타입 뿐만 아니라 DL이 필요한 경우는 언제든지 사용 할 수 있다.
 */
class PrototypeProviderTest {

    @Test
    void providerTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    static class ClientBean {

        //@Autowired
        //private ApplicationContext ac;

        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            //PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;
        public void addCount() {
            count++;
        }
        public int getCount() {
            return count;
        }
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}

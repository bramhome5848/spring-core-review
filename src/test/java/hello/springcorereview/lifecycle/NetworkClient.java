package hello.springcorereview.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 스프링 빈 라이프 사이클 : 객체 생성 -> 의존관계 주입
 * 스프링 빈은 객체를 생성하고, 의존관계 주입이 끝난 다음에 필요한 데이터를 사용할 수 있는 준비가 완료된다.
 * 초기화 작업은 의존관계 주입이 모두 완료되고 난 다음에 진행해야 하는데 의존관계 주입이 모두 완료된 시점을 어떻게 알 수 있을까?
 * 스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해 초기화 시점을 알려주는 기능을 제공한다.
 * 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 주게 된다.

 * 스프링 빈의 이벤트 라이프 사이클
 * 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존 관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료

 * 초기화 콜백 : 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
 * 소멸전 콜백 : 빈이 소멸되기 직전에 호출

 * 객체의 생성과 초기화를 분리하자!!
 * 생성자는 필수 정보(파라미터)를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가진다.
 * 초기화는 생성된 값들을 활용해서 외부 커넥션을 연결하는등 무거운 동작을 수행한다.
 * 생성자 안에서 무거운 초기화 작업을 함께 하는 것 보다는 객체를 생성하는 부분과 초기화 하는 부분을 나누는 것이 유지보수 관점에서 좋다.
 * 초기화 작업이 내부 값들만 약간 변경하는 정도로 단순한 경우에는 생성자에서 한번에 다 처리하는게 더 나을 수 있음

 * 스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 지원
 * 1. 인터페이스(InitializingBean, DisposableBean)
 * 2. 설정 정보에 초기화 메서드, 종료 메서드 지정
 * 3. @PostConstruct, @PreDestroy
 */
public class NetworkClient /*implements InitializingBean, DisposableBean*/ {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect : " + url);
    }

    public void call(String message) {
        System.out.println("call : " + url + " message = " + message);
    }

    public void disconnect() {
        System.out.println("close : " + url);
    }

    /**
     * 초기화 인터페이스의 단점
     * 스프링 전용 인터페이스로 해당 코드가 전용 인터페이스에 의존한다.
     * 초기화, 소멸 메서드의 이름을 변경할 수 없다.
     * 직접 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
     * 참고 : 초기화 인터페이스를 사용하는 방법은 스프링 초창기에 나온 방법들로 지금은 거의 사용하지 않는다.
     */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("NetworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destroy");
//        disconnect();
//    }

    /**
     * 설정 정보 사용 특징
     * 메서드 이름을 자유롭게 사용 가능하다.
     * 스프링 빈이 스프링 코드에 의존하지 않는다.
     * 코드가 아니라 설정 정보를 사용하기 때문에 코드르 고칠 수 없는 외불 라이브러리에도 초기화, 종료, 메서드를 적용할 수 있다.

     * 종료 메서드 추론(@Bean 으로 등록할 경우 destroyMethod 의 아주 특별한 기능)
     * 라이브러리는 대부분 close , shutdown 이라는 이름의 종료 메서드를 사용한다.
     * @Bean의 destroyMethod 는 기본값이 (inferred) (추론)으로 등록되어 있다.
     * 추론 기능은 close , shutdown 라는 이름의 메서드를 자동으로 호출, 종료 메서드를 추론해서 호출
     * 직접 스프링 빈으로 등록하면 종료 메서드는 따로 적어주지 않아도 잘 동작한다.
     * 추론 기능을 사용하기 싫으면 destroyMethod="" 처럼 빈 공백을 지정하면 된다.
     *
     * @PostConstruct, @PreDestroy 어노테이션 특징
     * 최신 스프링에서 가장 권장하는 방법
     * javax.annotation.PostConstruct -> 스프링에 종속적인 기술이 아니라 자바 표준
     * 스프링이 아닌 다른 컨테이너에서도 동작 가능하다
     * 외부 라이브러리에는 적용할 수 없다.(코드를 직접 고쳐야 하기 때문에) -> 외부 라이브러리를 초기화, 종료해야 한다면 @Bean 의 기능을 사용해야 한다.

     * 정리
     * @PostConstruct, @PreDestroy 어노테이션을 사용하자!
     * 코드를 고칠 수 없는 외부 라이브러리를 초기화, 종료해야 하면 @Bean 의 initMethod , destroyMethod 를 사용
     */
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
}



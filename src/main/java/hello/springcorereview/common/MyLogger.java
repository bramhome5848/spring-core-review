package hello.springcorereview.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * 웹 스코프
 * 웹 환경에서만 동작한다.
 * 프로토타입과 다르게 스프링이 해당 스코프의 종료시점까지 관리한다.(종료 메서드가 호출된다)

 * 웹 스코프의 종류
 * request : HTTP 요청 하나가 들어오고 나갈 때 까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고, 관리된다.
 * session : HTTP session 과 동일한 생명주기를 가지는 스코프
 * application : 서블릿 컨텍스트와 동일한 생명주기를 가진느 스코프
 * websocket : 웹 소켓과 동일한 생명주기를 가지는 스코프

 * request 스코프 예제
 * 로그를 출력하기 위한 MyLogger 클래스
 * request scope 로 HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸된다.
 * 빈이 생성되는 시점에 @PostConstruct 를 사용해서 UUID 를 생성하고 저장해둔다. UUID 를 저장해두면 다른 HTTP 요청과 구분할 수 있다.
 * 빈이 소멸디는 시점에 @PreDestroy 를 사용해서 종료 메시지를 남긴다.
 * requestURL 은 빈이 생성되는 시점에 알 수 없으므로, 외부에서 setter 로 입력 받는다.
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create : " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close : " + this);
    }
}

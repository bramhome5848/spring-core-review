package hello.springcorereview.web;

import hello.springcorereview.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 로거가 잘 동작하는지 확인하는 테스트용 컨트롤러
 * 참고 : requestURL 을 MyLogger 에 저장하는 부분은 컨트롤러보다는 공통 처리가 가능한 스프링 인터셉터나 서블릿 필터 같은 곳에서 활용하는 것이 좋다.

 * 애플리케이션 실행 시점에서 오류 발생
 * myLogger -> Scope 'request' is not active for the current thread
 * 스프링 애플리케이션을 실행하는 시점에 싱글톤 빈은 생성해서 주입이 가능하지만, request scope 빈은 아직 생성되지 않는다.
 * 스프링 컨테이너가 동작을 시작하는 단계에서는 request 요청이 없기 때문에 빈이 존재하지 않는다.
 * 만들어지지 않은 빈을 스프링 컨테이너에게 요청했기 때문에 에러가 발생한다.

 * 첫번째 해결 방법은 ObjectProvider 를 사용
 * ObjectProvider 덕분에 ObjectProvider.getObject() 를 호출하는 시점까지 request scope 빈의 생성을 지연할 수 있다.
 * ObjectProvider.getObject() 를 호출하는 시점에 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상 처리된다.
 * ObjectProvider.getObject() 를 Controller 와 Service 에서 각각 호출해도 같은 HTTP 요청이면 같은 스프링 빈이 반환된다.

 * 두번째 해결 방법은 프록시를 이용하는 방법
 * proxyMode = ScopedProxyMode.TARGET_CLASS
 * 적용대상이 인터페이스가 아닌 클래스면 TARGET_CLASS
 * 적용대상이 인터페이스면 INTERFACES 를 선택

 * 동작정리
 * CGLIB 라이브러리로 내 클래스를 상속받은 프록시 객체를 만들어서 주입한다.
 * 프록시 객체는 실제 요청이 오면 그 때 내부에서 실제 빈을 요청하는 위임 로직이 들어있다.
 * 클라이언트가 myLogger.logic 을 호출하면 프록시 객체의 메서드를 호출한 것이고 프록시 객체는 request scope 의 진짜 myLogger.logic 을 호출한다.
 * 프록시 객체는 원본 클래스를 상속받아 만들어졌기 때문에 해당 객체를 사용하는 클라이언트 입장에서는 원본인지 아닌지 구분할 수 없고, 동일하게 사용할 수 있다.
 * 프록시 객체는 실제 request scope 와는 관계가 없다. 그냥 가짜 객체이고 내부에 단순한 위임 로직만 있고, 싱글톤처럼 동작한다.

 * 특징정리
 * 프록시 객체 덕분에 클라이언트는 싱글톤 빈을 사용하듯 request scope 를 사용할 수 있다.
 * Provider, 프록시의 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 지연처리 한다는 점이다.
 * 단지 어노테이션 설정 변경만으로 원본 객체를 프록시 객체로 대체할 수 있다.(다형성과 DI 컨테이너가 가진 큰 강점)
 */
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
    //private final ObjectProvider<MyLogger> myLoggerProvider;    //DI(dependency lookup) : lazy operation

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        System.out.println("myLogger = " + myLogger.getClass());    //MyLogger$$EnhancerBySpringCGLIB$$3b197b20
        String requestURL = request.getRequestURL().toString();
        //MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}

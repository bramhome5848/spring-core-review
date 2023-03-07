package hello.springcorereview;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * excludeFilters
 * 컴포넌트 스캔 대상에서 @Configuration 이 붙은 설정 정보 제외, 일반적으로 @Configuration 을 제외하지는 않음(기존 예재 때문에 사용)
 * @Configuration 이 컴포넌트 스캔의 대상이 된 이유도 @Configuration 소스코드를 열어보면 @Component 어노테이션이 붙어 있기 때문이다.

 * basePackages : 탐색할 패키지의 시작 위치를 지정한다. 해당 패키지 포함해서 하위 패키지 모두를 탐색
 * basePackageClasses : 지정한 클래스의 패키지를 탐색 시작 위치로 지정
 * 지정 하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된(AutoAppConfig.class)
 * 권장하는 방법 : 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것, 스프링 부트도 이 방법을 기본으로 제공

 * 컴포넌트 기본 대상 : @Component, @Controller, @Service, @Repository, @Configuration -> 모두 @Component 포함
 *
 * 참고 : 어노테이션은 상속관계가 없다. 어노테이션이 특정 어노테이션을 들고 있는 것을 인식할 수 있는 것은 자바 언어가 아닌 스프링이 지원하는 기능이다.
 */
@Configuration
@ComponentScan(
        basePackages = "hello.springcorereview.member",
        excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
public class AutoAppConfig {
}

package hello.springcorereview.scan.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);

        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull();
        assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("beanB", BeanB.class));
    }

    /**
     * FilterType 5가지
     * ANNOTATION : 기본값, 어노테이션을 인식해서 동작
     * ASSIGNABLE_TYPE : 지정한 타잉ㅂ과 자식 타입을 인식해서 동작
     * ASPECTJ : AspectJ 패턴 사용
     * REGEX : 정규 표현식
     * CUSTOM : TypeFilter 이라는 인터페이스를 구현해서 처리
     */
    @Configuration
    @ComponentScan(
            includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
    static class ComponentFilterAppConfig {
    }

    /**
     * 중복 등록과 충돌(빈 이름이 같은 경우)
     * 자동 빈 등록 vs 자동 빈 등록 -> 컴포넌스 스캔에 의해 자동으로 빈이 등록 되는데 이름이 같은 경우 ConflictingBeanDefinitionException 발생
     * 수동 빈 등록 vs 자동 빈 등록 -> 수동 빈이 우선권을 가지고 수동 빈이 자동 빙을 오버라이딩 해버린다.
     * spring.main.allow-bean-definition-overriding=true
     * 참고 : 최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 기본 값이 false 로 바뀌었다.
     */
}

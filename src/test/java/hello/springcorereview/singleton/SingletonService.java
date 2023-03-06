package hello.springcorereview.singleton;

/**
 * static 영역에 객체 instance 를 미리 하나 생성해서 올려두고 오직 getInstance() 메서드를 통해서만 조회할 수 있도록 한다.
 * 딱 1개의 객체 인스턴스만 존재해야 하므로, 생성자를 private 으로 막아서 외부에서 new 키워드로 객체가 생성되는 것을 막는다.

 * 싱글톤 패턴의 문제점
 * 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
 * 의존관계상 클라이언트가 구체 클래스에 의존한다.(구체클래스.getInstance()) -> DIP 를 위반
 * 클라이언트가 구체 클라스에 의존해서 OCP 원칙을 위반할 가능성이 높다.
 * 테스트하기 어렵다.
 * 내부속성을 변경하거나 초기화 하기 어렵다.
 * private 생성자로 자식 크래스를 만들기 어렵다.
 * 유연성이 떨어진다.
 * 안티패턴으로 불리기도 한다.
 */
public class SingletonService {

    //1. static 영역에 객체를 1개만 생성해둔다.
    private static final SingletonService instance = new SingletonService();

    //2. public 으로 열어서 객체 인스턴스가 필요하면 이 static 메서드를 통해서만 조회하도록 허용한다.
    public static SingletonService getInstance() {
        return instance;
    }

    //3. 생성자를 private 으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 못하게 막는다.
    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}

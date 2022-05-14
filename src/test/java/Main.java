public class Main {

    public enum T {


        A() {
            @Override
            public void f() {
                System.out.println(1);
            }
        };
        public abstract void f();
    }

    public static void main(String[] args) {
        System.out.println(T.A.name());
    }
}

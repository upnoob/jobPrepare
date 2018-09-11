import java.util.Scanner;

/**
 * Created by lx on 2018/9/9 0009.
 */
public class Main {

    public static void main(String[] args) {
        String s = "true";
        System.out.println(s.equals(true));

        T t = new T();
        S s1 = new S();
        if (t instanceof Persion) {
            s1 = (S)t;
        }


        try{
            f();
            return;
        }finally {
            System.out.println("C");
        }
    }

    public static void f(){
        System.out.println("A");
    }



}

class Persion {

}


class T extends Persion{}
class S extends Persion{}

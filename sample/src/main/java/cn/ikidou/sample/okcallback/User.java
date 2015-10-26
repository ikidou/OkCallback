package cn.ikidou.sample.okcallback;

/**
 * Created by ikidou on 15-10-12.
 */
public class User {
    public String name;
    public int age;
    public Sex sex;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}

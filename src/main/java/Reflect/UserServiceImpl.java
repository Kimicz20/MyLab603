package Reflect;

/**
 * Created by geek on 2017/8/29.
 */
public class UserServiceImpl implements UserService {
    @Override
    public String getName(int id) {
        System.out.println("------getName------");
        return "Tom";
    }
}

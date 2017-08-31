package DS;

/**
 * Created by geek on 2017/8/22.
 */
public class SkipListNode<T> {
    public T key;
    public int level;
    public SkipListNode<T>[] next;

    public SkipListNode(T key, int level) {
        this.key = key;
        this.level = level;
    }
}

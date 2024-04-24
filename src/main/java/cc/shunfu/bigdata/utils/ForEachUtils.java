package cc.shunfu.bigdata.utils;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 获取下标
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-18
 */

public class ForEachUtils {


    /**
    * 
    * @param startIndex 开始索引
	* @param elements 几个
	* @param action 操作
    * @author svanrj
    * @date 2024/4/18
    */
    public static <T> void forEach(int startIndex,Iterable<? extends T> elements, BiConsumer<Integer, ? super T> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);
        if(startIndex < 0) {
            startIndex = 0;
        }
        int index = 0;
        for (T element : elements) {
            index++;
            if(index <= startIndex) {
                continue;
            }
            action.accept(index-1, element);
        }
    }
}

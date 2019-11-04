package service.middle;

import java.util.List;

/**
 * @author create by Xiao Han 10/22/19
 * @version 1.0
 * @since jdk 1.8
 */
public interface IQueueService<T> {

  void enqueue(T t);

  List<T> dequeue(int num);

}

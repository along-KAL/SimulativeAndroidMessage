package client;

import android.message.*;
import android.message.Handler.Callback;

public class test {

	static Handler handler;

	public static void main(String[] args) throws Exception {

		// 创建当前线程的Looper,和 messageQueue
		Looper.prepare();

		/**
		 * 以下模拟各种使用方法
		 * 
		 * 都需要handler，并在Handler的构造方法获取当前线程的looper，从而获取messageQueue
		 *
		 */

		// 派生Handler子类的用法
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// System.out.println("message==="+msg);
					System.out.println(Thread.currentThread());
				}
			}
		};

		// 在子线程中发送消息
		new Thread() {
			@Override
			public void run() {
				super.run();
				System.out.println(Thread.currentThread());
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}.start();

		// 重写Runnable run()的用法
		// 里面会把Runnable封装成Message, 这不是开启新线程，不知道为什么使用Runnable，感觉用别的接口也行
		handler.post(new Runnable() {
			@Override
			public void run() {
				// System.out.println("message===run");
			}
		});

		// 开启循环，取出消息
		Looper.loop();

		/**
		 * 小结： 一个线程存数据，另一个线程取数据，中间的媒介就是Message， 这样就实现了线程间的通行，
		 * 	
		 * 使用Message有解耦的作用，使得两个线程依赖不是那密切，两个对象之间通过第三方对象(一般就是抽象)产生依赖关系，
		 * 这也是遵循设计模式的依赖倒置原则
		 * 
		 * 还有一个关键的东西就是ThreadLocal，使用ThreadLocal保存Looper，使用当前线程作为键，保证了每个线程都有自己Looper，不受其他线程影响。
		 * 
		 * 有个疑问？ 在Android中，loop是个死循环，主线程为啥不会卡死？具体的需要理解Linux epoll，管道模型了
		 */

	}
}

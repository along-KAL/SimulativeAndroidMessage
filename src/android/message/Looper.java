package android.message;

public class Looper {

	/**
	 * 使用ThreadLocal来存储looper，保证每一个线程有独自的looper
	 * 为什么使用ThreadLocal，如果不使用，要保证每个线程都有独自的looper，
	 * 可能需要一个全局LooperManager来管理Looper,也是需要使用map的方式存储looper，
	 * 使用当前线程作为key,looper作为value，但这一切threadLocal都实现好了。
	 */
	static final ThreadLocal<Looper> mThreadLocal = new ThreadLocal<Looper>();

	public MessageQueue mQueue;

	/**
	 * 在构造函数中初始化消息队列
	 */
	public Looper() {
		mQueue = new MessageQueue();
	}

	/**
	 * 创建Looper并添加到ThreadLocal
	 */
	public static void prepare() {
		// 添加Looper
		mThreadLocal.set(new Looper());
	}

	// 从ThreadLocal获取Looper
	public static Looper myLooper() {
		return mThreadLocal.get();
	}

	// 循环取出消息
	public static void loop() {
		// 获取Looper
		Looper tempLooper = myLooper();
		// 获取MessageQueue
		MessageQueue tempMQueue = tempLooper.mQueue;
		for (;;) {
			// 取出消息
			Message msg = tempMQueue.next();
			// 唯一跳出循环的条件
			if (msg == null) {
				return;
			}
			// 调用Handle的dispatchMessage
			msg.target.dispatchMessage(msg);
		}
	}
	
	/**
	 * 退出loop();通过设置MessageQueue中的mQuitting来退出MessageQueue中的next()，从而退出loop()
	 */
	public void quit() {
		mQueue.setMQuitting(true);
	}
}

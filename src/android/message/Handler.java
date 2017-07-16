package android.message;

public class Handler {

	/**
	 * @author kongalong
	 * 
	 *         这个接口的意义是:起到一个拦截msg的作用，根据他的handleMessage()
	 *         返回值来决定是否调用Handler的handleMessage()
	 *         并且给用户提供实现接口的方法，来处理消息，而不用派生Handler的子类
	 */
	public interface Callback {
		public boolean handleMessage(Message msg);
	}

	public Callback mCallback;

	//持有一个线程中唯一一个Looper引用
	public Looper mLooper;
	//消息队列
	public MessageQueue mQueue;

	/**
	 * 构造方法： 初始化looper，通过Looper.myLooper()获取Looper引用
	 * ，这种方式获取的Looper是当前线程唯一的Looper，通过ThreadLocal实现
	 * 
	 * 通过looper初始化MessageQueue
	 * @throws Exception 
	 * 
	 */
	public Handler() throws Exception {
		// 通过Looper静态方法获取looper 也就是通过ThreadLocal获取当前线程的looper
		mLooper = Looper.myLooper();
		if (mLooper == null) {
			// 这里抛异常，不能在没有创建looper的线程中创建Handler用
			// 没有looper，handler也就没有存在的意义
			throw new Exception("不能再没有Looper的线程中创建Handler");
		}
		// 获取looper中的MessageQueue
		mQueue = mLooper.mQueue;
	}

	// 这个用户传过来的looper不能为空
	public Handler(Looper looper) throws Exception {
		mLooper = looper;
		if(mLooper == null){
			throw new Exception("不能再没有Looper的线程中创建Handler");

		}
		// 获取looper中的MessageQueue
		mQueue = mLooper.mQueue;
	}

	public Handler(Callback callback) throws Exception {
		// 调用无参构造方法获取looper
		this();
		mCallback = callback;
	}

	/**
	 *
	 *
	 * 先判断Message中的callback也就是Runnable是否为空，不为空就调用用户实现的run方法，就结束
	 * 否则判断当前Handler的mCallback是否为空，不为空就调用用户实现Callback中的handleMessage()方法
	 * mCallback.handleMessage()有返回值，true，直接结束，false，还会调用handler的handleMessage()
	 * 这些调用的方法都是回调用户实现的方法才有意义
	 * 
	 */
	public void dispatchMessage(Message msg) {
		if (msg.callback != null) {
			handleCallback(msg);
		} else {
			if (mCallback != null) {
				if (mCallback.handleMessage(msg)) {
					return;
				}
			}
			handleMessage(msg);
		}
	}

	// 调用用户实现的Runnable的run方法
	private void handleCallback(Message msg) {
		msg.callback.run();
	}

	// 用户需要实现
	public void handleMessage(Message msg) {
	}

	/**
	 * 向MessageQueue发送消息
	 * 
	 * 原版是里面调用了sendMessageDelayed(Message msg, long delayMillis)有时间参数的，
	 * 可以定时发送消息。为了简单点，这里就没有加入这个参数
	 * 
	 * 为了简化，直接 调用MessageQueue的enqueueMessage() 向里MessageQueue插入消息
	 * 
	 */
	public void sendMessage(Message msg) {
		MessageQueue queue = mQueue;
		// 把当前Handler赋给Message的target, 
		msg.target = this;
		//把消息插入消息队列
		queue.enqueueMessage(msg);
	}

	/**
	 * 原版是调用Message的obtain()生成一个消息，Message中有一个Message sPool成员，作用是缓存Message的意思，
	 * 如果sPool不为空就把sPool作为消息返回，否则直接new Message(); 目的是获取一个Message对象
	 *
	 * 这里为了简单，就直接new Message()了，
	 * 并且把Runnable赋值给Message的callback，这个Runnable就是用户实现Runnable的对象
	 *
	 * 最后还是调用sendMessage();
	 * 
	 */
	public void post(Runnable r) {
		Message msg = new Message();
		msg.callback = r;
		sendMessage(msg);
	}
}

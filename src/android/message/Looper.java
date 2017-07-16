package android.message;

public class Looper {

	/**
	 * ʹ��ThreadLocal���洢looper����֤ÿһ���߳��ж��Ե�looper
	 * Ϊʲôʹ��ThreadLocal�������ʹ�ã�Ҫ��֤ÿ���̶߳��ж��Ե�looper��
	 * ������Ҫһ��ȫ��LooperManager������Looper,Ҳ����Ҫʹ��map�ķ�ʽ�洢looper��
	 * ʹ�õ�ǰ�߳���Ϊkey,looper��Ϊvalue������һ��threadLocal��ʵ�ֺ��ˡ�
	 */
	static final ThreadLocal<Looper> mThreadLocal = new ThreadLocal<Looper>();

	public MessageQueue mQueue;

	/**
	 * �ڹ��캯���г�ʼ����Ϣ����
	 */
	public Looper() {
		mQueue = new MessageQueue();
	}

	/**
	 * ����Looper����ӵ�ThreadLocal
	 */
	public static void prepare() {
		// ���Looper
		mThreadLocal.set(new Looper());
	}

	// ��ThreadLocal��ȡLooper
	public static Looper myLooper() {
		return mThreadLocal.get();
	}

	// ѭ��ȡ����Ϣ
	public static void loop() {
		// ��ȡLooper
		Looper tempLooper = myLooper();
		// ��ȡMessageQueue
		MessageQueue tempMQueue = tempLooper.mQueue;
		for (;;) {
			// ȡ����Ϣ
			Message msg = tempMQueue.next();
			// Ψһ����ѭ��������
			if (msg == null) {
				return;
			}
			// ����Handle��dispatchMessage
			msg.target.dispatchMessage(msg);
		}
	}
	
	/**
	 * �˳�loop();ͨ������MessageQueue�е�mQuitting���˳�MessageQueue�е�next()���Ӷ��˳�loop()
	 */
	public void quit() {
		mQueue.setMQuitting(true);
	}
}

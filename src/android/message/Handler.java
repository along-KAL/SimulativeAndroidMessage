package android.message;

public class Handler {

	/**
	 * @author kongalong
	 * 
	 *         ����ӿڵ�������:��һ������msg�����ã���������handleMessage()
	 *         ����ֵ�������Ƿ����Handler��handleMessage()
	 *         ���Ҹ��û��ṩʵ�ֽӿڵķ�������������Ϣ������������Handler������
	 */
	public interface Callback {
		public boolean handleMessage(Message msg);
	}

	public Callback mCallback;

	//����һ���߳���Ψһһ��Looper����
	public Looper mLooper;
	//��Ϣ����
	public MessageQueue mQueue;

	/**
	 * ���췽���� ��ʼ��looper��ͨ��Looper.myLooper()��ȡLooper����
	 * �����ַ�ʽ��ȡ��Looper�ǵ�ǰ�߳�Ψһ��Looper��ͨ��ThreadLocalʵ��
	 * 
	 * ͨ��looper��ʼ��MessageQueue
	 * @throws Exception 
	 * 
	 */
	public Handler() throws Exception {
		// ͨ��Looper��̬������ȡlooper Ҳ����ͨ��ThreadLocal��ȡ��ǰ�̵߳�looper
		mLooper = Looper.myLooper();
		if (mLooper == null) {
			// �������쳣��������û�д���looper���߳��д���Handler��
			// û��looper��handlerҲ��û�д��ڵ�����
			throw new Exception("������û��Looper���߳��д���Handler");
		}
		// ��ȡlooper�е�MessageQueue
		mQueue = mLooper.mQueue;
	}

	// ����û���������looper����Ϊ��
	public Handler(Looper looper) throws Exception {
		mLooper = looper;
		if(mLooper == null){
			throw new Exception("������û��Looper���߳��д���Handler");

		}
		// ��ȡlooper�е�MessageQueue
		mQueue = mLooper.mQueue;
	}

	public Handler(Callback callback) throws Exception {
		// �����޲ι��췽����ȡlooper
		this();
		mCallback = callback;
	}

	/**
	 *
	 *
	 * ���ж�Message�е�callbackҲ����Runnable�Ƿ�Ϊ�գ���Ϊ�վ͵����û�ʵ�ֵ�run�������ͽ���
	 * �����жϵ�ǰHandler��mCallback�Ƿ�Ϊ�գ���Ϊ�վ͵����û�ʵ��Callback�е�handleMessage()����
	 * mCallback.handleMessage()�з���ֵ��true��ֱ�ӽ�����false���������handler��handleMessage()
	 * ��Щ���õķ������ǻص��û�ʵ�ֵķ�����������
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

	// �����û�ʵ�ֵ�Runnable��run����
	private void handleCallback(Message msg) {
		msg.callback.run();
	}

	// �û���Ҫʵ��
	public void handleMessage(Message msg) {
	}

	/**
	 * ��MessageQueue������Ϣ
	 * 
	 * ԭ�������������sendMessageDelayed(Message msg, long delayMillis)��ʱ������ģ�
	 * ���Զ�ʱ������Ϣ��Ϊ�˼򵥵㣬�����û�м����������
	 * 
	 * Ϊ�˼򻯣�ֱ�� ����MessageQueue��enqueueMessage() ����MessageQueue������Ϣ
	 * 
	 */
	public void sendMessage(Message msg) {
		MessageQueue queue = mQueue;
		// �ѵ�ǰHandler����Message��target, 
		msg.target = this;
		//����Ϣ������Ϣ����
		queue.enqueueMessage(msg);
	}

	/**
	 * ԭ���ǵ���Message��obtain()����һ����Ϣ��Message����һ��Message sPool��Ա�������ǻ���Message����˼��
	 * ���sPool��Ϊ�վͰ�sPool��Ϊ��Ϣ���أ�����ֱ��new Message(); Ŀ���ǻ�ȡһ��Message����
	 *
	 * ����Ϊ�˼򵥣���ֱ��new Message()�ˣ�
	 * ���Ұ�Runnable��ֵ��Message��callback�����Runnable�����û�ʵ��Runnable�Ķ���
	 *
	 * ����ǵ���sendMessage();
	 * 
	 */
	public void post(Runnable r) {
		Message msg = new Message();
		msg.callback = r;
		sendMessage(msg);
	}
}

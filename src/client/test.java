package client;

import android.message.*;
import android.message.Handler.Callback;

public class test {

	static Handler handler;

	public static void main(String[] args) throws Exception {

		// ������ǰ�̵߳�Looper,�� messageQueue
		Looper.prepare();

		/**
		 * ����ģ�����ʹ�÷���
		 * 
		 * ����Ҫhandler������Handler�Ĺ��췽����ȡ��ǰ�̵߳�looper���Ӷ���ȡmessageQueue
		 *
		 */

		// ����Handler������÷�
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// System.out.println("message==="+msg);
					System.out.println(Thread.currentThread());
				}
			}
		};

		// �����߳��з�����Ϣ
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

		// ��дRunnable run()���÷�
		// ������Runnable��װ��Message, �ⲻ�ǿ������̣߳���֪��Ϊʲôʹ��Runnable���о��ñ�Ľӿ�Ҳ��
		handler.post(new Runnable() {
			@Override
			public void run() {
				// System.out.println("message===run");
			}
		});

		// ����ѭ����ȡ����Ϣ
		Looper.loop();

		/**
		 * С�᣺ һ���̴߳����ݣ���һ���߳�ȡ���ݣ��м��ý�����Message�� ������ʵ�����̼߳��ͨ�У�
		 * 	
		 * ʹ��Message�н�������ã�ʹ�������߳��������������У���������֮��ͨ������������(һ����ǳ���)����������ϵ��
		 * ��Ҳ����ѭ���ģʽ����������ԭ��
		 * 
		 * ����һ���ؼ��Ķ�������ThreadLocal��ʹ��ThreadLocal����Looper��ʹ�õ�ǰ�߳���Ϊ������֤��ÿ���̶߳����Լ�Looper�����������߳�Ӱ�졣
		 * 
		 * �и����ʣ� ��Android�У�loop�Ǹ���ѭ�������߳�Ϊɶ���Ῠ�����������Ҫ���Linux epoll���ܵ�ģ����
		 */

	}
}

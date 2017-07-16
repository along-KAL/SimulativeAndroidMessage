package android.message;

public class MessageQueue {

	// �˳�next()�ı�־
	private boolean mQuitting;

	public void setMQuitting(boolean mQuitting) {
		this.mQuitting = mQuitting;
	}

	
	 //�൱��ͷָ�� �����￪ʼ����Ѱ�ҺͲ�����Ϣ
	Message mFirstMessage;


	/**
	 * ������Ϣ ���迴��������߼���ֻ�����������Message����Handler��sendMessage()�е���
	 */
	public void enqueueMessage(Message msg) {
		Message tempMessage = mFirstMessage;
		
		//û��ʵ�ֶ��е��Ƚ��ȳ���û��ͬ��
		//��һ����Ϣ����
		if (tempMessage == null) {
			mFirstMessage = msg;
			msg.next = null;
		} else {
			// ������Ϣ
			Message prevMsg;
			for (;;) {
				prevMsg = tempMessage;
				tempMessage = tempMessage.next;
				// ����������β��
				if (tempMessage == null) {
					break;
				}
			}
			// �µ���Ϣ����β��
			prevMsg.next = msg;
			msg.next = null;
		}
	}

	/**
	 *  ȡ����Ϣ ��ֻ��ѭ��������ȡ��Message����Looper��loop�е���
	 * @return
	 */
	Message next() {

		/**
		 * û��ʵ�ֶ��е��Ƚ��ȳ���û��ͬ��, ֻ����ʾһ�£�ûʲôӰ�졣��
		 */

		for (;;) {
			Message tempMessage = mFirstMessage;
			if (tempMessage != null) {
				mFirstMessage = tempMessage.next;
				return tempMessage;
			}

			// �˳���ѭ����Ψһ��������ͨ��looper����
			if (mQuitting) {
				return null;
			}
		}
	}
}

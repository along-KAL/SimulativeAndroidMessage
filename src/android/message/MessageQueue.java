package android.message;

public class MessageQueue {

	// 退出next()的标志
	private boolean mQuitting;

	public void setMQuitting(boolean mQuitting) {
		this.mQuitting = mQuitting;
	}

	
	 //相当于头指针 从这里开始遍历寻找和插入消息
	Message mFirstMessage;


	/**
	 * 插入消息 不需看懂里面的逻辑，只是向链表插入Message，在Handler的sendMessage()中调用
	 */
	public void enqueueMessage(Message msg) {
		Message tempMessage = mFirstMessage;
		
		//没有实现队列的先进先出，没有同步
		//第一个消息来了
		if (tempMessage == null) {
			mFirstMessage = msg;
			msg.next = null;
		} else {
			// 插入消息
			Message prevMsg;
			for (;;) {
				prevMsg = tempMessage;
				tempMessage = tempMessage.next;
				// 遍历到链表尾部
				if (tempMessage == null) {
					break;
				}
			}
			// 新的消息接在尾部
			prevMsg.next = msg;
			msg.next = null;
		}
	}

	/**
	 *  取出消息 ，只是循环从链表取出Message，在Looper的loop中调用
	 * @return
	 */
	Message next() {

		/**
		 * 没有实现队列的先进先出，没有同步, 只是演示一下，没什么影响。。
		 */

		for (;;) {
			Message tempMessage = mFirstMessage;
			if (tempMessage != null) {
				mFirstMessage = tempMessage.next;
				return tempMessage;
			}

			// 退出死循环的唯一条件，，通过looper设置
			if (mQuitting) {
				return null;
			}
		}
	}
}

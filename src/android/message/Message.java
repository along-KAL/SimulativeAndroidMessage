package android.message;

public class Message {

	/**
	 * 定义消息携带的数据
	 */
	public Message next;

	public Handler target;

	public int what;

	public Object obj;

	Runnable callback;

}

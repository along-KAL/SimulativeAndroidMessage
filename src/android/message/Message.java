package android.message;

public class Message {

	/**
	 * ������ϢЯ��������
	 */
	public Message next;

	public Handler target;

	public int what;

	public Object obj;

	Runnable callback;

}

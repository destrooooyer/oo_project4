package oo;

/**
 * Created by DESTRooooYER on 2016/4/2.
 */
public class req_que_3 extends req_que
{
	protected int front;
	protected int rear;
	protected request_3[] rq = new request_3[10000];

	public req_que_3()
	{
		this.front = 0;
		this.rear = 0;
		for (int i = 0; i < 10000; i++)
			this.bo[i] = true;
	}
	public request_3 getRq(int x)
	{
		return this.rq[x];
	}

	synchronized public void push_back(request_3 req)
	{
		this.rq[rear++] = req;
		if (rear >= 10000)
			exph.err(4);
	}

	synchronized public int is_empty()
	{
		if (front < rear)
			return 0;
		else return 1;
	}

	synchronized public request_3 pop_front()
	{
		if (front < rear)
			return this.rq[front++];
		else
			return null;
	}
}

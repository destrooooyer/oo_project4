package oo;

import java.util.Date;

/**
 * Created by DESTRooooYER on 2016/4/2.
 */
public class DISP_3 extends DISP implements Runnable
{
	private req_que_3 req;
	private lift_3[] __lift = new lift_3[10];
	private int godie_flag;
	private Object godie_flag_lock;

	public DISP_3()
	{
		this.req = new req_que_3();
		this.godie_flag = 0;
		this.godie_flag_lock = new Object();
	}

	public void push_request(request_3 req)
	{
		this.req.push_back(req);
	}

	public int add_lifts(lift_3 lift[])
	{

		for (int i = 0; i < 3; i++)
		{
			this.__lift[i] = lift[i];
		}
		return 1;
	}

	@Override
	public void godie()
	{
		this.godie_flag = 1;
	}

	@Override
	public void run()
	{
		while (true)
		{
			if (this.godie_flag == 1 && this.req.is_empty() == 1)
				break;
//			System.out.print(this.godie_flag+"      ");
//			System.out.println(this.req.front+"      "+this.req.rear);

			for (int i = this.req.front; i < this.req.rear; i++)
			{
				if(this.req.getBo(i)==false)
					continue;
				request_3 rq_temp = this.req.getRq(i);
				//ER request
				if (rq_temp.type == -1)
				{
					if (__lift[rq_temp.getLift_n()].pick_up(rq_temp) == 1 || __lift[rq_temp.getLift_n()].dt == 0)
					{
						Date now=new Date();
						rq_temp.start_time=now.getTime();
						__lift[rq_temp.getLift_n()].push_req(rq_temp);
						__lift[rq_temp.getLift_n()].update_status();
						this.req.setBo(i);
						System.out.println("lift chosen:	" + rq_temp.getLift_n());
					}
					continue;
				}

				//else
				//decide the lift to fullfill the request
				int lift_flag = 0;    //the lift selected
				int decided = 0;

				for (int j = 1; j < 3; j++)
				{
					if (__lift[j].pick_up(rq_temp) == 1 && __lift[lift_flag].pick_up(rq_temp) == 1)    //both of the lifts can pick up
					{
						if (__lift[j].getS() < __lift[lift_flag].getS())    //compare their s
							lift_flag = j;    //change the lift selected
						decided = 1;
					}
					else if (__lift[j].pick_up(rq_temp) == 0 && __lift[lift_flag].pick_up(rq_temp) == 1)    //the previous one can but the current one can't
					{
						decided = 1;
					}
					else if (__lift[j].pick_up(rq_temp) == 1 && __lift[lift_flag].pick_up(rq_temp) == 0)    //the previous one can't but the current one can
					{
						lift_flag = j;    //change the lift selected
						decided = 1;
					}
					else    //neither of the lifts can pick up
					{
						if (__lift[j].dt == 0 && __lift[lift_flag].dt == 0)    //both are idle
						{
							if (__lift[j].getS() < __lift[lift_flag].getS())    //compare their s
								lift_flag = j;    //change the lift selected
							decided = 1;
						}
						else if (__lift[j].dt == 0 && __lift[lift_flag].dt != 0)    //__lift[j] is idle
						{
							lift_flag = j;
							decided = 1;
						}
						else if (__lift[j].dt != 0 && __lift[lift_flag].dt == 0)    //__lift[lift_flag] is idle
						{
							decided = 1;
						}
						else
						{
							//do nothing
						}
					}

				}
				if (decided == 1)
				{
					Date now=new Date();
					rq_temp.start_time=now.getTime();
					__lift[lift_flag].push_req(rq_temp);
					__lift[lift_flag].update_status();
					this.req.setBo(i);
					System.out.println("lift chosen:	" + lift_flag);
					//System.out.println(this.req.front+"	"+i+"	"+this.req.rear);
				}

			}
			while (this.req.getBo(this.req.front) == false && this.req.front < this.req.rear)
				this.req.pop_front();

			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		}

		for (int i = 0; i < 3; i++)
			__lift[i].godie();
	}
}

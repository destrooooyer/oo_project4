package oo;

import java.util.Date;

/**
 * Created by DESTRooooYER on 2016/4/2.
 */
public class lift_3 extends lift implements Runnable, _lift
{
	private int lift_number;
	private long time0;
	private req_que_3 req_running;
	private req_que_3 req_q;
	private int n;
	private int s;
	private int godie_flag;


	public lift_3(int lift_number)
	{
		this.req_q = new req_que_3();
		this.req_running = new req_que_3();
		this.lift_number = lift_number;
		this.time0 = 0;
		this.fl.setN(1);
		this.dt = 0;
		this.n = 1;
		this.godie_flag = 0;
	}

	public void push_req(request_3 req)
	{
		this.req_q.push_back(req);
	}

	/**
	 * 判断是否捎带
	 *
	 * @param req
	 * @return
	 */
	public int pick_up(request_3 req)
	{
		if (this.dt == 0)
			return 0;

		if (this.dt == 21)
		{
			if (req.getN() == this.n)
			{
				if (req.type == 1 && req.dt == -1)
					return 0;

				return 1;
			}
			else
				return 0;
		}

		if (this.dt == -21)
		{
			if (req.getN() == this.n)
			{
				if (req.type == 1 && req.dt == 1)
					return 0;

				return 1;
			}
			else
				return 0;
		}

		if (this.dt == 2)
		{
			if (req.getN() == this.n)
			{
				return 1;
			}
			else
				return 0;
		}

		if (req.type == 1)    //FR request
		{
			if (req.dt == this.dt)
			{
				if ((this.dt == 1 && req.n <= this.n && req.n > this.fl.getN()) ||
						this.dt == -1 && req.n >= this.n && req.n < this.fl.getN())
					return 1;
				else
					return 0;
			}
			else
				return 0;
		}
		else if (req.type == -1)    //ER request
		{
			if (this.dt == 1 && req.n > this.fl.getN() ||
					this.dt == -1 && req.n < this.fl.getN())
				return 1;
			else
				return 0;
		}
		else
			exph.err(-1);

		return 0;
	}

	public void godie()
	{
		this.godie_flag = 1;
	}

	public void update_status()
	{
		for (int i = this.req_q.front; i < this.req_q.rear; i++)
		{
			if (this.req_q.getBo(i) == true)
			{
				if (this.dt == 0)
				{
					if (this.req_q.getRq(i).n > this.fl.getN())
						this.dt = 1;
					else if (this.req_q.getRq(i).n < this.fl.getN())
						this.dt = -1;
					else
					{
						if (this.req_q.getRq(i).type == 1)
						{
							if (this.req_q.getRq(i).dt == 1)
								this.dt = 21;
							else if (this.req_q.getRq(i).dt == -1)
								this.dt = -21;
							else
								exph.err(-1);
						}
						else
							this.dt = 2;
					}
				}
				if (this.dt == 1)
				{
					if (this.req_q.getRq(i).getN() > this.n)
						this.n = this.req_q.getRq(i).getN();
				}
				if (this.dt == -1)
				{
					if (this.req_q.getRq(i).getN() < this.n)
						this.n = this.req_q.getRq(i).getN();
				}
				if(this.dt==2)
				{
					if (this.req_q.getRq(i).type == 1)    //静止同楼层请求捎带同楼层FR，只捎带一个方向
					{
						if (this.req_q.getRq(i).dt == 1)
							this.dt = 21;
						else if (this.req_q.getRq(i).dt == -1)
							this.dt = -21;
						else
							exph.err(-1);
					}
				}
			}
		}
//		System.out.println("update_status:dt:	" + this.dt);
//
//		System.out.println("update_status:n:	" + this.n);
	}

	@Override
	public void run()
	{
		while (true)
		{
			//System.out.println(this.lift_number+"	"+this.n+"	"+this.fl.getN());
			//move up
			if (this.n > this.fl.getN())
			{
				//System.out.println("123");
				try
				{
					Thread.sleep(3000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				int stop_flag = 0;
				for (int i = this.req_q.front; i < this.req_q.rear; i++)
				{
					if (this.req_q.getBo(i) == true && this.req_q.getRq(i).getN() == this.fl.getN() + 1)
					{
						stop_flag = 1;
						break;
					}
				}
				if (stop_flag == 1)
				{
					try
					{
						Thread.sleep(6000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					this.fl.setN(this.fl.getN() + 1);
					this.s += 1;
//					System.out.println("fl:	" + this.fl.getN());

					for (int i = this.req_q.front; i < this.req_q.rear; i++)
					{
						if (this.req_q.getBo(i) == true && this.req_q.getRq(i).getN() == this.fl.getN())
						{
							this.req_q.setBo(i);
							print(this.req_q.getRq(i));
						}
					}
					while (this.req_q.getBo(this.req_q.front) == false && this.req_q.front < this.req_q.rear)
						this.req_q.pop_front();
				}
				else
				{
					this.fl.setN(this.fl.getN() + 1);
					this.s += 1;
//					System.out.println("fl:	" + this.fl.getN());
				}
			}
			//move down
			else if (this.n < this.fl.getN())
			{
				try
				{
					Thread.sleep(3000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				int stop_flag = 0;
				for (int i = this.req_q.front; i < this.req_q.rear; i++)
				{
					if (this.req_q.getBo(i) == true && this.req_q.getRq(i).getN() == this.fl.getN() - 1)
					{
						stop_flag = 1;
						break;
					}
				}
				if (stop_flag == 1)
				{
					try
					{
						Thread.sleep(6000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					this.fl.setN(this.fl.getN() - 1);
					this.s += 1;

					for (int i = this.req_q.front; i < this.req_q.rear; i++)
					{
						if (this.req_q.getBo(i) == true && this.req_q.getRq(i).getN() == this.fl.getN())
						{
							this.req_q.setBo(i);
							print(this.req_q.getRq(i));
						}
					}
					while (this.req_q.getBo(this.req_q.front) == false && this.req_q.front < this.req_q.rear)
						this.req_q.pop_front();
				}
				else
				{
					this.fl.setN(this.fl.getN() - 1);
					this.s += 1;
				}
			}
			//no movement
			else
			{
				if (this.dt == 2 || this.dt == 21 || this.dt == -21)
				{
					try
					{
						Thread.sleep(6000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					for (int i = this.req_q.front; i < this.req_q.rear; i++)
					{
						if (this.req_q.getRq(i).getN() == this.fl.getN())
						{
							this.req_q.setBo(i);
							print(this.req_q.getRq(i));
						}
					}
					while (this.req_q.getBo(this.req_q.front) == false && this.req_q.front < this.req_q.rear)
						this.req_q.pop_front();
				}

				this.dt = 0;
//				//check new request
//				if (this.req_q.is_empty() == 0)
//				{
//
//				}
			}
			if (this.godie_flag == 1 && this.req_q.is_empty() == 1)
				break;


			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void print(request_3 req)
	{
		String direction;
		if (this.dt == 1)
			direction = "UP";
		else if (this.dt == -1)
			direction = "DOWN";
		else
			direction = "STAY";
		Date now = new Date();
		double clk = ((double) (now.getTime() / 100 - this.time0 / 100)) / 10;
		//System.out.println(now.getTime()-req.start_time);

		String str_out = new String();
		str_out += "(#" + (this.lift_number + 1) + ",#" + this.fl.getN() + "," + direction + "," + this.s + "," + String.valueOf(clk) + ")";
		str_out += "\t完成请求：";

		//System.out.print("(#" + (this.lift_number + 1) + "," + this.fl.getN() + "," + direction + "," + this.s + "," + String.valueOf(clk) + ")");

		if (req.type == 1)
		{
			str_out += ("(FR,");
			str_out += (req.n + ",");
			if (req.dt == 1)
				str_out += ("UP)");
			else if (req.dt == -1)
				str_out += ("DOWN)");
			else
				exph.err(-1);
			System.out.println(str_out);
		}
		else if (req.type == -1)
		{
			str_out += ("(ER,#");
			str_out += (req.getLift_n() + 1 + ",");
			str_out += (req.n + ")");
			System.out.println(str_out);
		}
		else
			exph.err(-1);

	}

	@Override
	public int move(req_que req)
	{
		return 0;
	}

	public void setTime0(long time0)
	{
		this.time0 = time0;
	}

	public int getS()
	{
		return s;
	}
}
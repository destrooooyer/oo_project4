package oo;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by DESTRooooYER on 2016/3/8.
 */
public class Main
{

	/**
	 * 检测输入是否合法
	 *
	 * @param str
	 * @return
	 */
	private static int check(String str)
	{
		Pattern pt;
		pt = Pattern.compile("^((\\(FR,[0-9]+,((UP)|(DOWN)),[0-9]+\\))|(\\(ER,[0-9]+,[0-9]+\\)))+$");

		try
		{
			if (pt.matcher(str).find())
				return 1;
			else
				return 0;
		}
		catch (java.lang.StackOverflowError e)
		{
			exph.err(3);
		}
		return 0;
	}


	public static void main(String wtf[])
	{
//		String str_in;
//		Scanner scanner = new Scanner(System.in);
//		str_in = scanner.nextLine();
//
//		if (check(str_in) == 1)
//		{
//			req_que req = new req_que(str_in);
//
//			req.check();
//
//			//req.print();
////			DISP disp = new DISP(req);
////
////			disp.godie();
//
//			DISP_2 disp=new DISP_2(req);
//			disp.ALS_Schedule();
//
//		}
//		else
//			exph.err(0);

		//init
		long time0 = -1;

		String str_in;
		Scanner sc = new Scanner(System.in);

		DISP_3 disp = new DISP_3();    //create disp
		lift_3 __lift[] = new lift_3[3];    //creat lifts
		for (int i = 0; i < 3; i++)
			__lift[i] = new lift_3(i);
		disp.add_lifts(__lift);

		//start the disp and lifts
		Thread T_disp = new Thread(disp);
		T_disp.start();
		Thread[] T_lift = new Thread[3];
		for (int i = 0; i < 3; i++)
		{
			T_lift[i] = new Thread(__lift[i]);
			T_lift[i].start();
		}

		//main loop
		while (true)
		{
			//input request
			str_in = sc.nextLine();

			//end?
			if (str_in.equals("end"))
			{
				disp.godie();
				//System.out.println("1");
				try
				{
					T_disp.join();
					//System.out.println("2");
					for (int i = 0; i < 3; i++)
						T_lift[i].join();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				//System.out.println("3");
				break;
			}


			//get time
			Date now = new Date();
			long clk = now.getTime();

			request_3 temp_req = new request_3();
			int bo = temp_req.get_req(str_in);
			if (bo == 0 || temp_req.check() == 0)    //wrong input
				System.out.println("上条请求不符合输入要求，被忽略");
			else
			{
				if (time0 == -1)
				{
					time0 = clk;    //set time0
					for (int i = 0; i < 3; i++)
						__lift[i].setTime0(time0);
				}
				temp_req.t = (int) (clk - time0);

				disp.push_request(temp_req);
			}
//			System.out.println(str_in);
		}
	}
}

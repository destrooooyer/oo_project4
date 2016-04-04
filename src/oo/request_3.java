package oo;

import java.util.regex.Pattern;

/**
 * Created by DESTRooooYER on 2016/4/2.
 */
public class request_3 extends request
{
	private int lift_n;
	protected long start_time;

	public request_3()
	{
		this.n = 0;
		this.t = 0;
		this.type = 0;
		this.dt = 0;
		this.start_time = 0;
	}

	public int check()
	{
		if (this.type == 1)
		{
			if (this.n == 20 && this.dt == 1)
				return 0;
			if (this.n == 1 && this.dt == -1)
				return 0;
		}
		return 1;
	}

	public int get_req(String str)
	{
		//match FR requests
		Pattern pt_FR;
		pt_FR = Pattern.compile("^\\(FR,(([1-9])|(1[0-9])|20),((UP)|(DOWN))\\)$");
		try
		{
			if (pt_FR.matcher(str).find())
			{
				//get request from str
				str = str.replaceAll("[\\(\\)]", "");    //remove '(' and ')'

				String str_temp[] = str.split(",");
				for (int i = 0, j = 0; i < str_temp.length; i++)
				{
					if (!str_temp[i].equals(""))
					{
						if (j == 0)
						{
							if (str_temp[i].equals("FR"))
								this.type = 1;
							else
								exph.err(-1);
						}
						else if (j == 1)
						{
							try
							{
								this.n = Integer.parseInt(str_temp[i]);
							}
							catch (java.lang.NumberFormatException e)
							{
								exph.err(-1);
							}
						}
						else if (j == 2)
						{
							if (str_temp[i].equals("UP"))
								this.dt = 1;
							else if (str_temp[i].equals("DOWN"))
								this.dt = -1;
							else
								exph.err(-1);
						}


						j++;
					}
				}

				return 1;
				//matching and getting request succeed then return 1
			}
			else
			{
				//failed to match pattern "FR", then try to match "ER" later
			}
		}
		catch (java.lang.StackOverflowError e)
		{
			exph.err(3);
		}

		//match "ER" requests
		Pattern pt_ER;
		pt_ER = Pattern.compile("^\\(ER,#[1-3],(([1-9])|(1[0-9])|20)\\)$");
		try
		{
			if (pt_ER.matcher(str).find())
			{
				//get request from str
				str = str.replaceAll("[\\(\\)]", "");    //remove '(' and ')'

				String str_temp[] = str.split(",");
				for (int i = 0, j = 0; i < str_temp.length; i++)
				{
					if (!str_temp[i].equals(""))
					{
						if (j == 0)            //get the id "ER"
						{
							if (str_temp[i].equals("ER"))
								this.type = -1;
							else
								exph.err(-1);
						}
						else if (j == 1)    //get number of lift
						{
							str_temp[i] = str_temp[i].replaceAll("#", "");    //remove '#'
							try
							{
								this.lift_n = Integer.parseInt(str_temp[i]) - 1;
							}
							catch (java.lang.NumberFormatException e)
							{
								exph.err(-1);
							}
						}
						else if (j == 2)    //get floor number
						{
							try
							{
								this.n = Integer.parseInt(str_temp[i]);
							}
							catch (java.lang.NumberFormatException e)
							{
								exph.err(-1);
							}
						}


						j++;
					}
				}

				return 1;
				//succeed return 1
			}
			else
			{
				//matching "ER" failed either, return 0
				return 0;
			}
		}
		catch (java.lang.StackOverflowError e)
		{
			exph.err(3);
		}

		return 0;
	}

	public int getLift_n()
	{
		return lift_n;
	}
}

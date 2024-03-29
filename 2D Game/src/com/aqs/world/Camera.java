package com.aqs.world;

public class Camera
{
	public static int x, y = 0;
	
	public static int clamp(int Cur, int Min, int Max) 
	{
		if(Cur < Min) 
		{
			Cur = Min;
		}
		
		if(Cur > Max) 
		{
			Cur = Max;
		}
		
		return Cur;
	}
}

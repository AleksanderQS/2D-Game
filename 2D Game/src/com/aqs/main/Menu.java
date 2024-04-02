package com.aqs.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu 
{
	public String[] options = {"New game", "Load game", "Quit"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
	public void tick() 
	{
		if (up) 
		{
			up = false;
			currentOption--;
			if (currentOption < 0) 
			{
				currentOption = maxOption;
			}
		}
		
		if (down)
		{
			down = false;
			currentOption++;
			if (currentOption > maxOption) 
			{
				currentOption = 0;
			}
		}
		
		if (enter) 
		{
			enter = false;
			if (options[currentOption] == "New game" || options[currentOption] == "Continue") 
			{
				Game.gameState = "NORMAL";
				pause = false;
			}
			else if(options[currentOption] == "Quit") 
			{
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.orange);
		g.setFont(new Font("arial", Font.BOLD, 36*2));
		g.drawString("World monsters", (Game.WIDTH * Game.SCALE) / 2 - 220, (Game.HEIGHT * Game.SCALE) / 2 - 390);
		
		// Menu options
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 36));
		if(!pause) 
		{
			g.drawString("New game", (Game.WIDTH * Game.SCALE) / 2 - 800, (Game.HEIGHT * Game.SCALE) / 2 - 200);
		}
		else 
		{
			g.drawString("Continue", (Game.WIDTH * Game.SCALE) / 2 - 800, (Game.HEIGHT * Game.SCALE) / 2 - 200);
			
		}
		
		g.drawString("Load game", (Game.WIDTH * Game.SCALE) / 2 - 800, (Game.HEIGHT * Game.SCALE) / 2 - 150);
		g.drawString("Quit", (Game.WIDTH * Game.SCALE) / 2 - 800, (Game.HEIGHT * Game.SCALE) / 2 - 100);
		
		if (options[currentOption] == "New game") 
		{
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 830, (Game.HEIGHT * Game.SCALE) / 2 - 200);
		}
		else if (options[currentOption] == "Load game") 
		{
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 830, (Game.HEIGHT * Game.SCALE) / 2 - 150);
		}
		else if (options[currentOption] == "Quit") 
		{
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 830, (Game.HEIGHT * Game.SCALE) / 2 - 100);
		}
	}
}

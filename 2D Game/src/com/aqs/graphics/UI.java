package com.aqs.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.aqs.main.Game;

public class UI 
{
	public void render(Graphics g) 
	{
		g.setColor(Color.red);
		g.fillRect(10, 10, 100, 15);
		g.setColor(Color.green);
		g.fillRect(10, 10, (int)((Game.player.life / Game.player.maxLife) * 100), 15);
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 12));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 40, 22);
	}
}

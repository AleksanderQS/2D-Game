package com.aqs.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.aqs.main.Game;
import com.aqs.world.Camera;

public class Bullet extends Entity
{
	private double dx, dy;
	private double spd = 10;
	
	private int life = 100, curLife = 0;
	
	public Bullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() 
	{
		x += dx * spd;
		y += dy * spd;
		curLife++;
		if(curLife == life) 
		{
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) 
	{
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 4, 4);
	}
}

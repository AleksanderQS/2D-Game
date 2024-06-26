package com.aqs.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.aqs.main.Game;
import com.aqs.world.Camera;

public class Entity 
{
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(5*64, 0, 64, 64);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(6*64, 0, 64, 64);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(5*64, 64, 64, 64);
	public static BufferedImage MULA = Game.spritesheet.getSprite(6*64, 64, 64, 64);
	public static BufferedImage MULA_FEEDBACK = Game.spritesheet.getSprite(6*64, 128, 64, 64);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(7*64, 0, 64, 64);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(6*64, 0, 64, 64);
	public static BufferedImage CURUPIRA = Game.spritesheet.getSprite(3*64, 0, 64, 64);
	
	
	protected double x, y;
	protected int width, height;
	
	private BufferedImage sprite;
	
	private int maskx, masky, mwidth, mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) 
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) 
	{
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public int getX() 
	{
		return (int) this.x;
	}
	
	public void setX(int newX) 
	{
		this.x = newX;
	}

	public int getY() 
	{
		return (int) this.y;
	}

	public void setY(int newY) 
	{
		this.y = newY;
	}

	public int getWidth() 
	{
		return width;
	}

	public void setWidth(int width) 
	{
		this.width = width;
	}

	public int getHeight() 
	{
		return height;
	}

	public void setHeight(int height) 
	{
		this.height = height;
	}
	
	public void tick() 
	{
		
	}
	
	public static boolean isColliding(Entity e1, Entity e2) 
	{
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g)
	{
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		// g.setColor(Color.red);
		// g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
	}
}

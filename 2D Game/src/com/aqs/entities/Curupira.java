package com.aqs.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.aqs.main.Game;
import com.aqs.main.Sound;
import com.aqs.world.Camera;
import com.aqs.world.World;

public class Curupira extends Entity
{
	private double speed = 1.8;
	
	private int maskx = 16, masky = 10, maskw = 30, maskh = 50;
	
	private int frames = 0, maxFrames = 13, index = 0, maxIndex = 1;
	
	private BufferedImage[] sprites;
	
	private int life = 100;
	
	private boolean isDamaged = false;
	private int damageFrames = 5, damageCurrent = 0;

	public Curupira(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(3*64, 64, 64, 64);
		sprites[1] = Game.spritesheet.getSprite(4*64, 64, 64, 64);
	}
	
	public void tick() 
	{
		if(isCollidingWithPlayer() == false) 
		{
			if((int) x < Game.player.getX() && World.isFree((int)(x + speed), this.getY())
				&& !isColliding((int)(x + speed), this.getY())) 
			{
				x += speed;
			}
			else if((int) x > Game.player.getX() && World.isFree((int)(x - speed), this.getY())
					&& !isColliding((int)(x - speed), this.getY()))
			{
				x -= speed;
			}
			
			if((int) y < Game.player.getY()  && World.isFree (this.getX(), (int)(y + speed))
					&& !isColliding(this.getX(), (int)(y + speed))) 
			{
				y += speed;
			}
			else if((int) y > Game.player.getY()  && World.isFree (this.getX(), (int)(y - speed))
					&& !isColliding(this.getX(), (int)(y - speed))) 
			{
				y -= speed;
			}
		}else
		{
			if (Game.rand.nextInt(100) < 20) 
			{
				Sound.hurtSound.play();
				Game.player.life -= Game.rand.nextInt(3);
				Game.player.isDamaged = true;
			}
			
		}
		
		frames++;
		if(frames == maxFrames) 
		{
			frames = 0;
			index++;
			if(index > maxIndex)
				index = 0;
		}
		
		collidingBullet();
		
		if(life <= 0) 
		{
			destroySelf();
			return;
		}
		
		if(isDamaged) 
		{
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) 
			{
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}
	}
	
	public void destroySelf() 
	{
		Game.curupira.remove(this);
		Game.entities.remove(this);
	}
		
	public void collidingBullet() 
	{
		for(int i = 0; i < Game.bullets.size(); i++) 
		{
			Entity e = Game.bullets.get(i);
			if(Entity.isColliding(this, e)) 
			{
				isDamaged = true;
				life-= Game.rand.nextInt(21);
				Game.bullets.remove(i);
				return;
			}
		}
	}
		
	
	public boolean isCollidingWithPlayer() 
	{
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 64, 64);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliding(int xnext, int ynext) 
	{
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for(int i = 0; i < Game.curupira.size(); i++) 
		{
			Curupira e = Game.curupira.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)){
				return true;
			}
		}
		return false;
	}
	
	public void render(Graphics g) 
	{
		if (!isDamaged)
		{
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		else
		{
			g.drawImage(Entity.CURUPIRA, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}
}

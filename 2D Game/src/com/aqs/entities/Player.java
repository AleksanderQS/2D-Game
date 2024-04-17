package com.aqs.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.aqs.main.Game;
import com.aqs.main.Sound;
import com.aqs.world.Camera;
import com.aqs.world.World;

public class Player extends Entity
{
	public boolean up, down, left, right;
	public int down_dir = 0, up_dir = 1, right_dir = 2, left_dir = 3;
	public int dir = down_dir;
	public double speed = 3.5;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 2;
	private boolean moved = false;
	private BufferedImage[] downPlayer;
	private BufferedImage[] upPlayer;
	
	private BufferedImage playerDamage;
	private int damageFrames = 0;
	
	private boolean gun = false;
	
	public int ammo = 0;
	private long lastShootTime = 0;
	private final long shootCooldown = 1800;
	
	public boolean shoot = false, mouseShoot = false;
	
	public boolean isDamaged = false;
	
	public double life = 100, maxLife = 100;
	public int mx, my;

	public Player(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		downPlayer = new BufferedImage[3];
		upPlayer = new BufferedImage[3];
		playerDamage = Game.spritesheet.getSprite(0, 128, 64, 64);
		for(int i = 0; i < 3; i++) 
		{
			downPlayer[i] = Game.spritesheet.getSprite(i * 64, 0, 64, 64);
		}
		for(int i = 0; i < 3; i++) 
		{
			upPlayer[i] = Game.spritesheet.getSprite(i * 64, 64, 64, 64);
		}
	}

	public void tick() 
	{ 
		moved = false;
		if (up && World.isFree(this.getX(), (int)(y - speed))) 
		{
			moved = true;
			dir = up_dir;
			y -= speed;
		}
		else if (down && World.isFree(this.getX(), (int)(y + speed)))
		{
			moved = true;
			dir = down_dir;
			y += speed;
		}
			
		if (right && World.isFree((int)(x + speed), this.getY())) 
		{
			moved = true;
			dir = right_dir;
			x += speed;
		}
		else if (left && World.isFree((int)(x - speed), this.getY())) 
		{
			moved = true;
			dir = left_dir;
			x -= speed;
		}	
		
		if(moved) 
		{
			frames++;
			if(frames == maxFrames) 
			{
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		}
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		
		if(isDamaged) 
		{
			this.damageFrames++;
			if(this.damageFrames == 8) 
			{
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot && System.currentTimeMillis() - lastShootTime > shootCooldown) 
		{
			shoot = false;
			int dx = 0;
			int px = 0;
			int py = 38;
			if (dir == right_dir) 
			{
				px = 69;
				dx = 1;
			}
			else 
			{
				px = -10;
				dx = -1;
			}
			if(gun && ammo > 0) 
			{
				Sound.shotSound.play();
				ammo--;
				
				Bullet bullet = new Bullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
				
				lastShootTime = System.currentTimeMillis();
			}
		}
		
		if(mouseShoot && System.currentTimeMillis() - lastShootTime > shootCooldown) 
		{
			mouseShoot = false;
			
			if(gun && ammo > 0) 
			{
				Sound.shotSound.play();
				ammo--;

				int px = 0;
				int py = 38;
				double angle = 0;
				
				if (dir == right_dir || dir == down_dir) 
				{
					px = 69;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));
				}
				else if (dir == left_dir || dir == up_dir) 
				{
					px = -10;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));
				}	
				
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				Bullet bullet = new Bullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
				
				lastShootTime = System.currentTimeMillis();
			}
		}
		
		if (life <= 0) 
		{
			life = 0;
			Game.gameState = "GAME_OVER";
		}		
		
		updateCamera();
	}
	
	public void updateCamera() 
	{
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 64 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 64- Game.HEIGHT);
	}
	
	public void checkCollisionGun() 
	{
		for(int i = 0; i < Game.entities.size(); i++) 
		{
			Entity e = Game.entities.get(i);
			if(e instanceof Weapon) 
			{
				if(Entity.isColliding(this, e))
				{
					gun = true;
					Game.entities.remove(e);
				}
			}
		}
	}
	
	public void checkCollisionAmmo() 
	{
		for(int i = 0; i < Game.entities.size(); i++) 
		{
			Entity e = Game.entities.get(i);
			if(e instanceof Ammo) 
			{
				if(Entity.isColliding(this, e))
				{
					ammo += 25;
					if (life > maxLife) 
						life = maxLife;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	public void checkCollisionLifePack() 
	{
		for(int i = 0; i < Game.entities.size(); i++) 
		{
			Entity e = Game.entities.get(i);
			if(e instanceof Lifepack) 
			{
				if(Entity.isColliding(this, e))
				{
					life += 10;
					if (life > maxLife) 
						life = maxLife;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	public void render(Graphics g) 
	{
		if (!isDamaged) 
		{
			if(dir == down_dir || dir == right_dir) 
			{
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (gun) 
				{
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 20 - Camera.x, this.getY() + 10 - Camera.y, null);
				}
			}
			else if (dir == up_dir || dir == left_dir) 
			{
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);	
				if (gun) 
				{
					g.drawImage(Entity.GUN_LEFT, this.getX() - 20 - Camera.x, this.getY() + 10 - Camera.y, null);
				}
			}
		}
		else 
		{
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}

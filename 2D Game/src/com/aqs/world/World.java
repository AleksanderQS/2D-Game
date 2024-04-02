package com.aqs.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.aqs.entities.*;
import com.aqs.graphics.Spritesheet;
import com.aqs.main.Game;

public class World 
{
	
	private static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static int TILE_SIZE = 64;
	
	public World(String path) 
	{
		try 
		{
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++) 
			{
				for(int yy = 0; yy < map.getHeight(); yy++) 
				{
					int curPixel = pixels[xx + (yy * map.getWidth())];
					tiles[xx +(yy * WIDTH)] = new FloorTile(xx * 64, yy * 64, Tile.TILE_FLOOR);
					if(curPixel == 0xFF000000) 
					{
						// Floor | Black
						tiles[xx +(yy * WIDTH)] = new FloorTile(xx * 64, yy * 64, Tile.TILE_FLOOR);
					}
					else if(curPixel == 0xFFFFFFFF) 
					{
						// Wall | White
						tiles[xx +(yy * WIDTH)] = new WallTile(xx * 64, yy * 64, Tile.TILE_WALL);
					}
					else if(curPixel == 0xFF0026FF) 
					{
						// Player | Blue
						Game.player.setX(xx * 64);
						Game.player.setY(yy * 64);
					}
					else if(curPixel == 0xFFFF0000)
					{
						// Enemy | Red
						Enemy en = new Enemy(xx * 64, yy * 64, 64, 64, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}
					else if(curPixel == 0xFFFF6A00) 
					{
						// Weapon | Orange
						Game.entities.add(new Weapon(xx * 64, yy * 64, 64, 64, Entity.WEAPON_EN));
					}
					else if(curPixel == 0xFFFFD800) 
					{
						// Ammo | Yellow
						Game.entities.add(new Ammo(xx * 64, yy * 64, 64, 64, Entity.BULLET_EN));
					}
					else if(curPixel == 0xFF4CFF00)
					{
						// Life Pack | Green
						Lifepack pack = new Lifepack(xx * 64, yy * 64, 64, 64, Entity.LIFEPACK_EN);
						pack.setMask(14, 37, 36, 27);
						Game.entities.add(pack);
					}
				}
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xnext, int ynext) 
	{
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		
		return !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
				 tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
				 tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
				 tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile);
	}
	
	public static void restartGame(String level) 
	{
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 64, 64, Game.spritesheet.getSprite(0, 0, 64, 64));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;
	}
	
	public void render(Graphics g) {
	    int xstart = Math.max(0, Camera.x / TILE_SIZE);
	    int ystart = Math.max(0, Camera.y / TILE_SIZE);
	    
	    int xfinal = Math.min(WIDTH - 1, (Camera.x + Game.WIDTH) / TILE_SIZE);
	    int yfinal = Math.min(HEIGHT - 1, (Camera.y + Game.HEIGHT) / TILE_SIZE);
	    
	    for(int xx = xstart; xx <= xfinal; xx++) {
	        for(int yy = ystart; yy <= yfinal; yy++) {
	            if (xx + yy * WIDTH < tiles.length && xx + yy * WIDTH >= 0) {
	                Tile tile = tiles[xx + yy * WIDTH];
	                tile.render(g);
	            }
	        }
	    }
	}

}

/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.fushiginopixel.fushiginopixeldungeon.levels;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Bones;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DM300;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DM5000;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.ToxicTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.Trap;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTileSheet;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class CavesBossLevel extends Level {
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

		viewDistance = Math.min(6, viewDistance);
	}

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;

	private static final int ROOM_LEFT		= WIDTH / 2 - 3;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 1;
	private static final int ROOM_TOP		= HEIGHT / 2 - 2;
	private static final int ROOM_BOTTOM	= HEIGHT / 2 + 2;
	
	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}
	
	private static final String DOOR	= "door";
	private static final String ENTERED	= "entered";
	private static final String DROPPED	= "droppped";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DOOR, arenaDoor );
		bundle.put( ENTERED, enteredArena );
		bundle.put( DROPPED, keyDropped );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		arenaDoor = bundle.getInt( DOOR );
		enteredArena = bundle.getBoolean( ENTERED );
		keyDropped = bundle.getBoolean( DROPPED );
	}
	
	@Override
	protected boolean build() {
		
		setSize(WIDTH, HEIGHT);

		Rect space = new Rect();

		space.set(
				Random.IntRange(2, 2 + (int)(width*0.2f)),
				Random.IntRange(2, 2 + (int)(height*0.2f)),
				Random.IntRange((int)(width * 0.8f - 2), width-2 ),
				Random.IntRange((int)(height * 0.8f - 2), height-2 )
		);

		Painter.fillEllipse( this, space, Terrain.EMPTY );

		exit = space.left + space.width()/2 + (space.top - 1) * width();
		
		map[exit] = Terrain.LOCKED_EXIT;
		
		Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1,
			ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL );
		Painter.fill( this, ROOM_LEFT, ROOM_TOP + 1,
			ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP, Terrain.EMPTY );

		Painter.fill( this, ROOM_LEFT, ROOM_TOP,
			ROOM_RIGHT - ROOM_LEFT + 1, 1, Terrain.EMPTY_DECO );
		
		arenaDoor = Random.Int( ROOM_LEFT, ROOM_RIGHT ) + (ROOM_BOTTOM + 1) * width();
		map[arenaDoor] = Terrain.DOOR;
		
		entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) +
			Random.Int( ROOM_TOP + 1, ROOM_BOTTOM - 1 ) * width();
		map[entrance] = Terrain.ENTRANCE;

		for (int i=0; i < length(); i++) {
			boolean noWall = true;
			for(int j = 0; j < PathFinder.NEIGHBOURS8.length; j++){
				int pos = i + PathFinder.NEIGHBOURS8[j];
				if(pos >= 0 && pos < length() && map[pos] == Terrain.WALL){
					noWall = false;
					break;
				}
			}
			if (map[i] == Terrain.EMPTY && map[i] != Terrain.ENTRANCE && map[i]!= Terrain.ENTRANCE && Random.Int( 5 ) == 0 && noWall) {
				map[i] = Terrain.WALL;
			}
		}
		
		boolean[] patch = Patch.generate( width, height, 0.30f, 6, true );
		for (int i=0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}
		
		for (int i=width() + 1; i < length() - width(); i++) {
			if (map[i] == Terrain.EMPTY) {
				int n = 0;
				if (map[i+1] == Terrain.WALL) {
					n++;
				}
				if (map[i-1] == Terrain.WALL) {
					n++;
				}
				if (map[i+width()] == Terrain.WALL) {
					n++;
				}
				if (map[i-width()] == Terrain.WALL) {
					n++;
				}
				if (Random.Int( 8 ) <= n) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		for (int i=0; i < length() - width(); i++) {
			if (map[i] == Terrain.WALL
					&& DungeonTileSheet.floorTile(map[i + width()])
					&& Random.Int( 3 ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		return true;
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * width();
			} while (pos == entrance);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell() {
		int cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		while (!passable[cell]){
			cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		}
		return cell;
	}
	
	@Override
	public void press( int cell, Char hero ) {
		
		super.press( cell, hero );
		
		if (!enteredArena && outsideEntraceRoom( cell ) && hero == Dungeon.hero) {
			
			enteredArena = true;
			seal();
			
			for (Mob m : mobs){
				//bring the first ally with you
				if (m.alignment == Char.Alignment.ALLY){
					m.pos = Dungeon.hero.pos + (Random.Int(2) == 0 ? +1 : -1);
					m.sprite.place(m.pos);
					break;
				}
			}
			
			DM5000 boss = new DM5000();
			boss.state = boss.WANDERING;
			do {
				boss.pos = Random.Int( length() );
			} while (
				!passable[boss.pos] ||
				!outsideEntraceRoom( boss.pos ) ||
				heroFOV[boss.pos]);
			GameScene.add( boss );
			
			set( arenaDoor, Terrain.WALL );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();
			
			CellEmitter.get( arenaDoor ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );
		}
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (!keyDropped && item instanceof SkeletonKey) {
			
			keyDropped = true;
			unseal();
			
			CellEmitter.get( arenaDoor ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			
			set( arenaDoor, Terrain.EMPTY_DECO );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();
		}
		
		return super.drop( item, cell );
	}
	
	private boolean outsideEntraceRoom( int cell ) {
		int cx = cell % width();
		int cy = cell / width();
		return cx < ROOM_LEFT-1 || cx > ROOM_RIGHT+1 || cy < ROOM_TOP-1 || cy > ROOM_BOTTOM+1;
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.GRASS:
				return Messages.get(CavesLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_name");
			case Terrain.WATER:
				return Messages.get(CavesLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc( int tile ) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CavesLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CavesLevel.class, "exit_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_desc");
			case Terrain.WALL_DECO:
				return Messages.get(CavesLevel.class, "wall_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CavesLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		CavesLevel.addCavesVisuals(this, visuals);
		return visuals;
	}
}

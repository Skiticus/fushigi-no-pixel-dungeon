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

package com.fushiginopixel.fushiginopixeldungeon.plants;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Fire;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Freezing;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfFrost;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;

public class Icecap extends Plant {
	
	{
		image = 1;
	}
	
	@Override
	public void activate() {
		
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.losBlocking, null ), 1 );
		
		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
		
		for (int i=0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Freezing.affect( i, fire );
			}
		}
	}
	
	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_ICECAP;

			plantClass = Icecap.class;
			alchemyClass = PotionOfFrost.class;
		}
	}
}

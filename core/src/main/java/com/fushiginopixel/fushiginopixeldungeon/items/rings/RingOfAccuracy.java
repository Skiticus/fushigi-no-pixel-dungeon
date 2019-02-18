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

package com.fushiginopixel.fushiginopixeldungeon.items.rings;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;

public class RingOfAccuracy extends Ring {
	
	@Override
	protected RingBuff buff( ) {
		return new Accuracy();
	}
	
	//The ring of accuracy reduces enemy evasion
	// this makes it more powerful against more evasive enemies
	public static float enemyEvasionMultiplier( Char target ){
		if(getBonus(target, Accuracy.class) > 5)
			return 0;
		else
			return (float)Math.pow(0.75, getBonus(target, Accuracy.class));
	}
	
	public class Accuracy extends RingBuff {
	}
}

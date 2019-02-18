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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee;

import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.Assault;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spear extends MeleeWeapon {

	{
		image = ItemSpriteSheet.SPEAR;

		tier = 2;
		DLY = 1.5f; //0.67x speed
		RCH = 2;    //extra reach
		LIMIT = 3;
		properties = new ArrayList<Enchantment>(){
			{
				add(new Assault());
			}
		};
	}

	@Override
	public int max(int lvl) {
		return  20 +    //20 base, up from 15
				lvl*(UPGRADE_ATTACK+1); //+4 per level, up from +3
	}

}

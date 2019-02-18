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

package com.fushiginopixel.fushiginopixeldungeon.sprites;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.watabou.noosa.TextureFilm;

public class IronScorpioSprite extends ScorpioSprite {

	public IronScorpioSprite() {
		super();

		texture( Assets.SCORPIO );

		TextureFilm frames = new TextureFilm( texture, 18, 17 );

		int i = 14;
		idle = new Animation( 12, true );
		idle.frames( frames, i, i, i, i, i, i, i, i, i+1, i+2, i+1, i+2, i+1, i+2 );

		run = new Animation( 4, true );
		run.frames( frames, i+5, i+6 );

		attack = new Animation( 15, false );
		attack.frames( frames, i, i+3, i+4 );

		//zap = attack.clone();

		die = new Animation( 12, false );
		die.frames( frames, i, i+7, i+8, i+9, i+10 );

		play( idle );
	}
	
	@Override
	public int blood() {
		return 0xFF44FF22;
	}
}

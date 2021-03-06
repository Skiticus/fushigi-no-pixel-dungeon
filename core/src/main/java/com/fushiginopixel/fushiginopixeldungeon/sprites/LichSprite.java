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
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Lich;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.FishingSpear;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class LichSprite extends MobSprite {
	private Animation cast;

	public LichSprite() {
		super();

		int ofs = 42;
		texture( Assets.SKELETON );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );
		
		idle = new Animation( 12, true );
		idle.frames( frames, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+0, ofs+1, ofs+2, ofs+3 );
		
		run = new Animation( 15, true );
		run.frames( frames, ofs+4, ofs+5, ofs+6, ofs+7, ofs+8, ofs+9 );
		
		attack = new Animation( 8, false );
		attack.frames( frames, ofs+14, ofs+15, ofs+16 );

		cast = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, ofs+10, ofs+11, ofs+12, ofs+13 );
		
		play( idle );
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );

		MagicMissile.boltFromChar( parent,
				MagicMissile.SHADOW,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((Lich)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}
	
	@Override
	public void die() {
		super.die();
		if (Dungeon.level.heroFOV[ch.pos]) {
			emitter().burst( Speck.factory( Speck.BONE ), 6 );
		}
	}
	
	@Override
	public int blood() {
		return 0xFF555555;
	}
}

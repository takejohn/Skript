/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package org.skriptlang.skript.registration;

import org.skriptlang.skript.registration.SkriptRegistry.ChildKey;
import org.skriptlang.skript.registration.SkriptRegistry.Key;

class KeyImpl<T extends SyntaxInfo<?>> implements Key<T> {
	
	protected final String name;
	
	KeyImpl(String name) {
		this.name = name;
	}
	
	@Override
	public String name() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Key<?>))
			return false;
		Key<?> key = (Key<?>) other;
		return name().equals(key.name());
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	static final class Child<T extends P, P extends SyntaxInfo<?>> extends KeyImpl<T> implements ChildKey<T, P> {
		
		private final Key<P> parent;
		
		Child(Key<P> parent, String name) {
			super(name);
			this.parent = parent;
		}
		
		@Override
		public Key<P> parent() {
			return parent;
		}
		
	}
	
}

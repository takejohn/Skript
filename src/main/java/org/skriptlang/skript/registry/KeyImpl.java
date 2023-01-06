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
package org.skriptlang.skript.registry;

import org.skriptlang.skript.registry.SkriptRegistry.Key;

final class KeyImpl<T extends SyntaxInfo<?>> implements Key<T> {
	
	static <T extends SyntaxInfo<?>> Key<T> of(String name) {
		return new KeyImpl<>(name);
	}
	
	private final String name;
	
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
		if (!(other instanceof Key<?>)) return false;
		Key<?> key = (Key<?>) other;
		return name().equals(key.name());
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	static final class Child<T extends P, P extends SyntaxInfo<?>> implements SkriptRegistry.ChildKey<T, P> {
		
		static <T extends P, P extends SyntaxInfo<?>> SkriptRegistry.Key<T> of(SkriptRegistry.Key<P> parent, String name) {
			return new Child<>(parent, name);
		}
		
		private final Key<P> parent;
		private final String name;
		
		Child(Key<P> parent, String name) {
			this.parent = parent;
			this.name = name;
		}
		
		@Override
		public String name() {
			return name;
		}
		
		@Override
		public SkriptRegistry.Key<P> parent() {
			return parent;
		}
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Key<?>)) return false;
			Key<?> key = (Key<?>) other;
			return name().equals(key.name());
		}
		
		@Override
		public String toString() {
			return name;
		}
		
	}
	
}

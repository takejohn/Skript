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
package org.skriptlang.skript;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
final class SkriptRegistryImpl implements SkriptRegistry {
	
	private final Map<Key<?>, SyntaxRegistry<?>> registries = new ConcurrentHashMap<>();
	
	@Override
	@Unmodifiable
	public <I extends SyntaxInfo<?>> Set<I> syntaxes(Key<I> key) {
		return registry(key).syntaxes();
	}
	
	@Override
	public <I extends SyntaxInfo<?>> void register(Key<I> key, I info) {
		registry(key).register(info);
		
		if (key instanceof ChildKey)
			register(((ChildKey<? extends I, I>) key).parent(), info);
	}
	
	public void closeRegistration() {
		synchronized (registries) {
			for (Map.Entry<Key<?>, SyntaxRegistry<?>> entry : registries.entrySet())
				entry.setValue(entry.getValue().closeRegistration());
		}
	}
	
	private <I extends SyntaxInfo<?>> SyntaxRegistry<I> registry(Key<I> key) {
		return (SyntaxRegistry<I>) registries.computeIfAbsent(key, k -> new SyntaxRegistryImpl<>());
	}
	
}
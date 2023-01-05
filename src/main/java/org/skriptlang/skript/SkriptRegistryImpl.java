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

import ch.njol.skript.lang.SyntaxElement;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

final class SkriptRegistryImpl implements SkriptRegistry {
	
	private final Map<Key<?>, SyntaxRegistry<?>> registries = new ConcurrentHashMap<>();
	
	@Override
	@Unmodifiable
	public <T extends SyntaxElement> Set<RegistrationInfo<T>> syntaxes(Key<T> key) {
		return registry(key).syntaxes();
	}
	
	@Override
	public <T extends SyntaxElement> SyntaxRegistry<T> register(Key<T> key, RegistrationInfo<T> info) {
		SyntaxRegistry<T> registry = registry(key);
		registry.register(info);
		return registry;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends SyntaxElement> SyntaxRegistry<T> registry(Key<T> key) {
		return (SyntaxRegistry<T>) registries.computeIfAbsent(key, k -> new SyntaxRegistryImpl<>());
	}
	
}

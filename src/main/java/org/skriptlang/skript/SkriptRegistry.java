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

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.Set;

/**
 * The skript registry manages all registries for syntax registration.
 */
@ApiStatus.Experimental
public interface SkriptRegistry {
	
	@Unmodifiable
	<I extends SyntaxInfo<?>> Set<I> syntaxes(Key<I> key);
	
	<I extends SyntaxInfo<?>> SyntaxRegistry<I> register(Key<I> key, I info);
	
	interface Key<T extends SyntaxInfo<?>> {
		
		Key<SyntaxInfo<Condition>> CONDITION = key("condition");
		Key<DefaultSyntaxInfos.Expression<?, ?>> EXPRESSION = key("expression");
		Key<SyntaxInfo<Effect>> EFFECT = key("effect");
		Key<SyntaxInfo<SkriptEvent>> EVENT = key("event");
		Key<SyntaxInfo<Section>> SECTION = key("section");
		Key<SyntaxInfo<Structure>> STRUCTURE = key("structure");
		
		static <T extends SyntaxInfo<?>> Key<T> key(String key) {
			return new Key<T>() {
				@Override
				public String toString() {
					return key;
				}
				
				@Override
				public int hashCode() {
					return key.hashCode();
				}
				
				@Override
				public boolean equals(Object obj) {
					//noinspection ConditionCoveredByFurtherCondition
					return obj instanceof String && key.equals(obj);
				}
			};
		}
		
	}
	
}

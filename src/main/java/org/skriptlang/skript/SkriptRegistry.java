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
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SyntaxElement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.Set;

/**
 * The syntax registry manages syntax registration. Registering may not be possible
 * after the registration stage is over.
 */
@ApiStatus.Experimental
public interface SkriptRegistry {
	
	@Unmodifiable
	<T extends SyntaxElement> Set<RegistrationInfo<T>> syntaxes(Key<T> key);
	
	<T extends SyntaxElement> SyntaxRegistry<T> register(Key<T> key, RegistrationInfo<T> info);
	
	interface Key<T extends SyntaxElement> {
		
		Key<Condition> CONDITION = key("condition");
		Key<Expression<?>> EXPRESSION = key("expression");
		Key<Effect> EFFECT = key("effect");
		Key<SkriptEvent> EVENT = key("event");
		Key<Section> SECTION = key("section");
		Key<Structure> STRUCTURE = key("structure");
		
		static <T extends SyntaxElement> Key<T> key(String key) {
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

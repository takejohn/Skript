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
import ch.njol.skript.lang.Statement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

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
		
		Key<SyntaxInfo<? extends Condition>> CONDITION = key("condition");
		Key<SyntaxInfo<? extends Effect>> EFFECT = key("effect");
		Key<DefaultSyntaxInfos.Event<?>> EVENT = key("event");
		Key<DefaultSyntaxInfos.Expression<?, ?>> EXPRESSION = key("expression");
		Key<SyntaxInfo<? extends Section>> SECTION = key("section");
		Key<SyntaxInfo<? extends Statement>> STATEMENT = key("statement");
		Key<DefaultSyntaxInfos.Structure<?>> STRUCTURE = key("structure");
		
		static <T extends SyntaxInfo<?>> Key<T> key(String key) {
			//noinspection EqualsWhichDoesntCheckParameterClass
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
					return key.equals(obj);
				}
			};
		}
		
	}
	
}

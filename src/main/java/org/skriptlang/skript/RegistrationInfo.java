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

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SyntaxElement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

@ApiStatus.Experimental
public interface RegistrationInfo<T extends SyntaxElement> {
	
	Class<T> type();
	
	@Unmodifiable
	List<String> patterns();
	
	interface ExpressionRegistrationInfo<T> extends RegistrationInfo<Expression<T>> {
		@Nullable
		Class<T> returnType();
	}
	
	interface Builder<T extends SyntaxElement> {
		
		@Contract("_ -> new")
		static <T extends SyntaxElement> Builder<T> of(Class<T> type) {
			return new RegistrationInfoImpl.BuilderImpl<>(type);
		}
		
		@Contract("_ -> this")
		Builder<T> addPattern(String pattern);
		
		@Contract("_ -> this")
		default Builder<T> addPatterns(Iterable<String> patterns) {
			for (String pattern : patterns)
				addPattern(pattern);
			
			return this;
		}
		
		@Contract("_ -> this")
		default Builder<T> addPatterns(String... patterns) {
			for (String pattern : patterns)
				addPattern(pattern);
			
			return this;
		}
		
		@Contract("-> new")
		RegistrationInfo<T> build();
		
	}
	
}

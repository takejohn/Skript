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
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

final class RegistrationInfoImpl<T extends SyntaxElement> implements RegistrationInfo<T> {
	
	private final Class<T> type;
	private final List<String> patterns;
	
	RegistrationInfoImpl(Class<T> type, List<String> patterns) {
		this.type = type;
		this.patterns = ImmutableList.copyOf(patterns);
	}
	
	@Override
	public Class<T> type() {
		return type;
	}
	
	@Override
	@Unmodifiable
	public List<String> patterns() {
		return patterns;
	}
	
	static final class BuilderImpl<T extends SyntaxElement> implements RegistrationInfo.Builder<T> {
		
		private final Class<T> type;
		private final List<String> patterns = new ArrayList<>();
		
		BuilderImpl(Class<T> type) {
			this.type = type;
		}
		
		@Override
		public Builder<T> addPattern(String pattern) {
			patterns.add(pattern);
			return this;
		}
		
		@Override
		public RegistrationInfo<T> build() {
			return new RegistrationInfoImpl<>(type, patterns);
		}
		
	}
	
}
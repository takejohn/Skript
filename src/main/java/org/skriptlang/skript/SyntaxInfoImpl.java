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

import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SyntaxElement;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.util.List;

class SyntaxInfoImpl<T extends SyntaxElement> implements SyntaxInfo<T> {
	
	private final SyntaxOrigin origin;
	private final Class<T> type;
	private final List<String> patterns;
	
	SyntaxInfoImpl(SyntaxOrigin origin, Class<T> type, List<String> patterns) {
		this.origin = origin;
		this.type = type;
		this.patterns = ImmutableList.copyOf(patterns);
	}
	
	@Override
	public SyntaxOrigin origin() {
		return origin;
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
	
	static final class EventImpl<E extends SkriptEvent> extends StructureImpl<E> implements SyntaxInfo.Event<E> {
		
		private final String name;
		private final List<Class<? extends org.bukkit.event.Event>> events;
		
		EventImpl(SyntaxOrigin origin, Class<E> type, List<String> patterns,
		               String name, List<Class<? extends org.bukkit.event.Event>> events) {
			
			super(origin, type, patterns, null);
			this.name = name;
			this.events = ImmutableList.copyOf(events);
		}
		
		@Override
		public String name() {
			return name;
		}
		
		@Override
		public List<Class<? extends org.bukkit.event.Event>> events() {
			return events;
		}
		
	}
	
	static final class ExpressionImpl<E extends ch.njol.skript.lang.Expression<R>, R> extends SyntaxInfoImpl<E> implements SyntaxInfo.Expression<E, R> {
		
		private final Class<R> returnType;
		private final ExpressionType expressionType;
		
		ExpressionImpl(SyntaxOrigin origin, Class<E> type, List<String> patterns,
		               Class<R> returnType, ExpressionType expressionType) {
			
			super(origin, type, patterns);
			this.returnType = returnType;
			this.expressionType = expressionType;
		}
		
		@Override
		public Class<R> returnType() {
			return returnType;
		}
		
		@Override
		public ExpressionType expressionType() {
			return expressionType;
		}
		
	}
	
	static class StructureImpl<E extends org.skriptlang.skript.lang.structure.Structure> extends SyntaxInfoImpl<E> implements SyntaxInfo.Structure<E> {
		
		@Nullable
		private final EntryValidator entryValidator;
		
		StructureImpl(SyntaxOrigin origin, Class<E> type, List<String> patterns,
		               @Nullable EntryValidator entryValidator) {
			
			super(origin, type, patterns);
			this.entryValidator = entryValidator;
		}
		
		@Override
		@Nullable
		public EntryValidator entryValidator() {
			return entryValidator;
		}
		
	}
	
}

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

import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SyntaxElement;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ApiStatus.Experimental
public final class SyntaxInfoBuilder<E extends SyntaxElement> {

	public static <E extends SyntaxElement> SyntaxInfoBuilder<E> builder(Class<E> type) {
		return new SyntaxInfoBuilder<>(type);
	}

	private final Class<E> type;
	private final List<String> patterns = new ArrayList<>();
	@Nullable
	private Supplier<E> supplier;
	private SyntaxOrigin origin = SyntaxOrigin.UNKNOWN;

	private SyntaxInfoBuilder(Class<E> type) {
		this.type = type;
	}

	@Contract("_ -> this")
	public SyntaxInfoBuilder<E> origin(SyntaxOrigin origin) {
		this.origin = origin;
		return this;
	}

	@Contract("_ -> this")
	public SyntaxInfoBuilder<E> supplier(Supplier<E> supplier) {
		this.supplier = supplier;
		return this;
	}

	@Contract("_ -> this")
	public SyntaxInfoBuilder<E> addPattern(String pattern) {
		patterns.add(pattern);
		return this;
	}

	@Contract("_ -> this")
	public SyntaxInfoBuilder<E> addPatterns(String... patterns) {
		for (String pattern : patterns)
			addPattern(pattern);
		return this;
	}

	@Contract("_ -> this")
	public SyntaxInfoBuilder<E> addPatterns(List<String> patterns) {
		for (String pattern : patterns)
			addPattern(pattern);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <R> Expression<ch.njol.skript.lang.Expression<R>, R> expression(Class<R> returnType) {
		return new Expression<>(origin, (Class<ch.njol.skript.lang.Expression<R>>) type,
				(Supplier<ch.njol.skript.lang.Expression<R>>) supplier, patterns, returnType);
	}

	@SuppressWarnings("unchecked")
	public Structure<org.skriptlang.skript.lang.structure.Structure> structure() {
		return new Structure<>(origin, (Class<org.skriptlang.skript.lang.structure.Structure>) type,
				(Supplier<org.skriptlang.skript.lang.structure.Structure>) supplier, patterns);
	}

	@Contract("-> new")
	public SyntaxInfo<E> build() {
		return SyntaxInfo.of(origin, type, supplier, patterns);
	}

	public static final class Expression<E extends ch.njol.skript.lang.Expression<R>, R> {

		private final SyntaxOrigin origin;
		private final Class<E> type;
		@Nullable
		private final Supplier<E> supplier;
		private final List<String> patterns;
		private final Class<R> returnType;
		@Nullable
		private ExpressionType expressionType;

		private Expression(SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier, List<String> patterns, Class<R> returnType) {
			this.origin = origin;
			this.type = type;
			this.supplier = supplier;
			this.patterns = ImmutableList.copyOf(patterns);
			this.returnType = returnType;
		}

		@Contract("_ -> this")
		public Expression<E, R> expressionType(ExpressionType expressionType) {
			this.expressionType = expressionType;
			return this;
		}

		public SyntaxInfo.Expression<E, R> build() {
			if (expressionType == null)
				throw new NullPointerException("expressionType is not set");
			return SyntaxInfo.Expression.of(origin, type, supplier, patterns, returnType, expressionType);
		}

	}

	public static final class Structure<E extends org.skriptlang.skript.lang.structure.Structure> {

		private final SyntaxOrigin origin;
		private final Class<E> type;
		@Nullable
		private final Supplier<E> supplier;
		private final List<String> patterns;
		@Nullable
		private EntryValidator entryValidator;

		private Structure(SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier, List<String> patterns) {
			this.origin = origin;
			this.type = type;
			this.supplier = supplier;
			this.patterns = ImmutableList.copyOf(patterns);
		}

		@Contract("_ -> this")
		public Structure<E> entryValidator(EntryValidator entryValidator) {
			this.entryValidator = entryValidator;
			return this;
		}

		public SyntaxInfo.Structure<E> build() {
			return SyntaxInfo.Structure.of(origin, type, supplier, patterns, entryValidator);
		}

	}

}

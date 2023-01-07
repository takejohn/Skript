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

@ApiStatus.Experimental
public final class SyntaxInfoBuilder<E extends SyntaxElement> {
	
	public static <E extends SyntaxElement> SyntaxInfoBuilder<E> builder(Class<E> type) {
		return new SyntaxInfoBuilder<>(type);
	}
	
	private final Class<E> type;
	private final List<String> patterns = new ArrayList<>();
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
		return new Expression<>(origin, (Class<ch.njol.skript.lang.Expression<R>>) type, patterns);
	}
	
	@SuppressWarnings("unchecked")
	public Structure<org.skriptlang.skript.lang.structure.Structure> structure() {
		return new Structure<>(origin, (Class<org.skriptlang.skript.lang.structure.Structure>) type, patterns);
	}
	
	@Contract("-> new")
	public SyntaxInfo<E> build() {
		return SyntaxInfo.of(origin, type, patterns);
	}
	
	public static final class Expression<E extends ch.njol.skript.lang.Expression<R>, R> {
		
		private final SyntaxOrigin origin;
		private final Class<E> type;
		private final List<String> patterns;
		@Nullable
		private Class<R> returnType;
		@Nullable
		private ExpressionType expressionType;
		
		private Expression(SyntaxOrigin origin, Class<E> type, List<String> patterns) {
			this.origin = origin;
			this.type = type;
			this.patterns = ImmutableList.copyOf(patterns);
		}
		
		@Contract("_ -> this")
		public Expression<E, R> origin(Class<R> returnType) {
			this.returnType = returnType;
			return this;
		}
		
		@Contract("_ -> this")
		public Expression<E, R> origin(ExpressionType expressionType) {
			this.expressionType = expressionType;
			return this;
		}
		
		private SyntaxInfo.Expression<E, R> build() {
			if (returnType == null)
				throw new NullPointerException("returnType is not set");
			if (expressionType == null)
				throw new NullPointerException("expressionType is not set");
			return SyntaxInfo.Expression.of(origin, type, patterns, returnType, expressionType);
		}
		
	}
	
	public static final class Structure<E extends org.skriptlang.skript.lang.structure.Structure> {
		
		private final SyntaxOrigin origin;
		private final Class<E> type;
		private final List<String> patterns;
		@Nullable
		private EntryValidator entryValidator;
		
		private Structure(SyntaxOrigin origin, Class<E> type, List<String> patterns) {
			this.origin = origin;
			this.type = type;
			this.patterns = ImmutableList.copyOf(patterns);
		}
		
		@Contract("_ -> this")
		public Structure<E> entryValidator(EntryValidator entryValidator) {
			this.entryValidator = entryValidator;
			return this;
		}
		
		private SyntaxInfo.Structure<E> build() {
			return SyntaxInfo.Structure.of(origin, type, patterns, entryValidator);
		}
		
	}
	
}

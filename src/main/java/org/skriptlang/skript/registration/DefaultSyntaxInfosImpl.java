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
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.Priority;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.util.List;
import java.util.function.Supplier;

@ApiStatus.Experimental
@ApiStatus.Internal
public final class DefaultSyntaxInfosImpl implements DefaultSyntaxInfos {

	@ApiStatus.Experimental
	@ApiStatus.Internal
	public static class ExpressionImpl<E extends ch.njol.skript.lang.Expression<R>, R>
		extends SyntaxInfoImpl<E> implements DefaultSyntaxInfos.Expression<E, R> {

		private final Class<R> returnType;
		private final ExpressionType expressionType;

		protected ExpressionImpl(
			SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier,
			List<String> patterns, Class<R> returnType, ExpressionType expressionType
		) {
			super(origin, type, supplier, patterns);
			if (returnType.isAnnotation() || returnType.isArray() || returnType.isPrimitive())
				throw new IllegalArgumentException("returnType must be a normal type");
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

		@Override
		public boolean equals(Object other) {
			if (this == other)
				return true;
			if (!(other instanceof Expression))
				return false;
			ExpressionImpl<?, ?> expression = (ExpressionImpl<?, ?>) other;
			return origin().equals(expression.origin()) && type().equals(expression.type()) &&
					patterns().equals(expression.patterns()) && returnType() == expression.returnType() &&
					expressionType().equals(expression.expressionType());
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(origin(), type(), patterns(), returnType(), expressionType());
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
					.add("origin", origin())
					.add("type", type())
					.add("patterns", patterns())
					.add("returnType", returnType())
					.add("expressionType", expressionType())
					.toString();
		}

		@Override
		public Priority priority() {
			return new Priority(super.priority().getPriority() | expressionType.ordinal() << 24);
		}

		static final class BuilderImpl<E extends ch.njol.skript.lang.Expression<R>, R>
			extends SyntaxInfoImpl.BuilderImpl<Expression.Builder<E, R>, E>
			implements Expression.Builder<E, R> {

			private final Class<R> returnType;
			@Nullable
			private ExpressionType expressionType;

			BuilderImpl(Class<E> expressionClass, Class<R> returnType) {
				super(expressionClass);
				this.returnType = returnType;
			}

			@Override
			public Expression.Builder<E, R> expressionType(ExpressionType expressionType) {
				this.expressionType = expressionType;
				return this;
			}

			@Contract("-> new")
			public Expression<E, R> build() {
				if (expressionType == null) {
					throw new NullPointerException("expressionType must be set");
				}
				return new ExpressionImpl<>(origin, type, supplier, patterns, returnType, expressionType);
			}
		}

	}

	@ApiStatus.Experimental
	@ApiStatus.Internal
	public static class StructureImpl<E extends org.skriptlang.skript.lang.structure.Structure>
		extends SyntaxInfoImpl<E> implements DefaultSyntaxInfos.Structure<E> {

		@Nullable
		private final EntryValidator entryValidator;

		protected StructureImpl(
			SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier,
			List<String> patterns, @Nullable EntryValidator entryValidator
		) {
			super(origin, type, supplier, patterns);
			this.entryValidator = entryValidator;
		}

		@Override
		@Nullable
		public EntryValidator entryValidator() {
			return entryValidator;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other)
				return true;
			if (!(other instanceof Structure))
				return false;
			Structure<?> structure = (Structure<?>) other;
			return origin().equals(structure.origin()) && type().equals(structure.type()) &&
					patterns().equals(structure.patterns()) &&
					Objects.equal(entryValidator(), structure.entryValidator());
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(origin(), type(), patterns(), entryValidator());
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
					.add("origin", origin())
					.add("type", type())
					.add("patterns", patterns())
					.add("entryValidator", entryValidator())
					.toString();
		}

		static final class BuilderImpl<E extends org.skriptlang.skript.lang.structure.Structure>
			extends SyntaxInfoImpl.BuilderImpl<Structure.Builder<E>, E>
			implements Structure.Builder<E> {

			@Nullable
			private EntryValidator entryValidator;

			BuilderImpl(Class<E> structureClass) {
				super(structureClass);
			}

			@Override
			public Structure.Builder<E> entryValidator(EntryValidator entryValidator) {
				this.entryValidator = entryValidator;
				return this;
			}

			public Structure<E> build() {
				return new StructureImpl<>(origin, type, supplier, patterns, entryValidator);
			}
		}

	}

}

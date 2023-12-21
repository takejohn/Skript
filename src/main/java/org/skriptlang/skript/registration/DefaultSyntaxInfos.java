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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.registration.DefaultSyntaxInfosImpl.ExpressionImpl;
import org.skriptlang.skript.registration.DefaultSyntaxInfosImpl.StructureImpl;

import java.util.Collection;
import java.util.function.Supplier;

@ApiStatus.Experimental
interface DefaultSyntaxInfos {

	interface Expression<E extends ch.njol.skript.lang.Expression<R>, R> extends SyntaxInfo<E> {

		@Contract("_, _ -> new")
		static <E extends ch.njol.skript.lang.Expression<R>, R> Builder<? extends Builder<?, E, R>, E, R> builder(Class<E> expressionClass, Class<R> returnType) {
			return new ExpressionImpl.BuilderImpl<>(expressionClass, returnType);
		}

		// TODO consider forcing builder usage
		@Contract("_, _, _, _, _, _ -> new")
		static <E extends ch.njol.skript.lang.Expression<R>, R> Expression<E, R> of(
			SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier, Collection<String> patterns,
			Class<R> returnType, ExpressionType expressionType
		) {
			return new ExpressionImpl<>(origin, type, supplier, patterns, returnType, expressionType);
		}

		Class<R> returnType();

		ExpressionType expressionType();

		interface Builder<B extends Builder<B, E, R>, E extends ch.njol.skript.lang.Expression<R>, R> extends SyntaxInfo.Builder<B, E> {

			@Contract("_ -> this")
			B expressionType(ExpressionType expressionType);

			@Override
			@Contract("-> new")
			Expression<E, R> build();

		}

	}

	interface Structure<E extends org.skriptlang.skript.lang.structure.Structure> extends SyntaxInfo<E> {

		@Contract("_ -> new")
		static <E extends org.skriptlang.skript.lang.structure.Structure> Builder<? extends Builder<?, E>, E> builder(Class<E> structureClass) {
			return new StructureImpl.BuilderImpl<>(structureClass);
		}

		// TODO consider forcing builder usage
		@Contract("_, _, _, _ -> new")
		static <E extends org.skriptlang.skript.lang.structure.Structure> Structure<E> of(
			SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier, Collection<String> patterns
		) {
			return of(origin, type, supplier, patterns, null);
		}

		// TODO consider forcing builder usage
		@Contract("_, _, _, _, _ -> new")
		static <E extends org.skriptlang.skript.lang.structure.Structure> Structure<E> of(
			SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier,
			Collection<String> patterns, @Nullable EntryValidator entryValidator
		) {
			
			return new StructureImpl<>(origin, type, supplier, patterns, entryValidator);
		}

		@Nullable
		EntryValidator entryValidator();

		interface Builder<B extends Builder<B, E>, E extends org.skriptlang.skript.lang.structure.Structure> extends SyntaxInfo.Builder<B, E> {

			@Contract("_ -> this")
			B entryValidator(EntryValidator entryValidator);

			@Override
			@Contract("-> new")
			Structure<E> build();

		}

	}

}

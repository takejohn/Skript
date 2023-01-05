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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.List;

interface DefaultSyntaxInfos {
	
	@ApiStatus.NonExtendable
	interface Expression<E extends ch.njol.skript.lang.Expression<R>, R> extends SyntaxInfo<E> {
		
		@Contract("_, _, _, _, _ -> new")
		static <E extends ch.njol.skript.lang.Expression<R>, R> Expression<E, R> of(SyntaxOrigin origin, Class<E> type, List<String> patterns,
		                            Class<R> returnType, ExpressionType expressionType) {
			
			return new SyntaxInfoImpl.ExpressionImpl<>(origin, type, patterns, returnType, expressionType);
		}
		
		Class<R> returnType();
		
		ExpressionType expressionType();
		
	}
	
}

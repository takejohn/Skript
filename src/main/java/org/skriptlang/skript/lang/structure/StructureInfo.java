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
package org.skriptlang.skript.lang.structure;

import ch.njol.skript.lang.SyntaxElementInfo;
import org.eclipse.jdt.annotation.Nullable;
import org.skriptlang.skript.lang.entry.EntryValidator;

/**
 * Special {@link SyntaxElementInfo} for {@link Structure}s that may contain information such as the {@link EntryValidator}.
 */
public class StructureInfo<E extends Structure> extends SyntaxElementInfo<E> {

	@Nullable
	public final EntryValidator entryValidator;

	public StructureInfo(String[] patterns, Class<E> elementClass, String originClassPath) throws IllegalArgumentException {
		super(patterns, elementClass, originClassPath);
		entryValidator = null;
	}

	public StructureInfo(
		String[] patterns, Class<E> elementClass, String originClassPath,
		@Nullable EntryValidator entryValidator
	) throws IllegalArgumentException {
		super(patterns, elementClass, originClassPath);
		this.entryValidator = entryValidator;
	}

}

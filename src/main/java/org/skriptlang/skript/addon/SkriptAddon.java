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
package org.skriptlang.skript.addon;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * A Skript addon is an extension to Skript that expands its features.
 *
 */
@ApiStatus.Experimental
public interface SkriptAddon {

	/**
	 * @return The name of this addon.
	 */
	String name();

	/**
	 * The path to the directory on disk containing data files for this addon.
	 *  For example, this may include language files that have been saved to enable user customization.
	 * @return A string representing the path to the directory.
	 *  Null if this addon does not store any data files.
	 */
	@Nullable
	default String dataFileDirectory() {
		return null;
	}

	/**
	 * The path to the directory containing language files for this addon.
	 * When searching for language files on the jar, this will be used as the path.
	 * When searching for language files on the disk, this will be used along with {@link #dataFileDirectory()}.
	 * That is, it is expected that the path <code>dataFileDirectory() + languageFileDirectory()</code> would
	 *  lead to this addon's language files on the disk.
	 * @return A string representing the path to the directory.
	 *  Null if this addon does not have any language files.
	 */
	@Nullable
	default String languageFileDirectory() {
		return null;
	}

}

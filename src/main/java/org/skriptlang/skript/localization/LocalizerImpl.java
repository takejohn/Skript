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
package org.skriptlang.skript.localization;

import org.jetbrains.annotations.Nullable;

final class LocalizerImpl implements Localizer {

	@Nullable
	private final String languageFileDirectory;
	@Nullable
	private final String dataFileDirectory;

	LocalizerImpl(@Nullable String languageFileDirectory, @Nullable String dataFileDirectory) {
		this.languageFileDirectory = languageFileDirectory;
		this.dataFileDirectory = dataFileDirectory;
	}

	@Override
	@Nullable
	public String languageFileDirectory() {
		return languageFileDirectory;
	}

	@Override
	@Nullable
	public String dataFileDirectory() {
		return dataFileDirectory;
	}

}

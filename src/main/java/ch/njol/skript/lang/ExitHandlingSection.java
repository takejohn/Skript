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
package ch.njol.skript.lang;

import org.bukkit.event.Event;

/**
 * A {@link Section} implementing this interface can execute a task when
 * it is exited by an {@link ch.njol.skript.effects.EffExit 'exit'} or
 * {@link ch.njol.skript.effects.EffReturn 'return'} effect.
 */
public interface ExitHandlingSection {

	/**
	 * Exits the section
	 * @param event The involved event
	 */
	void exit(Event event);

}

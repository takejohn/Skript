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
package org.skriptlang.skript.bukkit.registration;

import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptEventInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxOrigin;

import java.util.List;
import java.util.function.Supplier;

@ApiStatus.Internal
public interface BukkitSyntaxInfos {

	String EVENT_PRIORITY_SYNTAX = " [with priority (lowest|low|normal|high|highest|monitor)]";
	
	@ApiStatus.NonExtendable
	interface Event<E extends SkriptEvent> extends SyntaxInfo.Structure<E> {
		
		@Contract("_, _, _, _, _, _, _, _, _, _, _, _, _ -> new")
		static <E extends SkriptEvent> BukkitSyntaxInfos.Event<E> of(
				SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier, List<String> patterns, String name, String id,
				@Nullable String since, @Nullable String documentationId, List<String> description, List<String> examples,
				List<String> keywords, List<String> requiredPlugins, List<Class<? extends org.bukkit.event.Event>> events
		) {
			return new BukkitSyntaxInfosImpl.EventImpl<>(origin, type, supplier, patterns, name, id,
					since, documentationId, description, examples, keywords, requiredPlugins, events);
		}
		
		@Contract("_ -> new")
		static <E extends SkriptEvent> BukkitSyntaxInfos.Event<E> legacy(SkriptEventInfo<E> info) {
			return new BukkitSyntaxInfosImpl.LegacyEventImpl<>(info);
		}
		
		String name();
		
		String id();
		
		@Nullable
		String since();
		
		@Nullable
		String documentationId();
		
		List<String> description();
		
		List<String> examples();
		
		List<String> keywords();
		
		List<String> requiredPlugins();
		
		List<Class<? extends org.bukkit.event.Event>> events();
		
	}

	/**
	 * Fixes a pattern for events. Adds {@literal on} as prefix and {@literal with priority ...} as suffix.
	 * Make sure to only use this on a pattern once at most.
	 *
	 * @param pattern The pattern to make usable as event
	 * @return The event-ready pattern
	 */
	static String eventPattern(String pattern) {
		return "[on] " + fixPattern(pattern) + EVENT_PRIORITY_SYNTAX;
	}

	/**
	 * Fixes patterns in event by modifying every {@link ch.njol.skript.patterns.TypePatternElement}
	 * to be nullable.
	 */
	static String fixPattern(String pattern) {
		char[] chars = pattern.toCharArray();
		StringBuilder stringBuilder = new StringBuilder();

		boolean inType = false;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			stringBuilder.append(c);

			if (c == '%') {
				// toggle inType
				inType = !inType;

				// add the dash character if it's not already present
				// a type specification can have two prefix characters for modification
				if (inType && i + 2 < chars.length && chars[i + 1] != '-' && chars[i + 2] != '-')
					stringBuilder.append('-');
			} else if (c == '\\' && i + 1 < chars.length) {
				// Make sure we don't toggle inType for escape percentage signs
				stringBuilder.append(chars[i + 1]);
				i++;
			}
		}
		return stringBuilder.toString();
	}
	
}

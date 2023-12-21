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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitInfosImpl.EventImpl;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxOrigin;

import java.util.Collection;
import java.util.function.Supplier;

public interface BukkitInfos {

	interface Event<E extends SkriptEvent> extends SyntaxInfo.Structure<E> {

		static <E extends SkriptEvent> Builder<? extends Builder<?, E>, E> builder(
			Class<E> eventClass, String name, String id
		) {
			return new EventImpl.BuilderImpl<>(eventClass, name, id);
		}

		@Contract("_, _, _, _, _, _, _, _, _, _, _, _, _ -> new")
		static <E extends SkriptEvent> BukkitInfos.Event<E> of(
				SyntaxOrigin origin, Class<E> type, @Nullable Supplier<E> supplier, Collection<String> patterns, String name, String id,
				@Nullable String since, @Nullable String documentationId, Collection<String> description, Collection<String> examples,
				Collection<String> keywords, Collection<String> requiredPlugins, Collection<Class<? extends org.bukkit.event.Event>> events
		) {
			return new EventImpl<>(
					SyntaxInfo.of(origin, type, supplier, patterns), name, id,
					since, documentationId, description, examples, keywords, requiredPlugins, events
			);
		}

		String name();

		String id();

		@Nullable
		String since();

		@Nullable
		String documentationId();

		Collection<String> description();

		Collection<String> examples();

		Collection<String> keywords();

		Collection<String> requiredPlugins();

		Collection<Class<? extends org.bukkit.event.Event>> events();

		interface Builder<B extends Builder<B, E>, E extends SkriptEvent> extends SyntaxInfo.Structure.Builder<B, E> {

			@Contract("_ -> this")
			B since(String since);

			@Contract("_ -> this")
			B documentationId(String documentationId);

			@Contract("_ -> this")
			B addDescription(String description);

			@Contract("_ -> this")
			B addDescription(String... description);

			@Contract("_ -> this")
			B addDescription(Collection<String> description);

			@Contract("_ -> this")
			B addExample(String example);

			@Contract("_ -> this")
			B addExamples(String... examples);

			@Contract("_ -> this")
			B addExamples(Collection<String> examples);

			@Contract("_ -> this")
			B addKeyword(String keyword);

			@Contract("_ -> this")
			B addKeywords(String... keywords);

			@Contract("_ -> this")
			B addKeywords(Collection<String> keywords);

			@Contract("_ -> this")
			B addRequiredPlugin(String requiredPlugin);

			@Contract("_ -> this")
			B addRequiredPlugins(String... requiredPlugins);

			@Contract("_ -> this")
			B addRequiredPlugins(Collection<String> requiredPlugins);

			@Contract("_ -> this")
			B addEvent(Class<? extends org.bukkit.event.Event> event);

			@Contract("_ -> this")
			B addEvents(Class<? extends org.bukkit.event.Event>... events);

			@Contract("_ -> this")
			B addEvents(Collection<Class<? extends org.bukkit.event.Event>> events);

			@Override
			@Contract("_ -> fail")
			B entryValidator(EntryValidator entryValidator);

			@Override
			@Contract("-> new")
			Event<E> build();

		}

	}

	/**
	 * Fixes patterns in event by modifying every {@link ch.njol.skript.patterns.TypePatternElement} to be nullable.
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

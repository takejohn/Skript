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
import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.skriptlang.skript.registration.SyntaxOrigin;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Experimental
public final class EventInfoBuilder<E extends SkriptEvent> {
	
	public static <E extends SkriptEvent> EventInfoBuilder<E> builder(Class<E> type, String name) {
		return new EventInfoBuilder<>(type, name);
	}
	
	private final Class<E> type;
	private final String name;
	private final List<String> patterns = new ArrayList<>();
	private final List<Class<? extends Event>> events = new ArrayList<>();
	private SyntaxOrigin origin = SyntaxOrigin.UNKNOWN;
	
	private EventInfoBuilder(Class<E> type, String name) {
		this.type = type;
		this.name = name;
	}
	
	@Contract("_ -> this")
	public EventInfoBuilder<E> origin(SyntaxOrigin origin) {
		this.origin = origin;
		return this;
	}
	
	@Contract("_ -> this")
	public EventInfoBuilder<E> addPattern(String pattern) {
		patterns.add(pattern);
		return this;
	}
	
	@Contract("_ -> this")
	public EventInfoBuilder<E> addPatterns(String... patterns) {
		for (String pattern : patterns)
			addPattern(pattern);
		return this;
	}
	
	@Contract("_ -> this")
	public EventInfoBuilder<E> addPatterns(List<String> patterns) {
		for (String pattern : patterns)
			addPattern(pattern);
		return this;
	}
	
	@Contract("_ -> this")
	public EventInfoBuilder<E> addEvent(Class<? extends Event> event) {
		events.add(event);
		return this;
	}
	
	@Contract("_ -> this")
	public EventInfoBuilder<E> addEvents(Class<? extends Event>... events) {
		for (Class<? extends Event> event : events)
			addEvent(event);
		return this;
	}
	
	@Contract("_ -> this")
	public EventInfoBuilder<E> addEvents(List<Class<? extends Event>> events) {
		for (Class<? extends Event> event : events)
			addEvent(event);
		return this;
	}
	
	@Contract("-> new")
	public BukkitSyntaxInfos.Event<E> build() {
		return BukkitSyntaxInfos.Event.of(origin, name, type, patterns, events);
	}
	
}

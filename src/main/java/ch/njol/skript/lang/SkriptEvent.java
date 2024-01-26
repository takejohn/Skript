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

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.SkriptConfig;
import ch.njol.skript.SkriptEventHandler;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.events.EvtClick;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.structures.StructEvent.EventData;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.eclipse.jdt.annotation.Nullable;
import org.skriptlang.skript.bukkit.registration.BukkitInfos;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.script.Script;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.List;
import java.util.Locale;

/**
 * A SkriptEvent is like a condition. It is called when any of the registered events occurs.
 * An instance of this class should then check whether the event applies
 * (e.g. the rightclick event is included in the PlayerInteractEvent which also includes lefclicks, thus the SkriptEvent {@link EvtClick} checks whether it was a rightclick or
 * not).<br/>
 * It is also needed if the event has parameters.
 *
 * @see Skript#registerEvent(String, Class, Class, String...)
 * @see Skript#registerEvent(String, Class, Class[], String...)
 */
@SuppressWarnings("NotNullFieldNotInitialized")
public abstract class SkriptEvent extends Structure {

	public static final Priority PRIORITY = new Priority(600);

	private String expr;
	@Nullable
	protected EventPriority eventPriority;
	private SkriptEventInfo<?> skriptEventInfo;

	/**
	 * The Trigger containing this SkriptEvent's code.
	 */
	protected Trigger trigger;

	@Override
	public final boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult, EntryContainer entryContainer) {
		this.expr = parseResult.expr;

		EventPriority priority = getParser().getData(EventData.class).getPriority();
		if (priority != null && !isEventPrioritySupported()) {
			Skript.error("This event doesn't support event priority");
			return false;
		}
		eventPriority = priority;

		SyntaxElementInfo<? extends Structure> syntaxElementInfo = getParser().getData(StructureData.class).getStructureInfo();
		if (!(syntaxElementInfo instanceof SkriptEventInfo))
			throw new IllegalStateException();
		skriptEventInfo = (SkriptEventInfo<?>) syntaxElementInfo;

		return init(args, matchedPattern, parseResult);
	}

	/**
	 * Called just after the constructor
	 */
	public abstract boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult);

	/**
	 * This method handles the loading of the Structure's syntax elements.
	 * Only override this method if you know what you are doing!
	 */
	@Override
	public boolean preLoad() {
		// Implemented just for javadoc
		return super.preLoad();
	}

	/**
	 * This method handles the loading of the Structure's syntax elements.
	 * Only override this method if you know what you are doing!
	 */
	@Override
	public boolean load() {
		if (!shouldLoadEvent())
			return false;

		SectionNode source = getEntryContainer().getSource();
		if (Skript.debug() || source.debug())
			Skript.debug(expr + " (" + this + "):");

		Class<? extends Event>[] eventClasses = getEventClasses();

		try {
			getParser().setCurrentEvent(skriptEventInfo.getName().toLowerCase(Locale.ENGLISH), eventClasses);

			@Nullable List<TriggerItem> items = ScriptLoader.loadItems(source);
			Script script = getParser().getCurrentScript();

			trigger = new Trigger(script, expr, this, items);
			int lineNumber = getEntryContainer().getSource().getLine();
			trigger.setLineNumber(lineNumber); // Set line number for debugging
			trigger.setDebugLabel(script + ": line " + lineNumber);
		} finally {
			getParser().deleteCurrentEvent();
		}

		return true;
	}

	/**
	 * This method handles the registration of this event with Skript and Bukkit.
	 * Only override this method if you know what you are doing!
	 */
	@Override
	public boolean postLoad() {
		SkriptEventHandler.registerBukkitEvents(trigger, getEventClasses());
		return true;
	}

	/**
	 * This method handles the unregistration of this event with Skript and Bukkit.
	 * Only override this method if you know what you are doing!
	 */
	@Override
	public void unload() {
		SkriptEventHandler.unregisterBukkitEvents(trigger);
	}

	/**
	 * This method handles the unregistration of this event with Skript and Bukkit.
	 * Only override this method if you know what you are doing!
	 */
	@Override
	public void postUnload() {
		// Implemented just for javadoc
		super.postUnload();
	}

	@Override
	public Priority getPriority() {
		return PRIORITY;
	}

	/**
	 * Checks whether the given Event applies, e.g. the left-click event is only part of the PlayerInteractEvent, and this checks whether the player left-clicked or not. This method
	 * will only be called for events this SkriptEvent is registered for.
	 * @return true if this is SkriptEvent is represented by the Bukkit Event or false if not
	 */
	public abstract boolean check(Event event);

	/**
	 * Script loader checks this before loading items in event. If false is
	 * returned, they are not parsed and the event is not registered.
	 * @return If this event should be loaded.
	 */
	public boolean shouldLoadEvent() {
		return true;
	}

	/**
	 * @return the Event classes to use in {@link ch.njol.skript.lang.parser.ParserInstance}.
	 */
	public Class<? extends Event>[] getEventClasses() {
		return skriptEventInfo.events;
	}

	/**
	 * @return the {@link EventPriority} to be used for this event.
	 * Defined by the user-specified priority, or otherwise the default event priority.
	 */
	public EventPriority getEventPriority() {
		return eventPriority != null ? eventPriority : SkriptConfig.defaultEventPriority.value();
	}

	/**
	 * @return whether this SkriptEvent supports event priorities
	 */
	public boolean isEventPrioritySupported() {
		return true;
	}

	/**
	 * Override this method to allow Skript to not force synchronization.
	 */
	public boolean canExecuteAsynchronously() {
		return false;
	}

	/**
	 * Fixes patterns in event by modifying every {@link ch.njol.skript.patterns.TypePatternElement}
	 * to be nullable.
	 * 
	 * @deprecated Use {@link BukkitInfos#fixPattern(String)}
	 */
	@Deprecated
	public static String fixPattern(String pattern) {
		return BukkitInfos.fixPattern(pattern);
	}

	@Nullable
	public static SkriptEvent parse(String expr, SectionNode sectionNode, @Nullable String defaultError) {
		return (SkriptEvent) Structure.parse(expr, sectionNode, defaultError, Skript.getEvents().iterator());
	}

}

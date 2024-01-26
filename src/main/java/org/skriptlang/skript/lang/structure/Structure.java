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

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Debuggable;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxElement;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.iterator.ConsumingIterator;
import org.eclipse.jdt.annotation.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryData;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Structures are the root elements in every script. They are essentially the "headers".
 * Events and functions are both a type of Structure. However, each one has its own
 *  parsing requirements, order, and defined structure within.
 *
 * Structures may also contain "entries" that hold values or sections of code.
 * The values of these entries can be obtained by parsing the Structure's sub{@link Node}s
 *  through registered {@link EntryData}.
 */
public abstract class Structure implements SyntaxElement, Debuggable {

	/**
	 * The default {@link Priority} of every registered Structure.
	 *
	 * @deprecated Use {@link org.skriptlang.skript.lang.Priority#DEFAULT_PRIORITY}
	 */
	@Deprecated
	public static final org.skriptlang.skript.lang.Priority DEFAULT_PRIORITY = org.skriptlang.skript.lang.Priority.DEFAULT_PRIORITY;

	/**
	 * Priorities are used to determine the order in which Structures should be loaded.
	 * As the priority approaches 0, it becomes more important. Example:
	 * priority of 1 (loads first), priority of 2 (loads second), priority of 3 (loads third)
	 *
	 * @deprecated Use {@link org.skriptlang.skript.lang.Priority}
	 */
	@Deprecated
	public static class Priority extends org.skriptlang.skript.lang.Priority {
		public Priority(int priority) {
			super(priority);
		}
	}

	@Nullable
	private EntryContainer entryContainer = null;

	/**
	 * @return An EntryContainer containing this Structure's {@link EntryData} and {@link Node} parse results.
	 * Please note that this Structure <b>MUST</b> have been initialized for this to work.
	 */
	public final EntryContainer getEntryContainer() {
		if (entryContainer == null)
			throw new IllegalStateException("This Structure hasn't been initialized!");
		return entryContainer;
	}

	@Override
	public final boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		StructureData structureData = getParser().getData(StructureData.class);

		Literal<?>[] literals = Arrays.copyOf(expressions, expressions.length, Literal[].class);

		StructureInfo<? extends Structure> structureInfo = structureData.structureInfo;
		assert structureInfo != null;
		EntryValidator entryValidator = structureInfo.entryValidator;

		if (entryValidator == null) { // No validation necessary, the structure itself will handle it
			entryContainer = EntryContainer.withoutValidator(structureData.sectionNode);
		} else { // Okay, now it's time for validation
			EntryContainer entryContainer = entryValidator.validate(structureData.sectionNode);
			if (entryContainer == null)
				return false;
			this.entryContainer = entryContainer;
		}

		return init(literals, matchedPattern, parseResult, entryContainer);
	}

	public abstract boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult, EntryContainer entryContainer);

	/**
	 * The first phase of Structure loading.
	 * During this phase, all Structures across all loading scripts are loaded with respect to their priorities.
	 * @return Whether preloading was successful. An error should be printed prior to returning false to specify the cause.
	 */
	public boolean preLoad() {
		return true;
	}

	/**
	 * The second phase of Structure loading.
	 * During this phase, all Structures across all loading scripts are loaded with respect to their priorities.
	 * @return Whether loading was successful. An error should be printed prior to returning false to specify the cause.
	 */
	public abstract boolean load();

	/**
	 * The third and final phase of Structure loading.
	 * During this phase, all Structures across all loading scripts are loaded with respect to their priorities.
	 * This method is primarily designed for Structures that wish to execute actions after
	 *  most other Structures have finished loading.
	 * @return Whether postLoading was successful. An error should be printed prior to returning false to specify the cause.
	 */
	public boolean postLoad() {
		return true;
	}

	/**
	 * Called when this structure is unloaded.
	 */
	public void unload() { }

	/**
	 * Called when this structure is unloaded.
	 * This method is primarily designed for Structures that wish to execute actions after
	 * 	most other Structures have finished unloading.
	 */
	public void postUnload() { }

	/**
	 * The priority of a Structure determines the order in which it should be loaded.
	 * For more information, see the javadoc of {@link Priority}.
	 * @return The priority of this Structure. By default, this is {@link Priority#DEFAULT_PRIORITY}.
	 */
	public org.skriptlang.skript.lang.Priority getPriority() {
		return org.skriptlang.skript.lang.Priority.DEFAULT_PRIORITY;
	}

	@Override
	public String toString() {
		return toString(null, false);
	}

	@Nullable
	public static Structure parse(String expr, SectionNode sectionNode, @Nullable String defaultError) {
		return parse(expr, sectionNode, defaultError, Skript.getStructures().iterator());
	}

	@Nullable
	public static Structure parse(String expr, SectionNode sectionNode, @Nullable String defaultError, Iterator<? extends StructureInfo<? extends Structure>> iterator) {
		ParserInstance.get().getData(StructureData.class).sectionNode = sectionNode;

		iterator = new ConsumingIterator<>(iterator, elementInfo -> ParserInstance.get().getData(StructureData.class).structureInfo = elementInfo);

		try (ParseLogHandler parseLogHandler = SkriptLogger.startParseLogHandler()) {
			Structure structure = SkriptParser.parseStatic(expr, iterator, ParseContext.EVENT, defaultError);
			if (structure != null) {
				parseLogHandler.printLog();
				return structure;
			}
			parseLogHandler.printError();
			return null;
		}
	}

	static {
		ParserInstance.registerData(StructureData.class, StructureData::new);
	}

	@SuppressWarnings("NotNullFieldNotInitialized")
	public static class StructureData extends ParserInstance.Data {

		private SectionNode sectionNode;
		@Nullable
		private StructureInfo<? extends Structure> structureInfo;

		public StructureData(ParserInstance parserInstance) {
			super(parserInstance);
		}

		@Nullable
		public StructureInfo<? extends Structure> getStructureInfo() {
			return structureInfo;
		}

	}

}

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
package org.skriptlang.skript.test.tests.debug;

import ch.njol.skript.effects.EffChange;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import ch.njol.skript.test.runner.SkriptJUnitTest;
import ch.njol.skript.variables.Variables;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skriptlang.skript.lang.debug.Debugger;
import org.skriptlang.skript.lang.debug.Debuggers;
import org.skriptlang.skript.lang.script.Script;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DebuggerTest extends SkriptJUnitTest {

	static {
		setShutdownDelay(1);
	}

	final Path scriptPath = Paths.get("..", "..", "..", "src", "test", "skript", "junit", "DebuggerTest.sk")
		.toAbsolutePath().normalize();

	final String mainFunctionName = "DebuggerTest_main";

	public final List<Object> variableStates = new ArrayList<>();

	final Debugger debugger = new Debugger() {

		@Override
		public void onWalk(TriggerItem triggerItem, Event event) {
			if (checkFile(triggerItem) && triggerItem instanceof EffChange)
				variableStates.add(Variables.getVariable("test", event, true));
		}

		private boolean checkFile(TriggerItem triggerItem) {
			@Nullable Trigger trigger = triggerItem.getTrigger();
			if (trigger == null)
				return false;
			@Nullable Script script = trigger.getScript();
			if (script == null)
				return false;
			@Nullable Path path = script.getConfig().getPath();
            return scriptPath.equals(path);
        }

	};

	@Before
	public void attach() {
		Debuggers.attachDebugger(debugger);
	}

	@Test
	public void test() {
		@Nullable Function<?> mainFunction = Functions.getGlobalFunction(mainFunctionName);
		if (!Debuggers.enabled())
			Assert.fail("Debugger is not enabled");
		if (mainFunction == null)
			Assert.fail("Global function " + mainFunctionName + " was not found");
		mainFunction.execute(new Object[0][]);
		Assert.assertArrayEquals(new Object[] { 1L, 2L }, variableStates.toArray(new Object[0]));
	}

	@After
	public void detach() {
		Debuggers.detachDebugger(debugger);
	}

}

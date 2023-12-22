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
package org.skriptlang.skript;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Collection;

/**
 * The main class for everything related to Skript.
 */
@ApiStatus.Experimental
public interface Skript extends SkriptAddon {

	/**
	 * This implementation makes use of default implementations of required classes.
	 * @param modules Modules for the Skript to use. These modules would make up the "built-in" syntax.
	 * @return A default Skript implementation.
	 */
	@Contract("_ -> new")
	static Skript createInstance(AddonModule... modules) {
		return new SkriptImpl(null, modules);
	}

	/**
	 * This implementation makes use of default implementations of required classes.
	 * @param dataFileDirectory {@link #dataFileDirectory()}
	 * @param modules Modules for the Skript to use. These modules would make up the "built-in" syntax.
	 * @return A default Skript implementation.
	 */
	@Contract("_, _ -> new")
	static Skript createInstance(String dataFileDirectory, AddonModule... modules) {
		return new SkriptImpl(dataFileDirectory, modules);
	}

	/**
	 * @return A read-only view of the syntax registry containing all syntax registered by this Skript and its addons.
	 * This is not a snapshot. Changes made to registers (by other sources) will be reflected.
	 */
	SyntaxRegistry registry();

	/**
	 * @return The current State this Skript is in.
	 */
	State state();

	// TODO listeners for when the state changes
	// For example, Converters will listen for when registration closes to construct chained converters
	@ApiStatus.Internal
	void updateState(State state);

	/**
	 * A State describes the point of initialization that a Skript instance is in.
	 */
	// TODO consider additional states for addon registration.
	enum State {

		/**
		 * A state in which a Skript instance is still performing its initialization.
		 * New registrations are permitted.
		 */
		REGISTRATION(true),

		/**
		 * A state in which a Skript instance has finished its initialization and addons may now load.
		 * New registrations are permitted.
		 */
		ADDON_REGISTRATION(true),

		/**
		 * A state in which registration is over, but not yet forbidden.
		 */
		ENDED_REGISTRATION(false),

		/**
		 * A state in which registration is over and forbidden.
		 */
		CLOSED_REGISTRATION(false);

		private final boolean acceptsRegistration;

		State(boolean acceptsRegistration) {
			this.acceptsRegistration = acceptsRegistration;
		}

		/**
		 * @return Whether registrations can occur in this state.
		 */
		public boolean acceptsRegistration() {
			return acceptsRegistration;
		}

	}

	/**
	 * Registers the provided addon with this Skript and loads the provided modules.
	 * @param addon The addon to register.
	 * @param modules Any modules of this addon to load.
	 */
	void registerAddon(SkriptAddon addon, AddonModule... modules);

	/**
	 * Registers the provided addon with this Skript and loads the provided modules.
	 * @param addon The addon to register.
	 * @param modules Any modules of this addon to load.
	 */
	void registerAddon(SkriptAddon addon, Collection<? extends AddonModule> modules);

	/**
	 * @return An unmodifiable snapshot of addons currently registered with this Skript.
	 */
	@Unmodifiable
	Collection<SkriptAddon> addons();

	/**
	 * {@inheritDoc}
	 */
	default String name() {
		return "Skript";
	}

}

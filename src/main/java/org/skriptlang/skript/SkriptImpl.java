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

import ch.njol.skript.SkriptAPIException;
import ch.njol.skript.localization.Language;
import ch.njol.skript.registrations.Classes;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converters;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

final class SkriptImpl implements Skript {

	@Nullable
	private final String dataFileDirectory;

	private final SyntaxRegistry registry = SyntaxRegistry.createInstance();
	private final SyntaxRegistry unmodifiableRegistry = SyntaxRegistry.unmodifiableView(registry);

	private State state = State.REGISTRATION;

	SkriptImpl(@Nullable String dataFileDirectory, AddonModule... modules) {
		this.dataFileDirectory = dataFileDirectory;
		this.registerAddon(this, modules);
	}

	@Override
	public SyntaxRegistry registry() {
		return unmodifiableRegistry;
	}

	@Override
	public State state() {
		return state;
	}

	@Override
	public void updateState(State state) {
		if (state == State.ENDED_REGISTRATION) {
			Converters.createChainedConverters();
		} else if (state == State.CLOSED_REGISTRATION) {
			registry.closeRegistration();
			Classes.onRegistrationsStop();
		}
		this.state = state;
	}

	//
	// SkriptAddon Management
	//

	private static final Set<SkriptAddon> addons = new HashSet<>();

	@Override
	public void registerAddon(SkriptAddon addon, AddonModule... modules) {
		registerAddon(addon, Arrays.asList(modules));
	}

	@Override
	public void registerAddon(SkriptAddon addon, Collection<? extends AddonModule> modules) {
		// ensure registration can proceed
		if (!state().acceptsRegistration()) {
			throw new UnsupportedOperationException("Registration is closed");
		}

		// make sure an addon is not already registered with this name
		if (addons.stream().anyMatch(otherAddon -> addon.name().equals(otherAddon.name()))) {
			// TODO more detailed error?
			throw new SkriptAPIException("An addon with the name '" + addon.name() + "' is already registered");
		}

		// load and register the addon
		Language.loadDefault(addon);
		for (AddonModule module : modules) {
			module.load(addon, registry);
		}
		addons.add(addon);
	}

	@Override
	@Unmodifiable
	public Collection<SkriptAddon> addons() {
		return ImmutableSet.copyOf(addons);
	}

	//
	// SkriptAddon Implementation
	//

	@Override
	@Nullable
	public String dataFileDirectory() {
		return dataFileDirectory;
	}

	@Override
	public String languageFileDirectory() {
		return "lang";
	}

}

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

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.skriptlang.skript.registry.SyntaxOrigin;

public final class BukkitOrigin implements SyntaxOrigin {
	
	@Contract("_ -> new")
	public static SyntaxOrigin of(Plugin plugin) {
		return new BukkitOrigin(plugin.getClass().getCanonicalName());
	}
	
	@Contract("_ -> new")
	public static SyntaxOrigin of(String name) {
		return new BukkitOrigin(name);
	}
	
	private final String name;
	
	private BukkitOrigin(Plugin plugin) {
		name = plugin.getClass().getCanonicalName();
	}
	
	private BukkitOrigin(String name) {
		this.name = name;
	}
	
	@Override
	public String name() {
		return name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof BukkitOrigin))
			return false;
		BukkitOrigin origin = (BukkitOrigin) other;
		return name().equals(origin.name());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("name", name)
			.toString();
	}
	
}

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
package org.skriptlang.skript.registration;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

final class SyntaxRegisterImpl<T extends SyntaxInfo<?>> implements SyntaxRegister<T> {
	
	private final Int2ObjectMap<T> syntaxes = new Int2ObjectRBTreeMap<>();
	
	@Override
	@Unmodifiable 
	public List<T> syntaxes() {
		synchronized (syntaxes) {
			return ImmutableList.copyOf(syntaxes.values());
		}
	}
	
	@Override
	public void add(T info) {
		synchronized (syntaxes) {
			if (syntaxes.put(info.id(), info) != null)
				throw new IllegalStateException("Duplicate id " + info.id() + " on info " + info);
		}
	}
	
	@Override
	@Contract("-> new")
	public SyntaxRegister<T> closeRegistration() {
		return new FinalSyntaxRegister<>(this);
	}
	
	static final class FinalSyntaxRegister<T extends SyntaxInfo<?>> implements SyntaxRegister<T> {
		
		private final List<T> syntaxes;
		
		FinalSyntaxRegister(SyntaxRegister<T> register) {
			syntaxes = register.syntaxes();
		}
		
		@Override
		@Unmodifiable 
		public List<T> syntaxes() {
			return syntaxes;
		}
		
		@Override
		@Contract("_ -> fail")
		public void add(T info) {
			throw new UnsupportedOperationException("Registration is closed");
		}
		
		@Override
		@Contract("-> fail")
		public SyntaxRegister<T> closeRegistration() {
			throw new UnsupportedOperationException("Registration is closed");
		}
		
	}
	
}

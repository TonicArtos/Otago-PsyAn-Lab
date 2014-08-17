
/*
 Copyright (C) 2012, 2013, 2014 University of Otago, Tonic Artos <tonic.artos@gmail.com>

 Otago PsyAn Lab is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.

 In accordance with Section 7(b) of the GNU General Public License version 3,
 all legal notices and author attributions must be preserved.
 */

package nz.ac.otago.psyanlab.common.model.generator;

import android.content.Context;

import nz.ac.otago.psyanlab.common.R;
import nz.ac.otago.psyanlab.common.model.Generator;
import nz.ac.otago.psyanlab.common.model.util.NameResolverFactory;

public class Shuffle extends Generator {
    protected static class MethodNameFactory extends Generator.MethodNameFactory {
        @Override
        public String getName(Context context, int lookup) {
            switch (lookup) {
                default:
                    return super.getName(context, lookup);
            }
        }
    }

    public Shuffle() {
    }

    @Override
    public int getKindResId() {
        return R.string.label_generator_shuffle;
    }

    @Override
    public NameResolverFactory getMethodNameFactory() {
        return new MethodNameFactory();
    }
}

/*
 Copyright (C) 2014 Tonic Artos <tonic.artos@gmail.com>

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

package nz.ac.otago.psyanlab.common.model.timer;

import com.google.gson.annotations.Expose;

import android.content.Context;

import nz.ac.otago.psyanlab.common.R;
import nz.ac.otago.psyanlab.common.model.Timer;
import nz.ac.otago.psyanlab.common.model.util.NameResolverFactory;

public class Periodic extends Timer {
    public static NameResolverFactory getEventNameFactory() {
        return new EventNameFactory();
    }

    protected static class MethodNameFactory extends Timer.MethodNameFactory {
        @Override
        public String getName(Context context, int lookup) {
            return super.getName(context, lookup);
        }
    }

    @Expose
    public int iterations;

    public Periodic() {}

    @Override
    public int getKindResId() {
        return R.string.label_timer_periodic;
    }

    @Override
    public NameResolverFactory getMethodNameFactory() { return super.getMethodNameFactory(); }

    @Override
    public String getStringFor(Context context, int resId) {
        if (resId == R.id.timer_iterations) {
            if (iterations == -1) {
                return context.getString(R.string.text_timer_infinite);
            }
            return context.getString(R.string.format_timer_iterations, iterations);
        }

        return super.getStringFor(context, resId);
    }
}


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

package nz.ac.otago.psyanlab.common.designer.util;

import nz.ac.otago.psyanlab.common.designer.ExperimentDesignerActivity.RuleDataChangeListener;
import nz.ac.otago.psyanlab.common.model.Rule;

import android.widget.SpinnerAdapter;

public interface RuleCallbacks {
    void addRuleDataChangeListener(RuleDataChangeListener listener);

    long addRule(Rule rule);

    void deleteRule(long id);

    SpinnerAdapter getEventsAdapter(Class<?> clazz);

    Rule getRule(long ruleId);

    ProgramComponentAdapter<Rule> getRuleAdapter(long sceneId);

    void removeRuleDataChangeListener(RuleDataChangeListener listener);

    void putRule(long id, Rule rule);
}

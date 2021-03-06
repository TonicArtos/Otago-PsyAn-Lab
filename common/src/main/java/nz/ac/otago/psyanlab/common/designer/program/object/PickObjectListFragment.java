
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

package nz.ac.otago.psyanlab.common.designer.program.object;

import nz.ac.otago.psyanlab.common.R;
import nz.ac.otago.psyanlab.common.designer.program.util.ProgramCallbacks;
import nz.ac.otago.psyanlab.common.designer.util.ExperimentObjectReferenceAdapter;
import nz.ac.otago.psyanlab.common.model.ExperimentObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PickObjectListFragment extends ListFragment {
    private static final String ARG_CALLER_ID = "arg_caller_id";

    private static final String ARG_CALLER_KIND = "arg_caller_kind";

    private static final String ARG_FILTER = "arg_filter";

    private static final String ARG_SCOPE = "arg_section";

    public static PickObjectListFragment newInstance(int callerKind, long callerId, int scope,
            int filter) {
        PickObjectListFragment f = new PickObjectListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCOPE, scope);
        args.putInt(ARG_FILTER, filter);
        args.putInt(ARG_CALLER_KIND, callerKind);
        args.putLong(ARG_CALLER_ID, callerId);
        f.setArguments(args);
        return f;
    }

    private ExperimentObjectReferenceAdapter mAdapter;

    private ProgramCallbacks mCallbacks;

    private long mCallerId;

    private int mCallerKind;

    private int mFilter;

    private int mScope;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof ProgramCallbacks)) {
            throw new RuntimeException("Activity must implement fragment callbacks.");
        }
        mCallbacks = (ProgramCallbacks)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_object_list, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ExperimentObject object = mAdapter.getItem(position);
        ((PickObjectDialogueFragment)getParentFragment()).onObjectPicked(id, object.kind(),
                l.getItemAtPosition(position));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args.isEmpty()) {
            throw new RuntimeException("Missing arguments.");
        }

        mScope = args.getInt(ARG_SCOPE);
        mCallerKind = args.getInt(ARG_CALLER_KIND);
        mCallerId = args.getLong(ARG_CALLER_ID);
        mFilter = args.getInt(ARG_FILTER);

        mAdapter = mCallbacks.getObjectSectionListAdapter(mCallerKind, mCallerId, mScope, mFilter);
        setListAdapter(mAdapter);
    }
}

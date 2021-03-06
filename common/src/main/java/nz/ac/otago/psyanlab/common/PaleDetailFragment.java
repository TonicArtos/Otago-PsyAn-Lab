/*
 Copyright (C) 2012, 2013 University of Otago, Tonic Artos <tonic.artos@gmail.com>

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

package nz.ac.otago.psyanlab.common;

import nz.ac.otago.psyanlab.common.designer.ExperimentDesignerActivity;
import nz.ac.otago.psyanlab.common.util.Args;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

public class PaleDetailFragment extends Fragment {
    public static final String TAG = "pale_detail_fragment";

    private Callbacks mCallbacks;

    protected UserExperimentDelegateI mDelegate;

    private ViewHolder mViews;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_pale_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pale_detail, container, false);
        ListView records = (ListView)v.findViewById(R.id.records);
        records.addHeaderView(inflater.inflate(R.layout.header_pale_detail, records, false));
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_run) {
            doRunExperiment();
            return true;
        } else if (itemId == R.id.menu_delete) {
            doDeleteExperiment();
            return true;
        } else if (itemId == R.id.menu_edit) {
            doEditExperiment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViews = new ViewHolder(view);

        mViews.setViewValues(mDelegate);

        if (mDelegate == null) {
            setHasOptionsMenu(false);
        } else {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the data in case it has change while we were away.
        setExperimentDelegate(mDelegate);
    }

    /**
     * Update fragment with selected experiment encapsulated by the experiment
     * delegate.
     * 
     * @param experimentDelegate Delegate which gives access to experiment.
     */
    public void setExperimentDelegate(UserExperimentDelegateI experimentDelegate) {
        mDelegate = experimentDelegate;
        if (mDelegate != null) {
            mDelegate.init(getActivity());
        }

        if (mViews != null) {
            mViews.setViewValues(mDelegate);
        }

        if (mDelegate == null) {
            setHasOptionsMenu(false);
        } else {
            setHasOptionsMenu(true);
        }
    }

    private void doDeleteExperiment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Experiment and all associated data will be deleted permanently.")
                .setTitle("Delete?").setCancelable(true)
                .setNeutralButton("Export Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mDelegate.deleteExperiment();
                            setExperimentDelegate(null);
                            mCallbacks.onExperimentDeleted();
                        } catch (IOException e) {
                            // FIXME: Handle error deleting experiment.
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void doEditExperiment() {
        Intent i = new Intent(getActivity(), ExperimentDesignerActivity.class).putExtra(
                Args.USER_EXPERIMENT_DELEGATE, mDelegate);
        startActivity(i);
    }

    private void doRunExperiment() {
        // Intent i = new Intent(getActivity(), ExperimentRuntimeActivity.class)
        // .putExtra(Args.USER_EXPERIMENT_DELEGATE, mDelegate);
        // startActivity(i);
    }

    public interface Callbacks {
        void onExperimentDeleted();
    }

    private class ViewHolder {
        public TextView authors;

        public TextView created;

        public TextView description;

        public TextView lastModified;

        public TextView lastRun;

        public TextView name;

        public ListView records;

        private ListAdapter mAdapter;

        private View mRecordsTitle;

        public ViewHolder(View view) {
            name = (TextView)view.findViewById(R.id.name);
            description = (TextView)view.findViewById(R.id.description);
            authors = (TextView)view.findViewById(R.id.authors);
            created = (TextView)view.findViewById(R.id.created);
            lastModified = (TextView)view.findViewById(R.id.last_modified);
            lastRun = (TextView)view.findViewById(R.id.last_run);
            records = (ListView)view.findViewById(R.id.records);
            mRecordsTitle = view.findViewById(R.id.records_title);
        }

        public void updateParameterTitleVisibility() {
            if (mAdapter == null || mAdapter.getCount() == 0) {
                mRecordsTitle.setVisibility(View.GONE);
            } else {
                mRecordsTitle.setVisibility(View.VISIBLE);
            }
        }

        public void setViewValues(UserExperimentDelegateI delegate) {
            if (delegate == null) {
                // Setup for blank scenario.
                mAdapter = null;
                records.setAdapter(null);
                records.setVisibility(View.GONE);
                updateParameterTitleVisibility();
                return;
            }

            // Fill in views with experiment held by the delegate.
            records.setVisibility(View.VISIBLE);

            final PaleRow experimentDetails = delegate.getExperimentDetails();

            // Show experiment name if we have it, else we show a default
            // message to advise the missing name.
            if (TextUtils.isEmpty(experimentDetails.name)) {
                name.setText(getString(R.string.note_experiment_no_name));
            } else {
                name.setText(experimentDetails.name);
            }

            // Show description if we have one, else we show a default message
            // to advise the missing description.
            if (TextUtils.isEmpty(experimentDetails.description)) {
                description.setText(getString(R.string.note_experiment_no_description));
            } else {
                description.setText(experimentDetails.description);
            }

            // Show authors if we have them, else we show a default message
            // to advise the missing authorship.
            if (TextUtils.isEmpty(experimentDetails.authors)) {
                authors.setText(getString(R.string.note_experiment_no_authors));
            } else {
                authors.setText(experimentDetails.authors);
            }

            created.setText(DateUtils.formatDateTime(getActivity(), experimentDetails.dateCreated,
                    0));

            lastModified.setText(DateUtils.formatDateTime(getActivity(),
                    experimentDetails.lastModified, 0));

            if (experimentDetails.lastRun > experimentDetails.dateCreated) {
                lastRun.setText(DateUtils.formatDateTime(getActivity(), experimentDetails.lastRun,
                        0));
            } else {
                lastRun.setText("Never");
            }

            mAdapter = new RecordAdapterWrapper(getActivity(), mDelegate.getRecordsAdapter(
                    R.layout.list_item_record, new int[] {
                            UserExperimentDelegateI.RECORD_ID, UserExperimentDelegateI.RECORD_DATE,
                            UserExperimentDelegateI.RECORD_NOTE
                    }, new int[] {
                            R.id.session_id, R.id.date, R.id.note
                    }));
            records.setAdapter(mAdapter);

            updateParameterTitleVisibility();
        }
    }
}

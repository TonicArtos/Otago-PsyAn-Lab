
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

package nz.ac.otago.psyanlab.common.designer.program.generator;

import nz.ac.otago.psyanlab.common.R;
import nz.ac.otago.psyanlab.common.designer.program.util.ProgramCallbacks;
import nz.ac.otago.psyanlab.common.model.Generator;
import nz.ac.otago.psyanlab.common.model.generator.Random;
import nz.ac.otago.psyanlab.common.model.generator.Shuffle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditGeneratorDialogFragment extends DialogFragment {

    private static final String ARG_ID = "arg_id";

    private static final long INVALID_ID = -1;

    private static final int MODE_EDIT = 0x02;

    private static final int MODE_NEW = 0x01;

    private static final int POS_RANDOM = 0x00;

    private static final int POS_SHUFFLE = 0x01;

    private ProgramCallbacks mCallbacks;

    private Generator mGenerator;

    private long mId;

    private int mMode;

    private OnGeneratorCreatedListener mOnGeneratorCreatedListener;

    private ViewHolder mViews;

    public static EditGeneratorDialogFragment newDialog(long id) {
        EditGeneratorDialogFragment f = new EditGeneratorDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof ProgramCallbacks)) {
            throw new RuntimeException("Activity must implement fragment callbacks.");
        }
        mCallbacks = (ProgramCallbacks) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Bundle args = getArguments();
        if (args != null) {
            mId = args.getLong(ARG_ID, INVALID_ID);
        }

        if (mId == INVALID_ID) {
            mGenerator = new Random();
            mId = mCallbacks.addGenerator(mGenerator);
            mGenerator.name = "Generator " + (mId + 1);
            mMode = MODE_NEW;
        } else {
            mGenerator = mCallbacks.getGenerator(mId);
            mMode = MODE_EDIT;
        }

        View view = inflater.inflate(R.layout.dialogue_edit_generator, null);
        mViews = new ViewHolder(view);
        mViews.initViews();
        mViews.setViewValues(mGenerator);
        // Build dialogue.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(
                (Integer) ((mMode == MODE_NEW) ? R.string.title_new_generator
                        : R.string.title_edit_generator))
                .setView(view)
                .setPositiveButton(
                        (mMode == MODE_NEW) ? R.string.action_create : R.string.action_confirm,
                        new OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                switch (mViews.type.getSelectedItemPosition()) {
                                    case POS_RANDOM:
                                        mGenerator = new Random();
                                        break;
                                    case POS_SHUFFLE:
                                        mGenerator = new Shuffle();
                                        break;

                                    default:
                                        throw new RuntimeException(
                                                "Unknown kind of generator selected");
                                }

                                String name = mViews.name.getText().toString();
                                if (TextUtils.isEmpty(name)) {
                                    name = mViews.name.getHint().toString();
                                }

                                String start = mViews.start.getText().toString();
                                if (TextUtils.isEmpty(start)) {
                                    start = mViews.start.getHint().toString();
                                }

                                String end = mViews.end.getText().toString();
                                if (TextUtils.isEmpty(end)) {
                                    end = mViews.end.getHint().toString();
                                }

                                mGenerator.name = name;
                                mGenerator.start = Integer.parseInt(start);
                                mGenerator.end = Integer.parseInt(end);

                                mCallbacks.putGenerator(mId, mGenerator);
                                if (mMode == MODE_NEW) {
                                    mOnGeneratorCreatedListener.onGeneratorCreated(mId);
                                }
                            }
                        }).setNegativeButton(R.string.action_discard, new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mMode == MODE_NEW) {
                    mCallbacks.deleteGenerator(mId);
                }
                getDialog().cancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setOnGeneratorActionListener(OnGeneratorCreatedListener onGeneratorActionListener) {
        mOnGeneratorCreatedListener = onGeneratorActionListener;
    }

    public interface OnGeneratorCreatedListener {

        void onGeneratorCreated(long id);
    }

    public class GeneratorType {

        public static final long TYPE_RANDOM = 0x02;

        public static final long TYPE_SHUFFLE = 0x01;

        private long mId;

        private int mLabelResId;

        public GeneratorType(long typeRandom, int labelResId) {
            mId = typeRandom;
            mLabelResId = labelResId;
        }

        public long getId() {
            return mId;
        }

        public int getLabelResId() {
            return mLabelResId;
        }

        @Override
        public String toString() {
            return getResources().getString(mLabelResId);
        }
    }

    public class ViewHolder {

        public EditText end;

        public TextView name;

        public EditText start;

        public Spinner type;

        public ViewHolder(View view) {
            name = (EditText) view.findViewById(R.id.name);
            type = (Spinner) view.findViewById(R.id.type);
            start = (EditText) view.findViewById(R.id.start);
            end = (EditText) view.findViewById(R.id.end);
        }

        public void initViews() {
            GeneratorType[] types = new GeneratorType[2];
            types[POS_RANDOM] = new GeneratorType(GeneratorType.TYPE_RANDOM, R.string.label_random);
            types[POS_SHUFFLE] = new GeneratorType(GeneratorType.TYPE_SHUFFLE,
                    R.string.label_shuffle);

            ArrayAdapter<GeneratorType> typeAdapter = new ArrayAdapter<GeneratorType>(getActivity(),
                    android.R.layout.simple_list_item_1, types);
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mViews.type.setAdapter(typeAdapter);
        }

        public void setViewValues(Generator generator) {
            if (generator == null) {
                return;
            }

            mViews.name.setText(generator.name);
            mViews.start.setText(String.valueOf(mGenerator.start));
            mViews.end.setText(String.valueOf(mGenerator.end));

            if (generator instanceof Random) {
                mViews.type.setSelection(POS_RANDOM);
            } else if (generator instanceof Shuffle) {
                mViews.type.setSelection(POS_SHUFFLE);
            } else {
                throw new RuntimeException(
                        "Unknown generator kind " + generator.getClass().getName());
            }
        }

    }
}

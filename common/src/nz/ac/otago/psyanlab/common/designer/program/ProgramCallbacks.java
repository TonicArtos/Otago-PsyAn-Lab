
package nz.ac.otago.psyanlab.common.designer.program;

import nz.ac.otago.psyanlab.common.designer.ExperimentDesignerActivity.ActionDataChangeListener;
import nz.ac.otago.psyanlab.common.designer.ExperimentDesignerActivity.GeneratorDataChangeListener;
import nz.ac.otago.psyanlab.common.designer.ExperimentDesignerActivity.LoopDataChangeListener;
import nz.ac.otago.psyanlab.common.designer.ExperimentDesignerActivity.RuleDataChangeListener;
import nz.ac.otago.psyanlab.common.designer.ExperimentDesignerActivity.SceneDataChangeListener;
import nz.ac.otago.psyanlab.common.designer.ProgramComponentAdapter;
import nz.ac.otago.psyanlab.common.designer.util.DialogueResultListenerRegistrar;
import nz.ac.otago.psyanlab.common.designer.util.HashMapAdapter.FragmentFactoryI;
import nz.ac.otago.psyanlab.common.model.Action;
import nz.ac.otago.psyanlab.common.model.Generator;
import nz.ac.otago.psyanlab.common.model.Loop;
import nz.ac.otago.psyanlab.common.model.Prop;
import nz.ac.otago.psyanlab.common.model.Rule;
import nz.ac.otago.psyanlab.common.model.Scene;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

public interface ProgramCallbacks extends DialogueResultListenerRegistrar {
    public Fragment getFragment(String tag);

    public ArrayList<Prop> getPropsArray(long stageId);

    void addActionDataChangeListener(ActionDataChangeListener listener);

    void addGeneratorDataChangeListener(GeneratorDataChangeListener listener);

    void addLoopDataChangeListener(LoopDataChangeListener listener);

    void addRuleDataChangeListener(RuleDataChangeListener listener);

    void addSceneDataChangeListener(SceneDataChangeListener listener);

    long createAction(Action action);

    long createGenerator(Generator generator);

    long createLoop(Loop loop);

    long createRule(Rule rule);

    long createScene(Scene scene);

    void deleteAction(long id);

    void deleteGenerator(long id);

    void deleteLoop(long id);

    void deleteRule(long id);

    void deleteScene(long id);

    void editStage(long objectId);

    Action getAction(long id);

    ProgramComponentAdapter<Action> getActionAdapter(long ruleId);

    ListAdapter getEventsAdapter(Class<?> clazz);

    Generator getGenerator(long id);

    ProgramComponentAdapter<Generator> getGeneratorAdapter(long loopId);

    Loop getLoop(long loopId);

    ProgramComponentAdapter<Loop> getLoopAdapter();

    ListAdapter getMethodsAdapter(Class<?> clazz, Class<?> returnType);

    ListAdapter getObjectSectionListAdapter(long sceneId, int section, int filter);

    FragmentPagerAdapter getObjectsPagerAdapter(long sceneId, FragmentManager fm,
            FragmentFactoryI<Integer> factory);

    Rule getRule(long ruleId);

    ProgramComponentAdapter<Rule> getRuleAdapter(long sceneId);

    Scene getScene(long sceneId);

    ProgramComponentAdapter<Scene> getScenesAdapter(long loopId);

    void removeActionDataChangeListener(ActionDataChangeListener listener);

    void removeGeneratorDataChangeListener(GeneratorDataChangeListener listener);

    void removeLoopDataChangeListener(LoopDataChangeListener listener);

    void removeRuleDataChangeListener(RuleDataChangeListener listener);

    void removeSceneDataChangeListener(SceneDataChangeListener listener);

    void updateAction(long id, Action action);

    void updateGenerator(long id, Generator generator);

    void updateLoop(long id, Loop loop);

    void updateRule(long id, Rule rule);

    void updateScene(long id, Scene scene);

}

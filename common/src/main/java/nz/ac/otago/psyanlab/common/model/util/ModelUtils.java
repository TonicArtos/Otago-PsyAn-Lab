
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

package nz.ac.otago.psyanlab.common.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import android.support.v4.util.LongSparseArray;

import nz.ac.otago.psyanlab.common.BuildConfig;
import nz.ac.otago.psyanlab.common.model.Asset;
import nz.ac.otago.psyanlab.common.model.Experiment;
import nz.ac.otago.psyanlab.common.model.ExperimentObject;
import nz.ac.otago.psyanlab.common.model.Generator;
import nz.ac.otago.psyanlab.common.model.Operand;
import nz.ac.otago.psyanlab.common.model.Prop;
import nz.ac.otago.psyanlab.common.model.Question;
import nz.ac.otago.psyanlab.common.model.Timer;
import nz.ac.otago.psyanlab.common.model.TouchEvent;
import nz.ac.otago.psyanlab.common.model.TouchMotionEvent;
import nz.ac.otago.psyanlab.common.model.Variable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ModelUtils {
    private static Gson mGson;

    public static Gson getDataReaderWriter() {
        if (mGson == null) {
            GsonBuilder gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();

            // Type loopArrayType = new TypeToken<LongSparseArray<Loop>>() {
            // }.getType();
            // gson.registerTypeAdapter(loopArrayType,
            // new LongSparseArrayGsonAdapter<Loop>(Loop.class));

            gson.registerTypeAdapter(Operand.class, new AbsModelGsonAdapter<Operand>(
                    AbsModelGsonAdapter.NS_MODEL_OPERAND));
            gson.registerTypeAdapter(Generator.class, new AbsModelGsonAdapter<Generator>(
                    AbsModelGsonAdapter.NS_MODEL_GENERATOR));
            gson.registerTypeAdapter(Question.class, new AbsModelGsonAdapter<Question>(
                    AbsModelGsonAdapter.NS_MODEL_SUBJECT));
            gson.registerTypeAdapter(Prop.class, new AbsModelGsonAdapter<Prop>(
                    AbsModelGsonAdapter.NS_MODEL_PROP));
            gson.registerTypeAdapter(Asset.class, new AbsModelGsonAdapter<Asset>(
                    AbsModelGsonAdapter.NS_MODEL_ASSET));
            gson.registerTypeAdapter(Variable.class, new AbsModelGsonAdapter<Variable>(
                    AbsModelGsonAdapter.NS_MODEL_VARIABLE));
            gson.registerTypeAdapter(Timer.class, new AbsModelGsonAdapter<Timer>(
                    AbsModelGsonAdapter.NS_MODEL_TIMER));
            if (BuildConfig.DEBUG) {
                gson.setPrettyPrinting();
            }
            mGson = gson.create();
        }

        return mGson;
    }

    public static NameResolverFactory getEventNameFactory(final Class<?> clazz) {
        Method m;
        try {
            m = clazz.getMethod("getEventNameFactory", (Class<?>[])null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error getting event name factory for " + clazz, e);
        }

        final NameResolverFactory nameFactory;
        try {
            nameFactory = (NameResolverFactory)m.invoke(null, (Object[])null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error getting event name factory for " + clazz, e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error getting event name factory for " + clazz, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error getting event name factory for " + clazz, e);
        }
        return nameFactory;
    }

    // public static NameResolverFactory getMethodNameFactory(final Class<?>
    // clazz) {
    // Method m;
    // try {
    // m = clazz.getMethod("getMethodNameFactory", (Class<?>[])null);
    // } catch (NoSuchMethodException e) {
    // throw new RuntimeException("Error getting method name factory for " +
    // clazz, e);
    // }
    //
    // final NameResolverFactory nameFactory;
    // try {
    // nameFactory = (NameResolverFactory)m.invoke(null, (Object[])null);
    // } catch (IllegalAccessException e) {
    // throw new RuntimeException("Error getting method name factory for " +
    // clazz, e);
    // } catch (IllegalArgumentException e) {
    // throw new RuntimeException("Error getting method name factory for " +
    // clazz, e);
    // } catch (InvocationTargetException e) {
    // throw new RuntimeException("Error getting method name factory for " +
    // clazz, e);
    // }
    // return nameFactory;
    // }
    //
    // public static NameResolverFactory getParameterNameFactory(final Class<?>
    // clazz) {
    // Method m;
    // try {
    // m = clazz.getMethod("getParameterNameFactory", (Class<?>[])null);
    // } catch (NoSuchMethodException e) {
    // throw new RuntimeException("Error getting parameter name factory for " +
    // clazz, e);
    // }
    //
    // final NameResolverFactory nameFactory;
    // try {
    // nameFactory = (NameResolverFactory)m.invoke(null, (Object[])null);
    // } catch (IllegalAccessException e) {
    // throw new RuntimeException("Error getting parameter name factory for " +
    // clazz, e);
    // } catch (IllegalArgumentException e) {
    // throw new RuntimeException("Error getting parameter name factory for " +
    // clazz, e);
    // } catch (InvocationTargetException e) {
    // throw new RuntimeException("Error getting parameter name factory for " +
    // clazz, e);
    // }
    // return nameFactory;
    // }

    public static ExperimentObject getEventObject(EventData event) {
        switch (event.type()) {
            case EventData.EVENT_TOUCH:
                return new TouchEvent();
            case EventData.EVENT_TOUCH_MOTION:
                return new TouchMotionEvent();

            default:
                return null;
        }
    }

    /**
     * Get the parameter id annotation for a parameter of the given method.
     * 
     * @param parameterPosition Position of parameter in method.
     * @param method Method to query.
     * @return
     */
    public static ParameterId getParameterIdAnnotation(int parameterPosition, Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int j = 0; j < parameterAnnotations.length; j++) {
            Annotation annotation = parameterAnnotations[parameterPosition][j];
            if (annotation instanceof ParameterId) {
                return (ParameterId)annotation;
            }
        }
        return null;
    }

    public static Experiment readDefinition(String paleDefinition) {
        return ModelUtils.getDataReaderWriter().fromJson(
                new JsonReader(new StringReader(paleDefinition)), Experiment.class);
    }

    public static Experiment readFile(File paleFile) throws FileNotFoundException {
        return ModelUtils.getDataReaderWriter().fromJson(new JsonReader(new FileReader(paleFile)),
                Experiment.class);
    }

    /**
     * Checks to see the given ored set of return types intersects with the
     * given method's return type.
     * 
     * @param method Method to test.
     * @param returnTypes Ored set of return types.
     * @return True if intersection.
     */
    public static boolean returnTypeIntersects(Method method, int returnTypes) {
        if ((returnTypes & Type.TYPE_BOOLEAN) != 0 && method.getReturnType().equals(Boolean.TYPE)) {
            return true;
        }
        if ((returnTypes & Type.TYPE_INTEGER) != 0 && method.getReturnType().equals(Integer.TYPE)) {
            return true;
        }
        if ((returnTypes & Type.TYPE_FLOAT) != 0 && method.getReturnType().equals(Float.TYPE)) {
            return true;
        }
        if ((returnTypes & Type.TYPE_STRING) != 0 && method.getReturnType().equals(String.class)) {
            return true;
        }
        if (returnTypes == 0 && method.getReturnType().equals(Void.TYPE)) {
            return true;
        }
        return false;
    }

    public static Long findUnusedKey(HashMap<Long, ?> map) {
        Long currKey = 0l;
        while (true) {
            if (!map.keySet().contains(currKey)) {
                break;
            }
            currKey++;
        }
        return currKey;
    }

    public static Long findUnusedKey(LongSparseArray<?> map) {
        Long currKey = 0l;
        while (true) {
            if (map.indexOfKey(currKey) < 0) {
                break;
            }
            currKey++;
        }
        return currKey;
    }
}

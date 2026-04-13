/*
 * Copyright 2026 brickwall2900
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.brickwall2900.presence.systray;

import io.github.brickwall2900.presence.systray.dorkbox.DorkboxTrayIconProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class SystemTrayProvider {
    private static TrayIconProvider trayIconProvider;

    public static TrayIconProvider get() {
        if (trayIconProvider == null) {
            create();
        }

        return trayIconProvider;
    }

    private static void create() {
        // i'm not going to use ServiceProviders for this one
        // unless there's a third provider or extensibility is required
        // we'll be using a simple class based lookup system
        String trayIconClass = System.getProperty("presence.trayIconProvider", DorkboxTrayIconProvider.class.getName());
        try {
            Class<?> potentialTrayIconManagerClass = Class.forName(trayIconClass);
            if (!TrayIconProvider.class.isAssignableFrom(potentialTrayIconManagerClass)) {
                throw new ClassCastException(potentialTrayIconManagerClass + " does not implement TrayIconProvider");
            }

            Constructor<?> constructor = potentialTrayIconManagerClass.getConstructor();
            trayIconProvider = (TrayIconProvider) constructor.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid class " + trayIconClass, e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No-arg constructor cannot be located", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

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

package io.github.brickwall2900.presence.lua;

import io.github.brickwall2900.presence.platform.Platform;
import org.jspecify.annotations.Nullable;
import oshi.ffm.SystemInfo;
import party.iroiro.luajava.ExternalLoader;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.luaj.LuaJ;
import party.iroiro.luajava.value.LuaValue;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

// Lua bridge for Computer Presence
public class LuaBridge {
    private final Lua lua = new LuaJ();
    private final DiscordPresenceObject presenceObject;
    private LuaValue presenceFunction;

    public LuaBridge(Platform platform, SystemInfo systemInfo) {
        lua.openLibrary("");
        lua.openLibrary("string");
        lua.openLibrary("table");
        lua.openLibrary("math");

        lua.pushNil();
        lua.setGlobal("luajava");

        lua.setExternalLoader(new ExternalLoader() {
            @Override
            public @Nullable Buffer load(String module, Lua L) {
                throw new IllegalStateException("Cannot load modules in a restricted environment!");
            }
        });

        presenceObject = new DiscordPresenceObject();
        lua.set("presence", new DiscordPresenceApi(lua, presenceObject, platform, systemInfo));
    }

    public void getPresenceFunction(String source) {
        byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        lua.load(buffer, "presence-script");

        LuaValue[] values = lua.get().call();
        if (values.length != 1) {
            throw new IllegalStateException("return values isn't exactly 1");
        }

        LuaValue maybeFunction = values[0];
        if (maybeFunction.type() != Lua.LuaType.FUNCTION) {
            throw new IllegalStateException("return value isn't a function!");
        }

        presenceFunction = maybeFunction;
    }

    public int getTickRate() {
        int tickRate = 3000;
        LuaValue tickRateValue = lua.get("TICK_RATE");
        if (tickRateValue.type() == Lua.LuaType.NUMBER) {
            tickRate = Math.toIntExact(tickRateValue.toInteger());
        }

        return tickRate;
    }

    public DiscordPresenceObject getPresenceObject() {
        return presenceObject;
    }

    public void run() {
        presenceFunction.call();
    }

    public void destroy() {
        presenceFunction = null;
        lua.close();
    }
}

package io.github.brickwall2900.presence.lua;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.jcm.discordgamesdk.activity.ActivityType;
import io.github.brickwall2900.presence.platform.Platform;
import oshi.ffm.SystemInfo;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.value.LuaValue;

import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

public class DiscordPresenceApi {
    private static final Gson GSON = new Gson();
    private final Lua lua;
    private final DiscordPresenceObject object;
    private final Platform platform;
    private final SystemInfo systemInfo;

    public DiscordPresenceApi(Lua lua, DiscordPresenceObject object, Platform platform, SystemInfo systemInfo) {
        this.lua = lua;
        this.object = object;
        this.platform = platform;
        this.systemInfo = systemInfo;
    }

    public void sayHi() {
        System.out.println("Hello!");
    }

    /// Set the `details` section of the RPC card in Discord
    public void setDetails(String details) {
        object.setDetails(details);
    }

    /// Set the `state` section of the RPC card in Discord
    public void setState(String state) {
        object.setState(state);
    }

    /// Set the `title` section of the RPC card in Discord
    public void setTitle(String title) {
        object.setLargeText(title);
    }

    /// Set the `type` section of the RPC card in Discord
    public void setType(String type) {
        object.setActivityType(ActivityType.valueOf(type.toUpperCase()));
    }

    public void setTimestampStart(long activityTimestampStart) {
        object.setActivityTimestampStart(activityTimestampStart);
    }

    public void setTimestampEnd(long activityTimestampEnd) {
        object.setActivityTimestampEnd(activityTimestampEnd);
    }

    public void setLargeText(String largeText) {
        object.setLargeText(largeText);
    }

    public void setSmallText(String smallText) {
        object.setSmallText(smallText);
    }

    public void setLargeImageKey(String largeImageKey) {
        object.setLargeImageKey(largeImageKey);
    }

    public void setSmallImageKey(String smallImageKey) {
        object.setSmallImageKey(smallImageKey);
    }

    public void setCurrentPartySize(int size) {
        object.setCurrentPartySize(size);
    }

    public void setMaxPartySize(int size) {
        object.setMaxPartySize(size);
    }

    public void setButtonALabel(String label) {
        object.setButtonALabel(label);
    }

    public void setButtonAUrl(String label) {
        object.setButtonAUrl(label);
    }

    public void setButtonBLabel(String label) {
        object.setButtonBLabel(label);
    }

    public void setButtonBUrl(String label) {
        object.setButtonBUrl(label);
    }

    /// Gets the {@link Platform} object
    public Platform getPlatform() {
        return platform;
    }

    /// Gets the OSHI {@link SystemInfo} object
    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    /// Creates an HTTP request with the `method` from the specified `url`
    public Object httpRequest(String link, Map<String, Object> requestParams) {
        Objects.requireNonNull(requestParams, "missing request parameters");

        if (!requestParams.containsKey("method")) {
            throw new IllegalArgumentException("missing method from requestParams");
        }

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(link));

            String method = String.valueOf(requestParams.get("method")).toUpperCase();
            switch (method) {
                case "GET", "HEAD", "DELETE" -> requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
                case "POST", "PUT" -> {
                    if (!requestParams.containsKey("payload")) {
                        throw new IllegalArgumentException("Missing payload from requestParams");
                    }
                    requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(String.valueOf(requestParams.get("payload"))));
                }
                default -> throw new IllegalStateException("method " + method + " not supported");
            }

            HttpRequest request = requestBuilder.build();

            try {
                return client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                throw new RuntimeException("Request failed", e);
            }
        }
    }

    /// Decodes the given JSON using the {@link Gson} library.
    public LuaValue jsonDecode(String contents) {
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> result = GSON.fromJson(contents, type);
        lua.checkStack(1);
        lua.push(result, Lua.Conversion.FULL);
        return lua.get();
    }
}

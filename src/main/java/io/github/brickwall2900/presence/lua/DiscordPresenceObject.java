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

import de.jcm.discordgamesdk.activity.ActivityType;

public class DiscordPresenceObject {
    private String largeText = "Unknown";
    private String smallText = "unknown";

    private String largeImageKey = "computer";
    private String smallImageKey = null;

    private String details = "Unknown";
    private String state = "Unknown";

    private ActivityType activityType = ActivityType.PLAYING;

    private long activityTimestampStart, activityTimestampEnd;
    private boolean hasTimestampStart, hasTimestampEnd;

    private PartySize partySize;
    private Button buttonA, buttonB;

    public static class PartySize {
        public int current, max;
    }

    public static class Button {
        public String label = "Unknown", url = "https://www.google.com";
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLargeText() {
        return largeText;
    }

    public void setLargeText(String largeText) {
        this.largeText = largeText;
    }

    public long getActivityTimestampStart() {
        return activityTimestampStart;
    }

    public void setActivityTimestampStart(long activityTimestampStart) {
        this.activityTimestampStart = activityTimestampStart;
        this.hasTimestampStart = true;
    }

    public long getActivityTimestampEnd() {
        return activityTimestampEnd;
    }

    public void setActivityTimestampEnd(long activityTimestampEnd) {
        this.activityTimestampEnd = activityTimestampEnd;
        this.hasTimestampEnd = true;
    }

    public boolean hasTimestampStart() {
        return hasTimestampStart;
    }

    public boolean hasTimestampEnd() {
        return hasTimestampEnd;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getSmallText() {
        return smallText;
    }

    public void setSmallText(String smallText) {
        this.smallText = smallText;
    }

    public String getLargeImageKey() {
        return largeImageKey;
    }

    public void setLargeImageKey(String largeImageKey) {
        this.largeImageKey = largeImageKey;
    }

    public String getSmallImageKey() {
        return smallImageKey;
    }

    public void setSmallImageKey(String smallImageKey) {
        this.smallImageKey = smallImageKey;
    }

    public boolean hasParty() {
        return partySize != null;
    }

    public void setMaxPartySize(int size) {
        if (partySize == null) {
            partySize = new PartySize();
            partySize.current = size;
        }
        partySize.max = size;
    }

    public void setCurrentPartySize(int size) {
        if (partySize == null) {
            partySize = new PartySize();
            partySize.max = size;
        }
        partySize.current = size;
    }

    public int getMaxPartySize() {
        return partySize != null ? partySize.max : 0;
    }

    public int getCurrentPartySize() {
        return partySize != null ? partySize.current : 0;
    }

    public void setButtonALabel(String label) {
        if (buttonA == null) {
            buttonA = new Button();
        }

        buttonA.label = label;
    }

    public void setButtonAUrl(String label) {
        if (buttonA == null) {
            buttonA = new Button();
        }

        buttonA.url = label;
    }

    public void setButtonBLabel(String label) {
        if (buttonB == null) {
            buttonB = new Button();
        }

        buttonB.label = label;
    }

    public void setButtonBUrl(String label) {
        if (buttonB == null) {
            buttonB = new Button();
        }

        buttonB.url = label;
    }

    public Button getButtonA() {
        return buttonA;
    }

    public Button getButtonB() {
        return buttonB;
    }
}

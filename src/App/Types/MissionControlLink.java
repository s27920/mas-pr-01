package App.Types;

import App.Models.Mission.Mission;

public record MissionControlLink(Mission mission, Runnable runnable) {
}

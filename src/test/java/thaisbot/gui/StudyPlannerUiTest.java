package thaisbot.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class StudyPlannerUiTest {
    @Test
    public void welcomeAndErrorMessages_useNeutralWording() {
        StudyPlannerUi ui = new StudyPlannerUi();

        ui.showWelcome();
        ui.showError("bad command");

        assertEquals(List.of(
                "Hello! I'm Thai's Bot.",
                "What can I do for you today?",
                "Error: bad command"
        ), ui.drainMessages());
    }
}

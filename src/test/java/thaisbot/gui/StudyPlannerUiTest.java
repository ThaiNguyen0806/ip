package thaisbot.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class StudyPlannerUiTest {
    @Test
    public void welcomeAndErrorMessages_useNewPersona() {
        StudyPlannerUi ui = new StudyPlannerUi();

        ui.showWelcome();
        ui.showError("bad command");

        assertEquals(List.of(
                "Hello! I'm Thai's Bot. Let's make the chaos manageable.",
                "Bring me tasks, deadlines, events, and tags.",
                "Oops — Thai's Bot got tangled up: bad command"
        ), ui.drainMessages());
    }
}

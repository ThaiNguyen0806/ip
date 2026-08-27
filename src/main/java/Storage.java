import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path dataFile;
    private final Parser parser;

    public Storage(String filePath, Parser parser) {
        dataFile = Paths.get(filePath);
        this.parser = parser;
    }

    public TaskList load() throws ThaisBotException {
        try {
            Files.createDirectories(dataFile.getParent());
            if (!Files.exists(dataFile)) {
                Files.createFile(dataFile);
                return new TaskList();
            }

            List<String> lines = Files.readAllLines(dataFile);
            ArrayList<Task> tasks = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    tasks.add(parseTaskLine(line, i + 1));
                }
            }
            return new TaskList(tasks);
        } catch (IOException e) {
            throw new ThaisBotException("I couldn't load tasks from disk.");
        }
    }

    public void save(TaskList tasks) throws ThaisBotException {
        try {
            Files.createDirectories(dataFile.getParent());
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileString());
            }
            Files.write(dataFile, lines);
        } catch (IOException e) {
            throw new ThaisBotException("I couldn't save your tasks to disk.");
        }
    }

    private Task parseTaskLine(String line, int lineNumber) throws ThaisBotException {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3) {
            throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
        }

        String taskType = parts[0];
        String statusFlag = parts[1];
        String description = parts[2];
        Task task;

        if ("T".equals(taskType)) {
            task = new Todo(description);
        } else if ("D".equals(taskType)) {
            task = parseDeadlineTask(parts, description, lineNumber);
        } else if ("E".equals(taskType)) {
            task = parseEventTask(parts, description, lineNumber);
        } else {
            throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
        }

        if ("1".equals(statusFlag)) {
            task.setStatus(TaskStatus.DONE);
        } else if ("0".equals(statusFlag)) {
            task.setStatus(TaskStatus.NOT_DONE);
        } else {
            throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
        }

        return task;
    }

    private Task parseDeadlineTask(String[] parts, String description, int lineNumber)
            throws ThaisBotException {
        if (parts.length < 4) {
            throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
        }

        Parser.ParsedDateTime by;
        if (parts.length >= 5) {
            by = parseStoredDateTimeWithFlag(parts[3], parts[4], lineNumber);
        } else {
            by = parser.parseDateTime(parts[3],
                    "Saved task data is corrupted at line " + lineNumber + ".");
        }
        return new Deadline(description, by.getValue(), by.hasTime());
    }

    private Task parseEventTask(String[] parts, String description, int lineNumber)
            throws ThaisBotException {
        if (parts.length < 5) {
            throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
        }

        Parser.ParsedDateTime from;
        Parser.ParsedDateTime to;
        if (parts.length >= 7) {
            from = parseStoredDateTimeWithFlag(parts[3], parts[4], lineNumber);
            to = parseStoredDateTimeWithFlag(parts[5], parts[6], lineNumber);
        } else {
            from = parser.parseDateTime(parts[3],
                    "Saved task data is corrupted at line " + lineNumber + ".");
            to = parser.parseDateTime(parts[4],
                    "Saved task data is corrupted at line " + lineNumber + ".");
        }
        return new Event(description, from.getValue(), from.hasTime(), to.getValue(), to.hasTime());
    }

    private Parser.ParsedDateTime parseStoredDateTimeWithFlag(String value, String flag,
                                                              int lineNumber)
            throws ThaisBotException {
        try {
            LocalDateTime parsed = LocalDateTime.parse(value);
            if ("1".equals(flag)) {
                return new Parser.ParsedDateTime(parsed, true);
            }
            if ("0".equals(flag)) {
                return new Parser.ParsedDateTime(parsed, false);
            }
        } catch (Exception e) {
            // Fall through to the uniform error below.
        }
        throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
    }
}

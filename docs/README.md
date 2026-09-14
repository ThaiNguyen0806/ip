# Thai's Bot User Guide

![Thai's Bot screenshot](Ui.png)

Thai's Bot is a simple task tracker with a playful academic voice. Use it to manage todos, deadlines, events, and tags from a single command box.

## Quick start

1. Launch the app.
2. Type a command in the input box.
3. Press **Enter** or click **Send**.

## Commands

### Add a todo

```text
todo <description> [#tag ...]
```

Example:

```text
todo read book #school #revision
```

Adds a simple task. Tags are optional.

### Add a deadline

```text
deadline <description> /by <date> [#tag ...]
```

Examples:

```text
deadline submit lab report /by 2026-09-18 #school #lab
deadline submit lab report /by 2026-09-18 2359 #school #lab
```

Use `yyyy-MM-dd` for dates. You may also include time with `yyyy-MM-dd HHmm`.

### Add an event

```text
event <description> /from <start> /to <end> [#tag ...]
```

Example:

```text
event project meeting /from 2026-09-15 1400 /to 2026-09-15 1600 #project
```

### Show all tasks

```text
list
```

Shows every task in the current list.

### Find tasks

```text
find <keyword>
```

Example:

```text
find book
```

Searches task descriptions and tags.

### Show tasks on a date

```text
on <date>
```

Example:

```text
on 2026-09-15
```

Shows deadlines and events that fall on that date.

### Mark a task as done or not done

```text
mark <task number>
unmark <task number>
```

Example:

```text
mark 2
```

### Delete a task

```text
delete <task number>
```

Example:

```text
delete 3
```

### Exit the app

```text
bye
```

## Tags

Add tags using `#tag` after the main command text.

- Tags use letters and numbers only.
- Duplicate tags are ignored.
- Tags appear in `list` output and are saved with the task.

Examples:

```text
todo read book #school #revision
deadline submit report /by 2026-09-18 #school
```

## Common errors

If you type an invalid command, Thai's Bot shows an error message and keeps running.
If the saved data file cannot be found or read, Thai's Bot shows a clear error and starts with an empty list when possible.

## Notes

- The product name is **Thai's Bot**.
- The GUI screenshot is stored as `Ui.png` in this folder.
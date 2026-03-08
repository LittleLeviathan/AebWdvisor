package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MacroCommand - Executes multiple commands as one transaction
 */
@Table(name = "command_history", isSubTable = true)
public class MacroCommand extends BaseCommand {
    private List<BaseCommand> commands;
    private String description;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public MacroCommand() {
        this("Initialized Macro");
    }

    public MacroCommand(String description) {
        super();
        this.commandType = "MACRO";
        this.description = description;
        this.commands    = new ArrayList<>();
    }

    public void addCommand(BaseCommand command) {
        commands.add(command);
    }

    public static MacroCommand fromSuperType(BaseCommand base) {
        MacroCommand cmd = new MacroCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();
        System.out.printf("▶ Executing macro: %s (%d commands)%n", description, commands.size());

        for (BaseCommand command : commands) {
            command.execute();
            if (!command.wasSuccessful()) {
                System.out.println("  ✗ Sub-command failed: " + command.getDescription());
                successful = false;
                executed   = true;
                System.out.println("✗ Macro failed — rolling back completed sub-commands");
                undo();
                return;
            }
        }

        executed   = true;
        successful = true;
        System.out.println("✓ Macro completed successfully");
    }

    @Override
    public void undo() {
        if (!executed) return;
        System.out.printf("↶ Undoing macro: %s%n", description);
        // Undo in reverse order (i.e. only commands that actually succeeded)
        for (int i = commands.size() - 1; i >= 0; i--) {
            BaseCommand cmd = commands.get(i);
            if (cmd.wasSuccessful()) {
                cmd.undo();
            }
        }
        this.undoneAt = LocalDateTime.now();
        this.isUndone = true;
    }

    @Override
    public boolean isUndoable() {
        return executed && commands.stream().allMatch(BaseCommand::isUndoable);
    }

    @Override
    public String getDescription() {
        return String.format("%s (Macro: %d commands)", description, commands.size());
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < commands.size(); i++) {
            BaseCommand bc = commands.get(i);
            Map<String, Object> entry = new HashMap<>();
            entry.put("type",  bc.getClass().getName());  // fully-qualified, no "class " prefix
            entry.put("index", i);
            entry.put("data",  bc.serializeCommandData());
            list.add(entry);
        }
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize MacroCommand data", e);
        }
    }

    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Map<String, Object>> list =
                    mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            this.commands = new ArrayList<>(list.size());

            for (Map<String, Object> entry : list) {
                String className    = (String) entry.get("type");
                String subData      = mapper.writeValueAsString(entry.get("data"));
                BaseCommand subCmd  = instantiateCommand(className);
                subCmd.setCommandData(subData);
                subCmd.initAfterLoad();
                this.commands.add(subCmd);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize MacroCommand data", e);
        }
    }

    private BaseCommand instantiateCommand(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return (BaseCommand) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate command class: " + className, e);
        }
    }
}
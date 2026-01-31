package com.example.demo.model;

public class FormControl {
    private String type;
    private String name;
    private String label;
    private boolean mandatory;
    private String[] options;
    // New fields for custom validation
    private String regex;
    private String validationMessage;

    // Extended constructor for regex validation
    public FormControl(String type, String name, String label, boolean mandatory, String regex, String validationMessage, String... options) {
        this.type = type;
        this.name = name;
        this.label = label;
        this.mandatory = mandatory;
        this.regex = regex;
        this.validationMessage = validationMessage;
        this.options = options;
    }

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public boolean isMandatory() { return mandatory; }
    public void setMandatory(boolean mandatory) { this.mandatory = mandatory; }
    public String[] getOptions() { return options; }
    public void setOptions(String[] options) { this.options = options; }
    public String getRegex() { return regex; }
    public void setRegex(String regex) { this.regex = regex; }
    public String getValidationMessage() { return validationMessage; }
    public void setValidationMessage(String validationMessage) { this.validationMessage = validationMessage; }
}
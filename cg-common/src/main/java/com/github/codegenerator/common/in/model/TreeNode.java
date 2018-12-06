package com.github.codegenerator.common.in.model;

import java.util.List;
import java.util.Map;

public class TreeNode {

    private Long id;
    private String text;
    private String state;
    private List<TreeNode> children;
    private Map<String,String> attributes;
    private boolean checked;


    public TreeNode(Long id, String text, String state, List<TreeNode> children, Map<String, String> attributes, boolean checked) {
        this.id = id;
        this.text = text;
        this.state = state;
        this.children = children;
        this.attributes = attributes;
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

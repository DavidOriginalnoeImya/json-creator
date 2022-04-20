package entities;

import java.util.ArrayList;
import java.util.List;

public class FSDirectory extends FSBase {
    private List<FSBase> content = new ArrayList<>();

    public FSDirectory() {
    }

    public FSDirectory(String name) {
        setName(name);
    }

    public List<FSBase> getContent() {
        return content;
    }

    public void setContent(List<FSBase> children) {
        this.content = children;
    }

    public void addChild(FSBase child) {
        content.add(child);
    }
}

package entities;

import com.fasterxml.jackson.annotation.JsonInclude;

public class FSFile extends FSBase {
    private long size = 0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String blob = null;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getBlob() {
        return blob;
    }

    public void setBlob(String blob) {
        this.blob = blob;
    }

    public FSFile() {
    }

    public FSFile(String name, long size, String blob) {
        setName(name);
        this.size = size;
        this.blob = blob;
    }
}

package io.alkal.demos.key_rotations.models;

/**
 * @author Ziv Salzman
 * Created on 27-Apr-2019
 */
public class PublicKey {

    private String type;
    private String value;

    public PublicKey() {

    }

    public PublicKey(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}

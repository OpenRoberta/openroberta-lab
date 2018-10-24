package de.fhg.iais.roberta.util;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.util.dbc.Assert;

public class DropDowns {
    private final Map<String, DropDown> dropDowns = new HashMap<>();

    public DropDowns() {
        // create map
    }

    public void add(String name, String left, String right) {
        Assert.notNull(name);
        DropDown dropDown = dropDowns.get(name);
        if ( dropDown == null ) {
            dropDown = new DropDown();
            dropDowns.put(name, dropDown);
        }
        dropDown.add(left, right);
    }

    public DropDown get(String name) {
        Assert.notNull(name);
        return dropDowns.get(name);
    }
}

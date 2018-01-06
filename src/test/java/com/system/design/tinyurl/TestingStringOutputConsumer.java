package com.system.design.tinyurl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestingStringOutputConsumer implements Consumer<String> {

    private List<String> output = new ArrayList<>();

    @Override
    public void accept(String outputString) {
        output.add(outputString);
    }

    public List<String> output() {
        return output;
    }
}

package com.github.br.starmarines.ui.api.utils;

import javafx.concurrent.Task;

/**
 * Created by user on 03.02.2018.
 */
public abstract class VoidTask extends Task<Void> {
    @Override
    protected Void call() throws Exception {
        callWithoutResult();
        return null;
    }

    protected abstract void callWithoutResult();
}

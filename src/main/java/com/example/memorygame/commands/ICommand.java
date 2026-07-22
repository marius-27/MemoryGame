package com.example.memorygame.commands;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Echivalentul interfeței System.Windows.Input.ICommand din WPF.
 * Extinde EventHandler&lt;ActionEvent&gt; astfel încât o implementare poate fi
 * atașată direct pe un control JavaFX, ex: button.setOnAction(command).
 */
public interface ICommand extends EventHandler<ActionEvent> {

    void execute(Object parameter);

    boolean canExecute(Object parameter);

    void addCanExecuteChangedListener(Runnable listener);

    void removeCanExecuteChangedListener(Runnable listener);

    @Override
    default void handle(ActionEvent event) {
        execute(null);
    }
}
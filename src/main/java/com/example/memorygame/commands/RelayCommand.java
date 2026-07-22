package com.example.memorygame.commands;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Echivalentul RelayCommand / DelegateCommand din WPF.
 * Încapsulează o acțiune (execute) și, opțional, o condiție (canExecute).
 *
 * Pe lângă contractul ICommand, expune o proprietate JavaFX (executableProperty)
 * care poate fi legată direct de UI, ex:
 *      button.disableProperty().bind(command.executableProperty().not());
 * asta e alternativa "cu Data Binding" la interogarea manuală CanExecute din WPF.
 */
public class RelayCommand implements ICommand {

    private final Consumer<Object> executeAction;
    private final Predicate<Object> canExecuteFunc;
    private final List<Runnable> listeners = new ArrayList<>();
    private final BooleanProperty executable = new SimpleBooleanProperty(true);

    public RelayCommand(Runnable action) {
        this(param -> action.run(), param -> true);
    }

    public RelayCommand(Runnable action, Supplier<Boolean> canExecute) {
        this(param -> action.run(), param -> canExecute.get());
    }

    public RelayCommand(Consumer<Object> executeAction, Predicate<Object> canExecuteFunc) {
        this.executeAction = executeAction;
        this.canExecuteFunc = canExecuteFunc;
        this.executable.set(canExecuteFunc.test(null));
    }

    @Override
    public void execute(Object parameter) {
        if (canExecute(parameter)) {
            executeAction.accept(parameter);
        }
    }

    @Override
    public boolean canExecute(Object parameter) {
        return canExecuteFunc.test(parameter);
    }

    @Override
    public void addCanExecuteChangedListener(Runnable listener) {
        listeners.add(listener);
    }

    @Override
    public void removeCanExecuteChangedListener(Runnable listener) {
        listeners.remove(listener);
    }

    /**
     * Echivalentul CommandManager.InvalidateRequerySuggested() din WPF.
     * Apeleaz-o din ViewModel ori de câte ori se schimbă o stare de care
     * depinde canExecute (ex: după ce ai modificat o proprietate legată în UI).
     */
    public void raiseCanExecuteChanged() {
        executable.set(canExecuteFunc.test(null));
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public BooleanProperty executableProperty() {
        return executable;
    }
}

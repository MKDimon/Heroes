package heroes.gui;

/**
 * Интерфейс для передачи GUI терминала в сущности без изменения исходного кода.
 */
public interface Visualisable {
    /**
     * Присваивает внутренней переменной терминала новое значение.
     * @param tw Обёртка над инфраструктурой GUI Lanterna.
     */
    void setTerminal(final TerminalWrapper tw);
}

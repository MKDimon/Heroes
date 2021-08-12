package heroes.gui;


/**
 * Интерфейс для передачи GUI терминала в сущности без изменения исходного кода.
 */
public interface Visualisable {
    /**
     * Присваивает внутренней переменной терминала новое значение.
     * @param gui Обёртка над инфраструктурой GUI.
     */
    void setTerminal(final IGUI gui);
}

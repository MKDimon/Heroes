package heroes.statistics;

/**
 * Класс, в котором происходит обработка и запись результатов обработки статистики по ботам отдельно от
 * обработки всей остальной статистики. Для удобства.
 **/

public class BotsStatisticsRecorder {

    public static void main(final String[] args) {
        StatisticsRecorder.recordBotsStatistics();
    }
}

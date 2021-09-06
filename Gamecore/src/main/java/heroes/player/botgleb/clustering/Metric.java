package heroes.player.botgleb.clustering;

@FunctionalInterface
public interface Metric <T> {
    double getDistance(final T o1, T o2);
}

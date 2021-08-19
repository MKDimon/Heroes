package heroes.player.botgleb.fann;

import com.googlecode.fannj.*;

import java.util.Arrays;

public class FANN {
    private static final Fann fann = new Fann(Arrays.asList(
            Layer.create(60, ActivationFunction.FANN_SIGMOID),
            Layer.create(60, ActivationFunction.FANN_SIGMOID),
            Layer.create(1)));

    public static void main(final String[] args) {
        trainFann(BoardToCSVRecorder.trainData);
    }

    private static void trainFann(final String trainData) {
        final Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_RPROP);
        trainer.train(trainData, 100000, 1000, 0.0001f);
        fann.save("ann");
    }

    public static float[] getPred(final float[] input) {
        return fann.run(input);
    }

}

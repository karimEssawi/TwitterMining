package Mallet;

import java.io.Serializable;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.Instance;
import cc.mallet.types.LabelVector;

public class SVM extends Classifier implements Serializable
{
	private static final long serialVersionUID = -3273222430839071709L;

	public static svm_node[] getSvmNodes(Instance instance)
	{
		FeatureVector fv = (FeatureVector) instance.getData();
		svm_node[] ret = null;
		if (fv.numLocations() > 0)
		{
			ret = new svm_node[fv.numLocations()];
			for (int loc = 0; loc < fv.numLocations(); loc++)
			{
				ret[loc] = new svm_node();
				ret[loc].index = fv.indexAtLocation(loc) + 1;
				ret[loc].value = fv.valueAtLocation(loc);
			}
		}

		return ret;
	}

	private svm_model model;

	public SVM(svm_model model, Pipe pipe)
	{
		super(pipe);

		this.model = model;
	}

	@Override
	public Classification classify(Instance instance)
	{
		svm_node[] input = getSvmNodes((Instance) instance.clone());

		int nr_class = svm.svm_get_nr_class(model);
		int[] labels = new int[nr_class];

		svm.svm_get_labels(model, labels);

		double[] prob_estimates = new double[nr_class];
		if (svm.svm_check_probability_model(model) == 0)
		{
			svm.svm_predict(model, input);
		}
		else
		{
			svm.svm_predict_probability(model, input, prob_estimates);
		}

		double[] scores = new double[labels.length];

		for (int loc = 0; loc < labels.length; loc++)
		{
			int labelId = labels[loc];
			if (scores.length <= labelId)
			{
				continue;
			}
			scores[labelId] = prob_estimates[loc];
		}

		LabelVector labelVector = new LabelVector(getLabelAlphabet(), scores);
		return new Classification(instance, this, labelVector);
	}

}

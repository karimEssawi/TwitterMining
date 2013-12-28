package Mallet;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Label;

public class SVMTrainer extends ClassifierTrainer<SVM> {

	public static InstanceList copy(InstanceList instances) {
		InstanceList ret = (InstanceList) instances.clone();
		// LabelAlphabet labelDict = (LabelAlphabet) ret.getTargetAlphabet();
		Alphabet featDict = ret.getDataAlphabet();

		for (int i = 0; i < ret.size(); i++) {
			Instance instance = ret.get(i);
			Instance clone = (Instance) instance.clone();
			FeatureVector fv = (FeatureVector) clone.getData();

			int[] indices = fv.getIndices();
			double[] values = fv.getValues();

			int[] newIndices = new int[indices.length];
			System.arraycopy(indices, 0, newIndices, 0, indices.length);

			double[] newValues = new double[indices.length];
			System.arraycopy(values, 0, newValues, 0, indices.length);

			FeatureVector newFv = new FeatureVector(featDict, newIndices, newValues);
			Instance newInstance = new Instance(newFv, instance.getTarget(), instance.getName(), instance.getSource());
			ret.set(i, newInstance);
		}

		return ret;
	}

	public static InstanceList scale(InstanceList trainingList, double lower, double upper) {
		InstanceList ret = copy(trainingList);
		Alphabet featDict = ret.getDataAlphabet();

		double[] feat_max = new double[featDict.size()];
		double[] feat_min = new double[featDict.size()];

		for (int i = 0; i < feat_max.length; i++) {
			feat_max[i] = -Double.MAX_VALUE;
			feat_min[i] = Double.MAX_VALUE;
		}

		for (int i = 0; i < ret.size(); i++) {
			Instance inst = ret.get(i);
			FeatureVector fv = (FeatureVector) inst.getData();

			for (int loc = 0; loc < fv.numLocations(); loc++) {
				int featId = fv.indexAtLocation(loc);
				double value = fv.valueAtLocation(loc);
				double maxValue = feat_max[featId];
				double minValue = feat_min[featId];

				double newMaxValue = Math.max(value, maxValue);
				double newMinValue = Math.min(value, minValue);

				feat_max[featId] = newMaxValue;
				feat_min[featId] = newMinValue;
			}
		}

		// double lower = -1;
		// double upper = 1;

		for (int i = 0; i < ret.size(); i++) {
			Instance inst = ret.get(i);
			FeatureVector fv = (FeatureVector) inst.getData();

			for (int loc = 0; loc < fv.numLocations(); loc++) {
				int featId = fv.indexAtLocation(loc);
				double value = fv.valueAtLocation(loc);
				double maxValue = feat_max[featId];
				double minValue = feat_min[featId];
				double newValue = Double.NaN;
				if (maxValue == minValue) {
					newValue = value;
				} else if (value == minValue) {
					newValue = lower;
				} else if (value == maxValue) {
					newValue = upper;
				} else {
					newValue = lower + (upper - lower) * (value - minValue) / (maxValue - minValue);
				}

				fv.setValueAtLocation(loc, newValue);
			}
		}

		return ret;
	}

	private svm_parameter param;

	private SVM classifier;

	public SVMTrainer() {
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0; // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 0;
		param.probability = 1;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		setParameter(param);
	}

	public SVMTrainer(svm_parameter param) {
		this.param = param;
	}

	@Override
	public SVM getClassifier() {
		return classifier;
	}

	public void setParameter(svm_parameter param) {
		this.param = param;
	}

	public SVM train(InstanceList trainingList) {
		svm_problem problem = new svm_problem();
		problem.l = trainingList.size();
		problem.x = new svm_node[problem.l][];
		problem.y = new double[problem.l];

		for (int i = 0; i < trainingList.size(); i++) {
			Instance instance = trainingList.get(i);
			svm_node[] input = SVM.getSvmNodes(instance);
			if (input == null) {
				continue;
			}
			int labelIndex = ((Label) instance.getTarget()).getIndex();
			problem.x[i] = input;
			problem.y[i] = labelIndex;
		}

		int max_index = trainingList.getDataAlphabet().size();

		if (param.gamma == 0 && max_index > 0) {
			param.gamma = 1.0 / max_index;
		}

		// int numLabels = trainingList.getTargetAlphabet().size();
		// int[] weight_label = new int[numLabels];
		// double[] weight = trainingList.targetLabelDistribution().getValues();
		// double minValue = Double.MAX_VALUE;
		//
		// for (int i = 0; i < weight.length; i++) {
		// if (minValue > weight[i]) {
		// minValue = weight[i];
		// }
		// }
		//
		// for (int i = 0; i < weight.length; i++) {
		// weight_label[i] = i;
		// weight[i] = weight[i] / minValue;
		// }
		//
		// param.weight_label = weight_label;
		// param.weight = weight;

		String error_msg = svm.svm_check_parameter(problem, param);

		if (error_msg != null) {
			System.err.print("Error: " + error_msg + "\n");
			System.exit(1);
		}

		svm_model model = svm.svm_train(problem, param);

		classifier = new SVM(model, trainingList.getPipe());

		return classifier;
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mallet;

/**
 *
 * @author Karim
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Label;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelVector;
import cc.mallet.types.Metric;

public class KNN extends Classifier {
private static final long serialVersionUID = 1L;

private int k;
private Metric metric;
private InstanceList instances;

public KNN(int k, Metric m, InstanceList il) {
this.k = k;
this.metric = m;
this.instances = il;
}

@Override
public Classification classify(Instance instance) {
// find the K nearest neighbours
Instance[] nn = knn(metric, k, instance, instances);

// compute classification (k should be odd to avoid ties)

// sum winning labels
Map<Label, Integer> labelsCounts = new HashMap<Label, Integer>();
for (Instance i : nn) {
Integer counts;
Label l = (Label)i.getTarget();
if(labelsCounts.containsKey(l)) counts = labelsCounts.get(l) + 1;
else counts = 1;
labelsCounts.put(l, counts);
}

LabelAlphabet targetAlphabet = (LabelAlphabet) instance.getTargetAlphabet();
double[] counts = new double[targetAlphabet.size()];
for (Entry<Label, Integer> lc : labelsCounts.entrySet())
counts[lc.getKey().getIndex()] = lc.getValue();

return new Classification(instance, this, new LabelVector(targetAlphabet, counts));
}

/**
* Returns the "k" nearest instances of the specified instance, when computed
* using the specified metric.
*
* @param metric The metric to use for calculating the similarity/distance between instances
* @param k The number of nearest neighbours to retrieve
* @param instance The medoid instance
* @param instances The space of instances to search in
* @return The K nearest neighbours of the medoid instance
*/
public static Instance[] knn(Metric metric, int k, Instance instance, InstanceList instances) {
// k is larger than the number of instances; return all
if(k >= instances.size()) return instances.toArray(new Instance[0]);

KInstanceCollection kic = new KInstanceCollection(k);

// compute the distance of the medoid to every instance in the instancelist
// hold K nearest in the KInstanceCollection
FeatureVector instanceVector = (FeatureVector)instance.getData();
for (Instance inst : instances) {
if(!instance.equals(inst)) {
// key: distance, value: instance
kic.put(
metric.distance(instanceVector, (FeatureVector)inst.getData()),
inst
);
}
}

return kic.getValues();
}
}

/**
* Custom collection that stores the K "items" with smallest "cost".
* Does not need to hold all the items in memory, just the K with lowest cost.
* Useful when running on a large instancelist.
*
* @author tt
*/
class KInstanceCollection {
// this only stores K items
// as long as K is smallish, it's hardly worth it to optimize this
// (e.g. by keeping the arrays sorted)
private final int maxN;
private final double[] costs;
private final ArrayList<Instance> items;

public KInstanceCollection(int maxN) {
this.maxN = maxN;
this.costs = new double[maxN];
this.items = new ArrayList<Instance>(maxN);
Arrays.fill(costs, Double.NaN);
}

public void put(double cost, Instance item) {
int idx = isLesserCost(cost, costs);
if(idx!=-1) {
costs[idx] = cost;
items.add(idx, item);

int s = items.size();
if(s > maxN) items.remove(s-1);
}
}

// returns the index of the item with distance > cost or -1 if cost is the highest
private static final int isLesserCost(double cost, double[] costs) {
for (int i=0; i<costs.length; ++i)
if(Double.isNaN(costs[i]) || costs[i] > cost)
return i;

return -1;
}

public double[] getCosts() {
return costs;
}

public Instance[] getValues() {
return items.toArray(new Instance[0]);
}
}

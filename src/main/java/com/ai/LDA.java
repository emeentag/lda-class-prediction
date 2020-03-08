package com.ai;

/**
 * Copyright (c) 2010, Dr. Wolfgang Lenhard, Psychometrica.de
 * All rights reserved.
 *
 * This code serves for calculating a linear discriminant analysis (LDA) and it is based on the
 * tutorial of Kardi Teknomo (http://people.revoledu.com/kardi/tutorial/LDA/index.html). You will
 * need JAMA (A Java Matrix Package; http://math.nist.gov/javanumerics/jama/) to run this routines.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Dr. Wolfgang Lenhard, 2010
 * @version 0.1, 07/31/2010
 *
 * Quotation:
 * Lenhard, W. (2010). Realisation of Linear Discriminant Analysis in Java. Bibergau: Psychometrica.
 * 
 * 
 */
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import Jama.Matrix;

public class LDA {
	private double[][] groupMean;
	private double[][] pooledInverseCovariance;
	private double[] probability;
	private ArrayList<Integer> groupList = new ArrayList<Integer>();

	/**
	 * Calculates a linear discriminant analysis (LDA) with all necessary
	 * 
	 * @param data  The data as double array. The array must have the same size as
	 *              the group array
	 * @param group The membership in the different groups
	 * @param p     Set to true, if the probability estimation should be based on
	 *              the real group sizes (true), or if the share of each group
	 *              should be equal
	 */
	@SuppressWarnings("unchecked")
	public LDA(double[][] d, int[] g, boolean p) {
		// check if data and group array have the same size
		if (d.length != g.length) {
			System.out.println("Data and group array have the same size!");
			return;
		}

		double[][] data = new double[d.length][d[0].length];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[i].length; j++) {
				data[i][j] = d[i][j];
			}
		}
		int[] group = new int[g.length];
		for (int j = 0; j < g.length; j++) {
			group[j] = g[j];
		}

		double[] globalMean;
		double[][][] covariance;

		// determine number and label of groups
		for (int i = 0; i < group.length; i++) {
			if (!groupList.contains(group[i])) {
				groupList.add(group[i]);
			}
		}

		// divide data into subsets
		ArrayList<double[]>[] subset = new ArrayList[groupList.size()];
		for (int i = 0; i < subset.length; i++) {
			subset[i] = new ArrayList<double[]>();
			for (int j = 0; j < data.length; j++) {
				if (group[j] == groupList.get(i)) {
					subset[i].add(data[j]);
				}
			}
		}

		// calculate group mean
		groupMean = new double[subset.length][data[0].length];
		for (int i = 0; i < groupMean.length; i++) {
			for (int j = 0; j < groupMean[i].length; j++) {
				groupMean[i][j] = getGroupMean(j, subset[i]);
			}
		}

		// calculate global mean
		globalMean = new double[data[0].length];
		for (int i = 0; i < data[0].length; i++) {
			globalMean[i] = getGlobalMean(i, data);
		}

		// correct subset data
		for (int i = 0; i < subset.length; i++) {
			for (int j = 0; j < subset[i].size(); j++) {
				double[] v = subset[i].get(j);

				for (int k = 0; k < v.length; k++)
					v[k] = v[k] - globalMean[k];

				subset[i].set(j, v);
			}
		}

		// calculate covariance
		covariance = new double[subset.length][globalMean.length][globalMean.length];
		for (int i = 0; i < covariance.length; i++) {
			for (int j = 0; j < covariance[i].length; j++) {
				for (int k = 0; k < covariance[i][j].length; k++) {
					for (int l = 0; l < subset[i].size(); l++)
						covariance[i][j][k] += (subset[i].get(l)[j] * subset[i].get(l)[k]);

					covariance[i][j][k] = covariance[i][j][k] / subset[i].size();
				}
			}
		}

		// calculate pooled within group covariance matrix and invert it
		pooledInverseCovariance = new double[globalMean.length][globalMean.length];
		for (int j = 0; j < pooledInverseCovariance.length; j++) {
			for (int k = 0; k < pooledInverseCovariance[j].length; k++) {
				for (int l = 0; l < subset.length; l++) {
					pooledInverseCovariance[j][k] += ((double) subset[l].size() / (double) data.length)
							* covariance[l][j][k];
				}
			}
		}

		pooledInverseCovariance = new Matrix(pooledInverseCovariance).inverse().getArray();

		// calculate probability for different groups
		this.probability = new double[subset.length];
		if (!p) {
			double prob = 1.0d / groupList.size();
			for (int i = 0; i < groupList.size(); i++) {
				this.probability[i] = prob;
			}
		} else {
			for (int i = 0; i < subset.length; i++) {
				this.probability[i] = (double) subset[i].size() / (double) data.length;
			}
		}
	}

	private double getGroupMean(int column, ArrayList<double[]> data) {
		double[] d = new double[data.size()];
		for (int i = 0; i < data.size(); i++) {
			d[i] = data.get(i)[column];
		}

		return getMean(d);
	}

	private double getGlobalMean(int column, double data[][]) {
		double[] d = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			d[i] = data[i][column];
		}

		return getMean(d);
	}

	/**
	 * Calculates the discriminant function values for the different groups
	 * 
	 * @param values
	 * @return
	 */
	public double[] getDiscriminantFunctionValues(double[] values) {
		double[] function = new double[groupList.size()];
		for (int i = 0; i < groupList.size(); i++) {
			double[] tmp = matrixMultiplication(groupMean[i], pooledInverseCovariance);
			function[i] = (matrixMultiplication(tmp, values)) - (.5d * matrixMultiplication(tmp, groupMean[i]))
					+ Math.log(probability[i]);
		}

		return function;
	}

	/**
	 * Calculates the discriminant function values for the different groups based on
	 * Mahalanobis distance
	 * 
	 * @param values
	 * @return
	 */
	public double[] getMahalanobisDistance(double[] values) {
		double[] function = new double[groupList.size()];
		for (int i = 0; i < groupList.size(); i++) {
			double[] dist = new double[groupMean[i].length];
			for (int j = 0; j < dist.length; j++)
				dist[j] = values[j] - groupMean[i][j];
			function[i] = matrixMultiplication(matrixMultiplication(dist, this.pooledInverseCovariance), dist);
		}

		double[] returnArray = function.clone();
		ArrayUtils.reverse(returnArray);

		return returnArray;
	}

	/**
	 * Predict the membership of an object to one of the different groups based on
	 * Mahalanobis distance
	 * 
	 * @param values
	 * @return the group
	 */
	public int predictM(double[] values) {
		int group = -1;
		double max = Double.NEGATIVE_INFINITY;
		double[] discr = this.getMahalanobisDistance(values);
		for (int i = 0; i < discr.length; i++) {
			if (discr[i] > max) {
				max = discr[i];
				group = groupList.get(i);
			}
		}

		return group;
	}

	/**
	 * Calculates the probability for the membership in the different groups
	 * 
	 * @param values
	 * @return the probabilities
	 */
	public double[] getProbabilityEstimates(double[] values) {
		// TODO
		return new double[] {};
	}

	/**
	 * Returns the weight for the linear fisher's discrimination functions
	 * 
	 * @return the weights
	 */
	public double[] getFisherWeights() {
		// TODO
		return new double[] {};
	}

	/**
	 * Predict the membership of an object to one of the different groups.
	 * 
	 * @param values
	 * @return the group
	 */
	public int predict(double[] values) {
		int group = -1;
		double max = Double.NEGATIVE_INFINITY;
		double[] discr = this.getDiscriminantFunctionValues(values);
		for (int i = 0; i < discr.length; i++) {
			if (discr[i] > max) {
				max = discr[i];
				group = groupList.get(i);
			}
		}

		return group;
	}

	/**
	 * Multiplies two matrices and returns the result as a double[][]-array. Please
	 * not, that the number of rows in matrix a must be equal to the number of
	 * columns in matrix b
	 * 
	 * @param a the first matrix
	 * @param b the second matrix
	 * @return the resulting matrix
	 */
	@SuppressWarnings("unused")
	private double[][] matrixMultiplication(final double[][] matrixA, final double[][] matrixB) {
		int rowA = matrixA.length;
		int colA = matrixA[0].length;
		int colB = matrixB[0].length;

		double c[][] = new double[rowA][colB];
		for (int i = 0; i < rowA; i++) {
			for (int j = 0; j < colB; j++) {
				c[i][j] = 0;
				for (int k = 0; k < colA; k++) {
					c[i][j] = c[i][j] + matrixA[i][k] * matrixB[k][j];
				}
			}
		}

		return c;
	}

	/**
	 * Multiplies two matrices and returns the result as a double[]-array. Please
	 * not, that the number of rows in matrix a must be equal to the number of
	 * columns in matrix b
	 * 
	 * @param a the first matrix
	 * @param b the second matrix
	 * @return the resulting matrix
	 */
	private double[] matrixMultiplication(double[] matrixA, double[][] matrixB) {

		double c[] = new double[matrixA.length];
		for (int i = 0; i < matrixA.length; i++) {
			c[i] = 0;
			for (int j = 0; j < matrixB[i].length; j++) {
				c[i] += matrixA[i] * matrixB[i][j];
			}
		}

		return c;
	}

	/**
	 * Multiplies two matrices and returns the result as a double (the second matrix
	 * is transposed automatically). Please note, that the number of rows in matrix
	 * a must be equal to the number of columns in matrix b
	 * 
	 * @param a the first matrix
	 * @param b the second matrix
	 * @return the resulting matrix
	 */
	private double matrixMultiplication(double[] matrixA, double[] matrixB) {

		double c = 0d;
		for (int i = 0; i < matrixA.length; i++) {
			c += matrixA[i] * matrixB[i];
		}

		return c;
	}

	/**
	 * Transposes a matrix
	 * 
	 * @param matrix the matrix to transpose
	 * @return the transposed matrix
	 */
	@SuppressWarnings("unused")
	private double[][] transpose(final double[][] matrix) {
		double[][] trans = new double[matrix[0].length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				trans[j][i] = matrix[i][j];
			}
		}

		return trans;
	}

	/**
	 * Transposes a matrix
	 * 
	 * @param matrix the matrix to transpose
	 * @return the transposed matrix
	 */
	@SuppressWarnings("unused")
	private double[][] transpose(final double[] matrix) {
		double[][] trans = new double[1][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			trans[0][i] = matrix[i];
		}

		return trans;
	}

	/**
	 * Returns the mean of the given values. On error or empty data returns 0.
	 * 
	 * @param values The values.
	 * @return The mean.
	 * @since 1.5
	 */
	public static double getMean(final double[] values) {
		if (values == null || values.length == 0)
			return Double.NaN;

		double mean = 0.0d;

		for (int index = 0; index < values.length; index++)
			mean += values[index];

		return mean / (double) values.length;
	}
}
package net.jobrapido.experiments.services.impl;

import java.math.BigInteger;
import java.util.List;

import net.jobrapido.experiments.entities.Experiment;
import net.jobrapido.experiments.entities.ExperimentUser;
import net.jobrapido.experiments.entities.ExperimentVariant;
import net.jobrapido.experiments.services.ConfigurationService;
import net.jobrapido.experiments.services.HashingService;
import net.jobrapido.experiments.services.RandomizationService;
import net.jobrapido.experiments.services.UserAssignmentService;

import com.google.inject.Inject;

public class UserAssignmentServiceDefault implements UserAssignmentService {

	@Inject private ConfigurationService configurationService;
	@Inject private RandomizationService randomizationService;
	@Inject private HashingService hashingService;
	
	@Override
	public Experiment getABTestForUser(ExperimentUser abTestUser) {
		List<Experiment> allActiveABTests = configurationService.getAllActiveABTests();
		long totalActiveTestsWeight = configurationService.getTotalActiveTestsWeight();
		long abTestUserLong = Math.abs(hashingService.toBigInteger(abTestUser.getHashKey()).longValue());
		long res =  abTestUserLong % totalActiveTestsWeight; 
		long aux = 0;
		for (Experiment abTest : allActiveABTests) {
			long nextAux = aux + abTest.getTestWeight();
			if((aux <= res) && (res < nextAux)) return abTest;
			aux = nextAux;
		}
		return null;
	}

	@Override
	public ExperimentVariant getABTestClusterForUserAndABTest(Experiment abTest,
			ExperimentUser abTestUser) {
		List<ExperimentVariant> abTestClusters = abTest.getClusters();
		long totalClusterWeight = configurationService.getTotalTestClustersWeight(abTest);
		BigInteger abTestBigInteger = hashingService.toBigInteger(abTest.getHashKey());
		BigInteger abTestUserBigInteger = hashingService.toBigInteger(abTestUser.getHashKey());
		BigInteger result = abTestBigInteger.xor(abTestUserBigInteger).abs();
		
		
		long res = Math.abs(result.longValue()) % totalClusterWeight;
		
		long aux = 0;
		for (ExperimentVariant abTestCluster : abTestClusters) {
			long nextAux = aux + abTestCluster.getWeight();
			if((aux <= res) && (res < nextAux)) return abTestCluster;
			aux = nextAux;
		}
		return null;
	}

}